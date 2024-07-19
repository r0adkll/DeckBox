package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.WavingHand
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon
import app.deckbox.playtest.api.model.Player
import com.moriatsushi.insetsx.navigationBars
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_hand
import org.jetbrains.compose.resources.stringResource

val PlayerBarHeight = 72.dp

@Composable
internal fun PlayerBar(
  player: Player,
  onHandClick: () -> Unit,
  navigationIcon: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .defaultMinSize(minHeight = PlayerBarHeight)
      .fillMaxWidth(),
    tonalElevation = 3.dp,
    shadowElevation = 4.dp,
  ) {
    Column {
      Row(
        modifier = Modifier
          .height(PlayerBarHeight)
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {

        navigationIcon()

        Spacer(Modifier.weight(1f))

        OutlinedIconButton(
          onClick = onHandClick,
          icon = {
            SizedIcon(
              icon = Icons.Rounded.WavingHand,
              contentDescription = null,
            )
          },
          label = {
            Text(stringResource(Res.string.play_mat_hand))
            Spacer(Modifier.width(8.dp))
            val shape = RoundedCornerShape(50)
            Text(
              text = player.hand.size.toString(),
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier
                .background(
                  color = MaterialTheme.colorScheme.secondaryContainer,
                  shape = shape,
                )
                .border(
                  width = 1.dp,
                  color = MaterialTheme.colorScheme.secondary,
                  shape = shape,
                )
                .padding(
                  horizontal = 8.dp,
                  vertical = 2.dp,
                ),
            )
          },
          contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp,
          )
        )
      }

      Spacer(
        modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars),
      )
    }
  }
}
