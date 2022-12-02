package com.kamiz.reflexgame.ui.game.composables

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class TileState {
    EMPTY,
    PENDING,
    DONE;
}

@Composable
fun GameBoardView(
    board: List<List<TileState>>,
    onTileClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val infinitelyAnimatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 200)
        )
    )

    Row(
        modifier
            .border(BorderStroke(2.dp, MaterialTheme.colors.onSurface))
            .fillMaxWidth()
    ) {
        board.forEachIndexed { x, row ->
            Column(Modifier.weight(1f)) {
                row.forEachIndexed { y, tileValue ->
                    GameTile(tileValue, { onTileClick(x, y) }, infinitelyAnimatedFloat)
                }
            }
        }
    }
}

@Composable
fun GameTile(
    tile: TileState,
    onTileClick: () -> Unit,
    infinitelyAnimatedFloat: State<Float>
) {
    val alpha = if (tile == TileState.PENDING) infinitelyAnimatedFloat.value else 1f
    val bgColor = if (tile == TileState.PENDING) MaterialTheme.colors.primary else Color.Transparent

    Box(
        Modifier
            .clickable { onTileClick() }
            .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface))
            .fillMaxWidth()
            .aspectRatio(1f)
            .alpha(alpha)
            .background(bgColor),
    )
}