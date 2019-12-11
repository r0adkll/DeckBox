package com.r0adkll.deckbuilder.util.glide.palette;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

/**
 * A {@link com.bumptech.glide.request.target.Target} that can display an {@link
 * Bitmap} in an {@link ImageView} and additionally set styles of
 * other views based on colors retrieved from a {@link Palette}.
 * <p>
 * Use the target directly:
 * <code><pre>
 * .into(new PaletteBitmapViewTarget(imageView, Collections.<PaletteAction>emptyList()) {
 *     {@code @}Override
 *     protected void setPalette(@Nullable Palette palette) {
 *         super.setPalette(palette);
 *         // use palette as you want
 *     }
 * })
 * </pre></code>
 * or using the builder for convenience:
 * <code><pre>
 * .into(new PaletteTargetBuilder(imageView)
 *     .background(MUTED_LIGHT, rootView)
 *     .swatch(VIBRANT).titleText(titleView).background(titleView, 0x80).finish()
 *     .build()
 * )
 * </pre></code>
 *
 * @see PaletteTargetBuilder
 */
public class PaletteBitmapSimpleTarget extends CustomTarget<PaletteBitmap> {

    private final Iterable<PaletteAction> actions;

    public PaletteBitmapSimpleTarget(@NonNull Iterable<PaletteAction> actions) {
        super();
        this.actions = Preconditions.checkNotNull(actions);
    }

    @Override
    public void onResourceReady(@NonNull PaletteBitmap resource, @Nullable Transition<? super PaletteBitmap> transition) {
        Palette palette = resource.palette;

        setPalette(palette);
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
    }

    protected void setPalette(Palette palette) {
        for (PaletteAction action : actions) {
            action.execute(palette);
        }
    }
}
