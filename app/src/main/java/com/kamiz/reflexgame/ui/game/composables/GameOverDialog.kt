package com.kamiz.reflexgame.ui.game.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.kamiz.reflexgame.R

@Composable
fun GameOverDialog(
    highestScore: Int,
    onRetry: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.game_over),
                style = MaterialTheme.typography.subtitle1,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.highestScore, highestScore),
                style = MaterialTheme.typography.subtitle1,
            )
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    )
}