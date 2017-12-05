package com.r0adkll.deckbuilder.util.palette;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Preconditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A builder to easily handle the different swatches and set the background and text colors.
 * <p>
 * <p>The following examples all do the same, with different levels of complexity and style. They
 * all can be repeated and combined on a single builder with ease.</p> <ol>
 * <p>
 * <li>
 * <p>
 * <b>Shorthand style</b>. Simple methods for simple use cases, one Swatch from the Palette is used
 * for a single View's single property.
 * <code><pre>
 * .into(new PaletteTargetBuilder(imageView)
 *     .background(MUTED, rootView)
 *     .title(MUTED, titleView)
 *     .build()
 * );
 * </pre></code>
 * <i>Notice that the swatch needs to be duplicated to set two properties or on different
 * Views.</i>
 * <p>
 * </li>
 * <p>
 * <li>
 * <p>
 * <b>Advanced usage</b> to apply a single swatch into multiple items.
 * <code><pre>
 * .into(new PaletteTargetBuilder(imageView)
 *     .swatch(MUTED).background(rootView).title(titleView).finish()
 *     .build()
 * );
 * </pre></code>
 * Method calls are in a single flow.
 * <p>
 * </li>
 * <p>
 * <li>
 * <p>
 * <b>Reusable swatch setup</b> which has the benefit of formatting sub-builders better,
 * <code><pre>
 * <p>
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(MUTED.as()
 *         .background(rootView)
 *         .title(titleView)
 *     )
 * );
 * </pre></code>
 * ... and can be re-used if necessary (e.g. with multiple builders):
 * <code><pre>
 * ReusableSwatchBuilder reusable = MUTED.as().background(rootView).title(titleView);
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(reusable)
 *     .build()
 * );
 * </pre></code>
 * </li>
 * <p>
 * </ol>
 * <p>
 * <p>
 * <p>
 * <b>Falling back to other swatches</b> if the selected one doesn't exists. In all of the above
 * examples it's possible to replace <code>MUTED</code> with <code>fallback(MUTED, MUTED_DARK,
 * MUTED_LIGHT)</code> to use one of the MUTED-type builtin swatches. This works with any selector
 * combination.
 * <p>
 * </p>
 * <p>
 * <p>
 * <b>Re-using custom selectors and targets</b>:
 * <code><pre>
 * PaletteActionGroup.SwatchSelector FANCIEST = ...;
 * PaletteActionGroup.SwatchTarget MY_TARGET = ...;
 * ReusableSwatchBuilder reusable = PaletteTargetBuilder.preApply(FANCIEST).custom(MY_TARGET);
 * .into(new PaletteTargetBuilder(imageView)
 *     .apply(reusable)
 *     .build()
 * );
 * </pre></code>
 */
public final class PaletteTargetBuilder {
    /**
     * Base class for selecting the 6 built-in Swatches from a Palette.
     */
    public abstract static class BuiltinSwatchSelector implements PaletteActionGroup.SwatchSelector {
        public ReusableSwatchBuilder as() {
            return preApply(this);
        }
    }

