package com.kamiz.reflexgame.ui.game

import com.kamiz.reflexgame.ui.game.composables.TileState
import kotlin.random.Random

class GameBoard(
    gameInfo: GameInfo,
) {
    private val width: Int = gameInfo.gridWidth
    private val height: Int = gameInfo.gridHeight
    private val squaresPerRound: Int = gameInfo.squaresByRound

    val board = (0 until width).map {
        (0 until height).map {
            TileState.EMPTY
        }.toMutableList()
    }

    val isRoundCleared get() = board.flatten().count { it == TileState.DONE } == squaresPerRound

    fun randomizeNewSquares() {
        reset()
        addSquares()
    }

    fun get(x: Int, y: Int): TileState {
        return board[x][y]
    }

    fun markTouched(x: Int, y: Int) {
        if (board[x][y] == TileState.EMPTY) {
            throw WrongSquareException
        }
        board[x][y] = TileState.DONE
    }

    private fun reset() {
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                board[x][y] = TileState.EMPTY
            }
        }
    }

    private fun addSquares() {
        var squaresToAdd = squaresPerRound
        while (squaresToAdd > 0) {
            val randomX = Random.nextInt(width)
            val randomY = Random.nextInt(height)
            if (board[randomX][randomY] == TileState.EMPTY) {
                board[randomX][randomY] = TileState.PENDING
                squaresToAdd--
            }
        }
    }

    object WrongSquareException : Exception()
}

