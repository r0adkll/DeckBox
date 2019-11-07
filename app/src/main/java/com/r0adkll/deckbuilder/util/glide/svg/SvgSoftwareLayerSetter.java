package com.r0adkll.deckbuilder.util.glide.svg;

import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;

/**
 * Listener which updates the {@link ImageView} to be software rendered, because
 * {@link com.caverock.androidsvg.SVG SVG}/{@link android.graphics.Picture Picture} can't render on
 * a hardware backed {@link android.graphics.Canvas Canvas}.
 */
public class SvgSoftwareLayerSetter implements RequestListener<SVG> {

    @Override
    public boolean onLoadFailed(GlideException e, Object model, Target<SVG> target,
                                boolean isFirstResource) {
        ImageView view = ((ImageViewTarget<?>) target).getView();
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null);
        return false;
    }

    @Override
    public boolean onResourceReady(SVG resource, Object model,
                                   Target<SVG> target, DataSource dataSource, boolean isFirstResource) {
        ImageView view = ((ImageViewTarget<?>) target).getView();
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
        return false;
    }
}