    public static final BuiltinSwatchSelector VIBRANT = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getVibrantSwatch();
        }
    };
    public static final BuiltinSwatchSelector VIBRANT_LIGHT = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getLightVibrantSwatch();
        }
    };
    public static final BuiltinSwatchSelector VIBRANT_DARK = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getDarkVibrantSwatch();
        }
    };

    public static final BuiltinSwatchSelector MUTED = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getMutedSwatch();
        }
    };
    public static final BuiltinSwatchSelector MUTED_LIGHT = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getLightMutedSwatch();
        }
    };
    public static final BuiltinSwatchSelector MUTED_DARK = new BuiltinSwatchSelector() {
        public Palette.Swatch select(@NonNull Palette palette) {
            return palette.getDarkMutedSwatch();
        }
    };

    @NonNull
    private final ImageView view;
    private final List<PaletteBitmapViewTarget.PaletteAction> actions = new LinkedList<>();

    public PaletteTargetBuilder(@NonNull ImageView view) {
        this.view = Preconditions.checkNotNull(view);
    }

    @NonNull
    public ImageView getView() {
        return view;
    }

    @NonNull
    public SwatchBuilder swatch(@NonNull PaletteActionGroup.SwatchSelector selector) {
        return new PaletteActionGroup(this, selector);
    }

    @NonNull
    public SwatchBuilder swatch(@NonNull PaletteActionGroup.SwatchSelector... selector) {
        return swatch(fallback(selector));
    }

    @NonNull
    public static BuiltinSwatchSelector fallback(@NonNull PaletteActionGroup.SwatchSelector... selector) {
        return new FallbackSwatchSelector(selector);
    }

    @NonNull
    public PaletteTargetBuilder action(@NonNull PaletteBitmapViewTarget.PaletteAction action) {
        actions.add(action);
        return this;
    }

    @NonNull
    public PaletteTargetBuilder apply(@NonNull ReusableSwatchBuilder builder) {
        return this.action(builder.build());
    }

    public static ReusableSwatchBuilder preApply(PaletteActionGroup.SwatchSelector selector) {
        return new PaletteActionGroup(selector);
    }

    public PaletteBitmapViewTarget build() {
        // make a copy in case the user keeps calling Builder methods after build()
        return new PaletteBitmapViewTarget(view, new ArrayList<>(PaletteTargetBuilder.this.actions));
    }

    public PaletteTargetBuilder title(@NonNull PaletteActionGroup.SwatchSelector selector, @NonNull TextView view) {
        return this.swatch(selector).title(view).finish();
    }

    public PaletteTargetBuilder titleText(@NonNull PaletteActionGroup.SwatchSelector selector, @NonNull TextView view) {
        return this.swatch(selector).titleText(view).finish();
    }

    public PaletteTargetBuilder body(@NonNull PaletteActionGroup.SwatchSelector selector, @NonNull TextView view) {
        return this.swatch(selector).body(view).finish();
    }

    public PaletteTargetBuilder bodyText(@NonNull PaletteActionGroup.SwatchSelector selector, @NonNull TextView view) {
        return this.swatch(selector).bodyText(view).finish();
    }

    public PaletteTargetBuilder background(@NonNull PaletteActionGroup.SwatchSelector selector, @NonNull View view) {
        return this.swatch(selector).background(view).finish();
    }

    public PaletteTargetBuilder background(
            @NonNull PaletteActionGroup.SwatchSelector selector, @NonNull View view, int alpha) {
        return this.swatch(selector).background(view, alpha).finish();
    }

    public PaletteTargetBuilder custom(
            @NonNull PaletteActionGroup.SwatchSelector selector, @NonNull PaletteActionGroup.SwatchTarget target) {
        return this.swatch(selector).custom(target).finish();
    }

    /**
     * A builder should support these applications of a Swatch to certain View properties.
     */
    public interface SwatchApplier {
        SwatchApplier title(@NonNull TextView view);

        SwatchApplier titleText(@NonNull TextView view);

        SwatchApplier body(@NonNull TextView view);

        SwatchApplier bodyText(@NonNull TextView view);

        SwatchApplier background(@NonNull View view);

        SwatchApplier background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

        SwatchApplier custom(@NonNull PaletteActionGroup.SwatchTarget target);
    }

    /**
     * This builder is to allow creating {@link PaletteBitmapViewTarget.PaletteAction}s in a reusable way. An implementation
     * of this builder should allow to create multiple distinct and independent actions.
     */
    public interface ReusableSwatchBuilder extends SwatchApplier {
        ReusableSwatchBuilder title(@NonNull TextView view);

        ReusableSwatchBuilder titleText(@NonNull TextView view);

        ReusableSwatchBuilder body(@NonNull TextView view);

        ReusableSwatchBuilder bodyText(@NonNull TextView view);

        ReusableSwatchBuilder background(@NonNull View view);

        ReusableSwatchBuilder background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

        ReusableSwatchBuilder custom(@NonNull PaletteActionGroup.SwatchTarget target);

        @NonNull
        PaletteBitmapViewTarget.PaletteAction build();
    }

    /**
     * This builder is to allow chaining swatch building amidst {@link PaletteTargetBuilder} calls.
     */
    public interface SwatchBuilder extends SwatchApplier {
        SwatchBuilder title(@NonNull TextView view);

        SwatchBuilder titleText(@NonNull TextView view);

        SwatchBuilder body(@NonNull TextView view);

        SwatchBuilder bodyText(@NonNull TextView view);

        SwatchBuilder background(@NonNull View view);

        SwatchBuilder background(@NonNull View view, @IntRange(from = 0, to = 255) int alpha);

        SwatchBuilder custom(@NonNull PaletteActionGroup.SwatchTarget target);

        @NonNull
        PaletteTargetBuilder finish();
    }

    public static class FallbackSwatchSelector extends BuiltinSwatchSelector {
        private final PaletteActionGroup.SwatchSelector[] selectors;

        public FallbackSwatchSelector(@NonNull PaletteActionGroup.SwatchSelector... selectors) {
            this.selectors = selectors; //Preconditions.checkNotEmpty(Preconditions.checkNotNull(selectors));
        }

        @Nullable
        @Override
        public Palette.Swatch select(@NonNull Palette palette) {
            for (PaletteActionGroup.SwatchSelector selector : selectors) {
                Palette.Swatch selected = selector.select(palette);
                if (selected != null) {
                    return selected;
                }
            }
            return null;
        }
    }
}
