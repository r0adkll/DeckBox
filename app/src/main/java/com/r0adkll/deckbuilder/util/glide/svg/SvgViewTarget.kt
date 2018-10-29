package com.r0adkll.deckbuilder.util.glide.svg

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget
import com.caverock.androidsvg.SVG
import timber.log.Timber


class SvgViewTarget(imageView: ImageView) : ImageViewTarget<SVG>(imageView) {

    override fun setResource(resource: SVG?) {
        if (resource != null) {
            val ratio = resource.documentViewBox.height() / resource.documentViewBox.width()

            resource.documentWidth = view.width.toFloat()
            resource.documentHeight = view.width.toFloat() * ratio
            val picture = resource.renderToPicture()
            if (picture != null) {
                view.setImageDrawable(PictureDrawable(picture))
            } else {
                Timber.e("Unable to render SVG")
            }
        } else {
            Timber.e("Unable to render SVG")
        }
    }
}