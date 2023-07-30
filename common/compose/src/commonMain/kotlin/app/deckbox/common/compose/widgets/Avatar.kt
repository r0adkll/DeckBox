package app.deckbox.common.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImagePainter

@Composable
fun Avatar(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier
      .size(40.dp)
      .clip(CircleShape)
  ) {
    content()
  }
}

@Composable
fun ImageAvatar(
  url: String,
  modifier: Modifier = Modifier,
  contentDescription: String? = null,
  progress: @Composable BoxScope.() -> Unit = {
    CircularProgressIndicator(
      modifier = Modifier
        .size(24.dp)
        .align(Alignment.Center),
      color = MaterialTheme.colorScheme.primary,
    )
  }
) {
  val action by rememberImageAction(url)

  Avatar(modifier) {
    Image(
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
      painter = rememberImageActionPainter(action),
      contentDescription = contentDescription,
    )
    if (action is ImageEvent) {
      progress()
    }
  }
}
