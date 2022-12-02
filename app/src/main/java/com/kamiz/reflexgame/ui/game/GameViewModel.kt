package com.kamiz.reflexgame.ui.game

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.max
import kotlin.math.pow
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val INITIAL_SCORE = 1
private const val START_TIME = 0
private const val MINUTE_IN_SECONDS = 60
private const val HOUR_IN_SECONDS = 3600
private const val GAME_OVER_SCORE = 0

class ReflexGameViewModel : ViewModel() {

    private var roundJob: Job? = null
    private var timerJob: Job? = null

    private var gameInfo: GameInfo = GameInfo.BASIC
    private var gameStarted: Boolean = false
    private var _board by mutableStateOf(GameBoard(gameInfo))
    private var _score by mutableStateOf(INITIAL_SCORE)
    private var _highestScore = INITIAL_SCORE
    private var _isGameOver by mutableStateOf(false)
    private var _time by mutableStateOf(START_TIME)
    private var round = 1

    val highestScore get() = _highestScore
    val score by derivedStateOf { _score }
    val board by derivedStateOf { _board }
    val isGameOver by derivedStateOf { _isGameOver }
    val formattedTime by derivedStateOf { formatSeconds(_time) }

    fun startGame(gameInfo: GameInfo? = null) = viewModelScope.launch {
        this@ReflexGameViewModel.gameInfo = gameInfo ?: this@ReflexGameViewModel.gameInfo
        timerJob?.cancelAndJoin()
        roundJob?.cancelAndJoin()
        _isGameOver = false
        _score = INITIAL_SCORE
        _highestScore = INITIAL_SCORE
        _time = START_TIME
        _board = GameBoard(this@ReflexGameViewModel.gameInfo)
        gameStarted = true

        startTimer()
        startRound()
    }

    fun gameOver() = viewModelScope.launch {
        _isGameOver = true
        timerJob?.cancelAndJoin()
        roundJob?.cancelAndJoin()
    }

    fun onTileClick(x: Int, y: Int) {
        if (!gameStarted) return
        try {
            board.markTouched(x, y)
            if (board.isRoundCleared) {
                nextRound(completed = true)
            }
        } catch (e: GameBoard.WrongSquareException) {
            nextRound()
        }
    }

    private fun startTimer() {
        _time = 0
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _time += 1
            }
        }
    }

    private fun startRound() {
        round = 1
        roundJob = viewModelScope.launch() {
            _board.randomizeNewSquares()
            delay(getRoundTime())
            nextRound()
        }
    }

    private fun nextRound(completed: Boolean = false) {
        roundJob = viewModelScope.launch() {
            _board.randomizeNewSquares()
            _score += if (completed) 1 else -1
            _highestScore = max(_highestScore, score)
            if (score <= GAME_OVER_SCORE) {
                gameOver()
                return@launch
            }
            round += 1
            roundJob?.cancelAndJoin()
            delay(getRoundTime())
            nextRound()
        }
    }

    private fun getRoundTime(): Long {
        val speedFactor = gameInfo.speedFactor.pow((round / 5).toDouble())
        return (gameInfo.initialMsTimeout * speedFactor).toLong()
    }

    private fun formatSeconds(seconds: Int): String {
        val hh = seconds / HOUR_IN_SECONDS
        val mm = (seconds - hh * HOUR_IN_SECONDS) / MINUTE_IN_SECONDS
        val ss = seconds % MINUTE_IN_SECONDS
        return String.format("%02d:%02d:%02d", hh, mm, ss)
    }
}