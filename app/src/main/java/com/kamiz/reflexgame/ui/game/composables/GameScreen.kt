package com.kamiz.reflexgame.ui.game.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kamiz.reflexgame.R
import com.kamiz.reflexgame.ui.game.GameInfo
import com.kamiz.reflexgame.ui.game.ReflexGameViewModel


@Composable
fun GameScreen(vm: ReflexGameViewModel = viewModel()) {

    if (vm.isGameOver) {
        GameOverDialog(
            highestScore = vm.highestScore,
            onRetry = vm::startGame
        )
    }

    Column(Modifier.padding(16.dp)) {
        Row() {
            Text(
                text = stringResource(R.string.score, vm.score),
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = vm.formattedTime,
                style = MaterialTheme.typography.h6,
            )
        }

        GameBoardView(
            board = vm.board.board,
            onTileClick = vm::onTileClick,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(
                onClick = { vm.startGame(GameInfo.BASIC) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.start_basic))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { vm.startGame(GameInfo.PRO) },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.start_pro))
            }
        }
        OutlinedButton(
            onClick = { vm.gameOver() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Text(text = stringResource(R.string.surrender))
        }
    }
}
