package com.valentinilk.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberShimmer(
    shimmerBounds: ShimmerBounds,
    theme: ShimmerTheme = LocalShimmerTheme.current,
): Shimmer {
    val effect = rememberShimmerEffect(theme)
    val bounds  = rememberShimmerBounds(shimmerBounds)

    return remember(theme, effect, bounds) {
        Shimmer(theme, effect, bounds)
    }
}

data class Shimmer internal constructor(
    internal val theme: ShimmerTheme,
    internal val effect: ShimmerEffect,
    internal val bounds: Rect?,
) {

    internal val boundsFlow = MutableStateFlow(bounds)

    fun updateBounds(bounds: Rect?) {
        boundsFlow.value = bounds
    }
}
