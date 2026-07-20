package com.weberpackage.blackjack.betting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.composeunstyled.DialogState
import com.composeunstyled.UnstyledDialog
import com.composeunstyled.UnstyledDialogPanel
import com.composeunstyled.UnstyledScrim
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.model.ThousandsSeparatorTransformation
import com.weberpackage.blackjack.common.presentation.utils.fadeEnterTransition
import com.weberpackage.blackjack.common.presentation.utils.fadeExitTransition
import com.weberpackage.blackjack.common.presentation.utils.scaleEnterTransition

@Composable
fun CustomBestDialog(
    dialogState: DialogState,
    customBet: Int,
    onSelectCustomBet: (amount: Int) -> Unit,
) {
    var textFieldValue by remember(customBet) {
        val text = customBet.toString()
        mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length)))
    }

    UnstyledDialog(
        state = dialogState,
    ) {
        UnstyledScrim(
            enter = fadeEnterTransition(200),
            exit = fadeExitTransition(200)
        )
        UnstyledDialogPanel(
            modifier = Modifier
                .padding(24.dp)
                .imePadding()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(24.dp),
            enter = scaleEnterTransition(),
            exit = fadeExitTransition(200)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.custom_bet),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        if (newValue.text.all { it.isDigit() }) {
                            textFieldValue = newValue
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    placeholder = { Text("0") },
                    visualTransformation = ThousandsSeparatorTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { dialogState.visible = false }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amount = textFieldValue.text.toIntOrNull() ?: 0
                            onSelectCustomBet(amount)
                            dialogState.visible = false
                        }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}