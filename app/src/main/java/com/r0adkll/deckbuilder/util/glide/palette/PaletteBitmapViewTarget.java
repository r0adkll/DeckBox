package com.r0adkll.deckbuilder.util.glide.palette;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;
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
public class PaletteBitmapViewTarget extends ImageViewTarget<PaletteBitmap> {
    /**
     * An action executed when resource is ready to be set into the Target.
     */
    public interface PaletteAction {
        void execute(@Nullable Palette palette);
    }

    private final Iterable<PaletteAction> actions;

    public PaletteBitmapViewTarget(
            @NonNull ImageView view,
            @NonNull Iterable<PaletteAction> actions) {
        super(view);
        this.actions = Preconditions.checkNotNull(actions);
    }

    @Override
    protected void setResource(@Nullable PaletteBitmap resource) {
        Bitmap bitmap = resource != null ? resource.bitmap : null;
        Palette palette = resource != null ? resource.palette : null;

        view.setImageBitmap(bitmap);
        setPalette(palette);
    }

    protected void setPalette(Palette palette) {
        for (PaletteAction action : actions) {
            action.execute(palette);
        }
    }
}
