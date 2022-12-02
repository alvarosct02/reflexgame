package com.kamiz.reflexgame.ui.game

data class GameInfo(
    val gridWidth: Int,
    val gridHeight: Int,
    val squaresByRound: Int,
    val initialMsTimeout: Long,
    val speedFactor: Double,
) {
    companion object {

        val BASIC = GameInfo(
            gridWidth = 4,
            gridHeight = 4,
            initialMsTimeout = 3000L,
            speedFactor = 0.8,
            squaresByRound = 1,
        )

        val PRO = GameInfo(
            gridWidth = 6,
            gridHeight = 6,
            initialMsTimeout = 5000L,
            speedFactor = 0.9,
            squaresByRound = 2,
        )
    }
}

