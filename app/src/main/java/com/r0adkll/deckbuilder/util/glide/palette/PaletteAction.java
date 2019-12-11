package com.r0adkll.deckbuilder.util.glide.palette;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

/**
 * An action executed when resource is ready to be set into the Target.
 */
public interface PaletteAction {
    void execute(@Nullable Palette palette);
}
