package app.deckbox.common.compose.imageloading

import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.ContributesTo

expect interface PlatformImageLoaderComponent

@ContributesTo(MergeActivityScope::class)
interface ImageLoaderComponent : PlatformImageLoaderComponent
