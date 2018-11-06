package com.r0adkll.deckbuilder.arch.ui.components.preview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Base64
import android.view.View
import android.widget.ImageView
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview.PreviewSpec
import com.r0adkll.deckbuilder.arch.ui.widgets.AspectRatioImageView
import com.r0adkll.deckbuilder.util.extensions.margins
import com.r0adkll.deckbuilder.util.glide.AlphaTransformation
import com.r0adkll.deckbuilder.util.glide.RxGlide.asObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


object ExpansionPreviewRenderer {

    fun applyLogo(imageView: ImageView, spec: PreviewSpec.LogoSpec) {
        GlideApp.with(imageView)
                .load(spec.url)
                .into(imageView)
        imageView.margins(spec.margins)
    }


    fun applyBackground(view: View, specs: List<PreviewSpec.DrawableSpec>): Disposable {
        val drawables = specs.map { createDrawable(view.context, it) }
        return Observable.merge(drawables)
                .toList()
                .map { LayerDrawable(it.toTypedArray()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.setBounds(0, 0, view.width, view.height)
                    view.background = it
                }, {
                    Timber.e(it, "Error applying preview spec background")
                })
    }


    fun applyForeground(imageView: ImageView, spec: PreviewSpec.DrawableSpec): Disposable? {
        // Apply options
        imageView.margins(spec.margins)
        spec.alpha?.let { imageView.alpha = it }
        spec.aspectRatio?.let { aspectRatio ->
            if (imageView is AspectRatioImageView) {
                imageView.ratioType = if (aspectRatio) {
                    AspectRatioImageView.RATIO_WIDTH
                } else {
                    AspectRatioImageView.RATIO_NONE
                }
            }
        }

        // Due to Issue #56 - If it's an URL shortcut to using glide
        if (spec.source.type == "url") {
            var request = GlideApp.with(imageView)
                    .load(spec.source.value)

            spec.alpha?.let {
                request = request.transform(AlphaTransformation(it))
            }

            request.into(imageView)

            return null
        } else {
            return createDrawable(imageView.context, spec)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        imageView.setImageDrawable(it)
                    }, {
                        Timber.e(it, "Error loading foreground drawable")
                    })
        }
    }


    private fun createDrawable(context: Context, spec: PreviewSpec.DrawableSpec): Observable<Drawable> {
        return when(spec.source.type) {
            "base64" -> base64Drawable(context, spec)
            "url" -> urlDrawable(context, spec)
            "color" -> colorDrawable(spec)
            else -> Observable.empty()
        }
    }


    private fun base64Drawable(context: Context, spec: PreviewSpec.DrawableSpec): Observable<Drawable> {
        return Observable.just(spec.source.value)
                .map {
                    val bytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                .map {
                    BitmapDrawable(context.resources, it).apply {
                        spec.tile?.let { tile ->
                            setTileModeXY(tile.tileModeX, tile.tileModeY)
                        }
                        spec.source.density?.let { density ->
                            setTargetDensity((context.resources.displayMetrics.densityDpi * density).toInt())
                        }
                        spec.alpha?.let { alpha ->
                            this.alpha = (255f * alpha).toInt()
                        }
                    } as Drawable
                }
                .subscribeOn(Schedulers.computation())
    }


    private fun urlDrawable(context: Context, spec: PreviewSpec.DrawableSpec): Observable<Drawable> {
        var request = GlideApp.with(context)
                .load(spec.source.value)

        spec.alpha?.let {
            request = request.transform(AlphaTransformation(it))
        }

        return request.asObservable(context)
    }


    private fun colorDrawable(spec: PreviewSpec.DrawableSpec): Observable<Drawable> {
        val color = Color.parseColor(spec.source.value)
        val drawable = ColorDrawable(color)
        spec.alpha?.let {
            drawable.alpha = (255f * it).toInt()
        }
        return Observable.just(drawable)
    }
}