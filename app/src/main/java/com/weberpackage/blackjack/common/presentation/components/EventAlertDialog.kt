package com.weberpackage.blackjack.common.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeunstyled.DialogProperties
import com.composeunstyled.UnstyledDialog
import com.composeunstyled.UnstyledDialogPanel
import com.composeunstyled.UnstyledScrim
import com.composeunstyled.rememberDialogState
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.state.EventDialogState
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.spacing
import com.weberpackage.blackjack.common.presentation.utils.DialogAction
import com.weberpackage.blackjack.common.presentation.utils.DialogEvent
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.asString
import com.weberpackage.blackjack.common.presentation.utils.fadeEnterTransition
import com.weberpackage.blackjack.common.presentation.utils.fadeExitTransition
import com.weberpackage.blackjack.common.presentation.utils.scaleEnterTransition
import kotlinx.coroutines.launch

@Composable
fun EventAlertDialog(
    modifier: Modifier = Modifier,
    eventDialogState: EventDialogState
) {
    UnstyledDialog(
        state = eventDialogState.state,
        properties = DialogProperties(
            dismissOnBackPress = eventDialogState.event.dismissible,
            dismissOnClickOutside = eventDialogState.event.dismissible
        )
    ) {
        UnstyledScrim(
            enter = fadeEnterTransition(200),
            exit = fadeExitTransition(200)
        )
        UnstyledDialogPanel(
            modifier = modifier
                .displayCutoutPadding()
                .systemBarsPadding()
                .widthIn(min = 280.dp, max = 560.dp)
                .padding(MaterialTheme.spacing.mediumOne)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            enter = scaleEnterTransition(),
            exit = fadeExitTransition(200)
        ) {
            DialogContent(
                eventDialogState = eventDialogState
            )
        }
    }
}

@Composable
private fun DialogContent(
    eventDialogState: EventDialogState
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(
                start = MaterialTheme.spacing.mediumTwo,
                top = MaterialTheme.spacing.mediumTwo,
                end = MaterialTheme.spacing.mediumTwo
            )
        ) {
            Text(
                text = eventDialogState.event.title.asString(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = eventDialogState.event.message.asString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.smallTwo)
                .padding(end = MaterialTheme.spacing.smallOne)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallOne)
        ) {
            eventDialogState.event.negativeAction?.let { event ->
                OutlinedButton(
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.smallThree),
                    onClick = {
                        eventDialogState.dismiss()
                        scope.launch {
                            event.action.invoke()
                        }
                    }
                ) {
                    Text(
                        text = event.buttonText.asString(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            eventDialogState.event.positiveAction.let { event ->
                Button(
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.smallThree),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                    ),
                    onClick = {
                        eventDialogState.dismiss()
                        scope.launch {
                            event.action.invoke()
                        }
                    },
                ) {
                    Text(
                        text = event.buttonText.asString(),
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun EventAlertDialogPreview() {
    BlackJackTheme{
        Surface {
            DialogContent(
                eventDialogState = EventDialogState(
                    state = rememberDialogState(),
                    dialogEvent = DialogEvent(
                        title = UiText(R.string.dialog_exit_game_title),
                        message = UiText(R.string.dialog_exit_game_desc),
                        positiveAction = DialogAction(
                            buttonText = UiText(R.string.yes),
                            action = {}
                        ),
                        negativeAction = DialogAction(
                            buttonText = UiText(R.string.no),
                            action = {}
                        )
                    )
                )
            )
        }
    }
}