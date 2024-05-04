package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _editSquareVector: ImageVector? = null

val Icons.Rounded.EditSquare: ImageVector
  get() {
    if (_editSquareVector != null) {
      return _editSquareVector!!
    }
    _editSquareVector = ImageVector.Builder(
      name = "edit_square",
      defaultWidth = 40.0.dp,
      defaultHeight = 40.0.dp,
      viewportWidth = 40.0f,
      viewportHeight = 40.0f,
    ).apply {
      path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1f,
        stroke = null,
        strokeAlpha = 1f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(7.875f, 39.25f)
        quadToRelative(-1.042f, 0f, -1.833f, -0.792f)
        quadToRelative(-0.792f, -0.791f, -0.792f, -1.833f)
        verticalLineToRelative(-24.25f)
        quadToRelative(0f, -1.042f, 0.792f, -1.833f)
        quadToRelative(0.791f, -0.792f, 1.833f, -0.792f)
        horizontalLineTo(24f)
        lineToRelative(-2.625f, 2.625f)
        horizontalLineToRelative(-13.5f)
        verticalLineToRelative(24.25f)
        horizontalLineToRelative(24.25f)
        verticalLineTo(23f)
        lineToRelative(2.625f, -2.625f)
        verticalLineToRelative(16.25f)
        quadToRelative(0f, 1.042f, -0.792f, 1.833f)
        quadToRelative(-0.791f, 0.792f, -1.833f, 0.792f)
        close()
        moveTo(20f, 24.5f)
        close()
        moveTo(27.5f, 10f)
        lineToRelative(1.875f, 1.875f)
        lineTo(17.708f, 23.5f)
        verticalLineToRelative(3.292f)
        horizontalLineToRelative(3.25f)
        lineToRelative(11.709f, -11.709f)
        lineToRelative(1.875f, 1.834f)
        lineToRelative(-11.709f, 11.708f)
        quadTo(22.5f, 29f, 22f, 29.208f)
        quadToRelative(-0.5f, 0.209f, -1.042f, 0.209f)
        horizontalLineToRelative(-4.583f)
        quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
        reflectiveQuadToRelative(-0.375f, -0.917f)
        verticalLineToRelative(-4.583f)
        quadToRelative(0f, -0.542f, 0.209f, -1.042f)
        quadToRelative(0.208f, -0.5f, 0.583f, -0.833f)
        close()
        moveToRelative(7.042f, 6.917f)
        lineTo(27.5f, 10f)
        lineToRelative(4.167f, -4.167f)
        quadToRelative(0.75f, -0.75f, 1.854f, -0.75f)
        reflectiveQuadToRelative(1.854f, 0.792f)
        lineToRelative(3.208f, 3.25f)
        quadToRelative(0.792f, 0.792f, 0.792f, 1.875f)
        reflectiveQuadToRelative(-0.792f, 1.833f)
        close()
      }
    }.build()
    return _editSquareVector!!
  }
