package com.r0adkll.deckbuilder.util.glide.palette;

import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;

/**
 * A simple wrapper for a {@link Palette} and a {@link
 * Bitmap}.
 */
public class PaletteBitmap {
    public final Palette palette;
    public final Bitmap bitmap;

    public PaletteBitmap(Bitmap bitmap, Palette palette) {
        this.bitmap = bitmap;
        this.palette = palette;
    }
}
