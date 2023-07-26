import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moriatsushi.insetsx.systemBars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBoxRootAppBar(
  title: String,
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
) {
  TopAppBar(
    modifier = modifier,
    windowInsets = WindowInsets.systemBars
      .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    scrollBehavior = scrollBehavior,
    title = { Text(text = title) },
    actions = {
    },
  )
}
