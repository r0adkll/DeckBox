package com.r0adkll.deckbuilder.util.palette;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import static com.r0adkll.deckbuilder.util.palette.PaletteBitmapViewTarget.*;
import static com.r0adkll.deckbuilder.util.palette.PaletteTargetBuilder.*;

/**
 * Builder and executor of basic Swatch applications to Views.
 */
public class PaletteActionGroup implements SwatchBuilder, ReusableSwatchBuilder, PaletteAction {
    /**
     * Retrieve a swatch from a Palette based on some criteria which the implementors define. It is
     * not required to retrieve any swatch if no perfect match found.
     */
    public interface SwatchSelector {
        @Nullable
        Palette.Swatch select(@NonNull Palette palette);
    }

    /**
     * A target that can set its properties (such as background, text color, usually something visual
     * and colorful) based on a {@link Palette.Swatch}. Implementors must
     * handle lack of a swatch.
     */
    public interface SwatchTarget {
        /**
         * Use the selected swatch in any way you want. <code>null</code> swatch should trigger some
         * default values to prevent stale values when re-using views in a list.
         */
        void apply(@Nullable Palette.Swatch swatch);
    }

    private static final int OPAQUE = 0xFF;

    protected PaletteTargetBuilder builder;
    protected final SwatchSelector selector;

    protected boolean saved = false;
    protected final List<TextView> titles = new ArrayList<>();
    protected final List<ColorStateList> titlesSave = new ArrayList<>();
    protected final List<TextView> bodys = new ArrayList<>();
    protected final List<ColorStateList> bodysSave = new ArrayList<>();
    protected final List<ViewBackground> backgrounds = new ArrayList<>();
    protected final List<Drawable> backgroundsSave = new ArrayList<>();
    protected final List<SwatchTarget> customs = new ArrayList<>();

    public PaletteActionGroup(@NonNull SwatchSelector selector) {
        this.selector = Preconditions.checkNotNull(selector);
    }

    protected PaletteActionGroup(
            @NonNull PaletteTargetBuilder builder,
            @NonNull SwatchSelector selector) {
        this.builder = Preconditions.checkNotNull(builder);
        this.selector = Preconditions.checkNotNull(selector);
    }

    protected PaletteActionGroup(PaletteActionGroup other) {
        this(other.selector);

        this.titles.addAll(other.titles);
        this.bodys.addAll(other.bodys);
        this.backgrounds.addAll(other.backgrounds);
        this.customs.addAll(other.customs);

        this.saved = other.saved;
        this.titlesSave.addAll(other.titlesSave);
        this.bodysSave.addAll(other.bodysSave);
        this.backgroundsSave.addAll(other.backgroundsSave);
    }

    @NonNull
    public PaletteTargetBuilder finish() {
        if (builder == null) {
            throw new IllegalStateException("Cannot .finish() a " + ReusableSwatchBuilder.class
                    + ", use .build() or " + PaletteTargetBuilder.class + ".apply().");
        }
        checkEmpty();
        try {
            return builder.action(this);
        } finally {
            // forget it so its resources can be freed
            builder = null;
        }
    }

    protected void checkEmpty() {
        if (titles.isEmpty() && bodys.isEmpty() && backgrounds.isEmpty() && customs.isEmpty()) {
            throw new IllegalStateException("Cannot finish, the there's no swatch usage.");
        }
    }

    @NonNull
    @Override
    public PaletteAction build() {
        checkEmpty();
        return new PaletteActionGroup(this);
    }

    @NonNull
    public PaletteActionGroup titleText(@NonNull TextView view) {
        titles.add(view);
        return this;
    }


    @NonNull
    public PaletteActionGroup bodyText(@NonNull TextView view) {
        bodys.add(view);
        return this;
    }

    @NonNull
    public PaletteActionGroup background(@NonNull View view, int alpha) {
        backgrounds.add(new ViewBackground(view, alpha));
        return this;
    }

    @NonNull
    public PaletteActionGroup custom(@NonNull SwatchTarget target) {
        customs.add(target);
        return this;
    }

    public PaletteActionGroup title(@NonNull TextView view) {
        return this.background(view).titleText(view);
    }

    @NonNull
    public PaletteActionGroup body(@NonNull TextView view) {
        return this.background(view).bodyText(view);
    }

    @NonNull
    public PaletteActionGroup background(@NonNull View view) {
        return this.background(view, OPAQUE);
    }


    public void execute(@Nullable Palette palette) {
        ensureSaved();

        Palette.Swatch swatch = palette != null ? selector.select(palette) : null;

        if (swatch != null) {
            apply(swatch);
        } else {
            restoreSaved();
        }

        for (SwatchTarget custom : customs) {
            custom.apply(swatch);
        }
    }

    protected void apply(@NonNull Palette.Swatch swatch) {
        int titleTextColor = swatch.getTitleTextColor();
        for (TextView titleText : titles) {
            titleText.setTextColor(titleTextColor);
        }

        int bodyTextColor = swatch.getBodyTextColor();
        for (TextView bodyText : bodys) {
            bodyText.setTextColor(bodyTextColor);
        }

        int backgroundColor = swatch.getRgb();
        for (ViewBackground background : backgrounds) {
            background.view.setBackgroundColor(background.applyAlpha(backgroundColor));
        }
    }

    protected void ensureSaved() {
        if (!saved) {
            for (TextView titleText : titles) {
                titlesSave.add(titleText.getTextColors());
            }
            for (TextView bodyText : bodys) {
                bodysSave.add(bodyText.getTextColors());
            }
            for (ViewBackground background : backgrounds) {
                backgroundsSave.add(background.view.getBackground());
            }
            saved = true;
        }
    }


    @SuppressWarnings("deprecation")
    protected void restoreSaved() {
        for (int i = 0; i < titles.size(); i++) {
            TextView titleText = titles.get(i);
            titleText.setTextColor(titlesSave.get(i));
        }

        for (int i = 0; i < bodys.size(); i++) {
            TextView bodyText = bodys.get(i);
            bodyText.setTextColor(bodysSave.get(i));
        }

        for (int i = 0; i < backgrounds.size(); i++) {
            ViewBackground background = backgrounds.get(i);
            background.view.setBackgroundDrawable(backgroundsSave.get(i));
        }
    }

    private static class ViewBackground {
        protected final View view;
        @IntRange(from = 0, to = 255)
        protected final int alpha;

        public ViewBackground(View view, int alpha) {
            this.view = view;
            this.alpha = alpha;
        }

        @ColorInt
        public int applyAlpha(@ColorInt int color) {
            if (Color.alpha(color) < 0xFF) {
                // already has alpha set, don't modify it
                return color;
            }
            // return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
            return color & 0x00FFFFFF | alpha << 24;
        }
    }
}
