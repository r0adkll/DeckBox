package com.r0adkll.deckbuilder.util.glide.palette;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

/**
 * A {@link Resource} implementation for {@link
 * com.bumptech.glide.integration.palette.PaletteBitmap}.
 */
public class PaletteBitmapResource implements Resource<PaletteBitmap> {
    private PaletteBitmap paletteBitmap;
    private BitmapPool bitmapPool;

    public PaletteBitmapResource(PaletteBitmap paletteBitmap, BitmapPool bitmapPool) {
        this.paletteBitmap = paletteBitmap;
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Class<PaletteBitmap> getResourceClass() {
        return PaletteBitmap.class;
    }

    @Override
    public PaletteBitmap get() {
        return paletteBitmap;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(paletteBitmap.bitmap);
    }

    @Override
    public void recycle() {
        bitmapPool.put(paletteBitmap.bitmap);
    }
}
