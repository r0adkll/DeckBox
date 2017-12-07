package com.r0adkll.deckbuilder.util.palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.r0adkll.deckbuilder.GlideApp;

/**
 * A {@link ResourceTranscoder} for generating {@link
 * Palette}s from {@link Bitmap}s in the background.
 */
public class PaletteBitmapTranscoder implements ResourceTranscoder<Bitmap, PaletteBitmap> {
    /**
     * Extension point to allow for customizing Palette.Builder to generate a Palette.
     */
    public interface PaletteGenerator {
        Palette generate(Bitmap bitmap);
    }

    private final BitmapPool bitmapPool;
    private final PaletteGenerator generator;

    public PaletteBitmapTranscoder(Glide glide) {
        this(glide, new DefaultPaletteGenerator(null));
    }

    /**
     * @param numColors maximum number of swatches to generate (may be less)
     * @see Palette#generate(Bitmap, int)
     */
    public PaletteBitmapTranscoder(Glide glide, int numColors) {
        this(glide, new DefaultPaletteGenerator(numColors));
    }

    /**
     * @param generator custom generator to set up Palette.Builder and generate a Palette
     * @see Palette
     */
    public PaletteBitmapTranscoder(Glide glide, PaletteGenerator generator) {
        this.bitmapPool = glide.getBitmapPool();
        this.generator = generator;
    }

    @Override
    public Resource<PaletteBitmap> transcode(Resource<Bitmap> toTranscode) {
        Palette palette = generator.generate(toTranscode.get());
        PaletteBitmap result = new PaletteBitmap(toTranscode.get(), palette);
        return new PaletteBitmapResource(result, bitmapPool);
    }

    private static class DefaultPaletteGenerator implements PaletteGenerator {
        private final Integer numColors;

        public DefaultPaletteGenerator(Integer numColors) {
            this.numColors = numColors;
        }

        @Override
        public Palette generate(Bitmap bitmap) {
            Palette.Builder builder = new Palette.Builder(bitmap);
            if (numColors != null) {
                builder.maximumColorCount(numColors);
            }
            return builder.generate();
        }
    }
}
