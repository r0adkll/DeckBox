package com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.Pools
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.ftinc.kit.kotlin.extensions.*
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.BezelImageView
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.*
import com.r0adkll.deckbuilder.util.glide.EnergyCropTransformation
import com.r0adkll.deckbuilder.util.glide.ToolCropTransformation
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import timber.log.Timber
import java.util.*
import kotlin.math.roundToInt


class BoardCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        cardSize: Size = Size.SMALL,
        card: Board.Card? = null,
        startFaceDown: Boolean = false
) : ViewGroup(context, attrs, defStyleAttr), BoardView.Draggable {

    enum class Size(
            val cardRadiusDp: Float,
            val toolRadiusDp: Float,
            val toolStrokeDp: Float,
            val energyPaddingDp: Float
    ) {
        SMALL(4f, 2f, 1f, 2f),
        LARGE(16f, 4f, 2f, 4f);

        fun toolMaskDrawable(context: Context): ShapeDrawable {
            val corner = toolRadiusDp.dp(context)
            return ShapeDrawable(RoundRectShape(floatArrayOf(corner, corner, corner, corner, corner, corner, corner, corner),
                    null, null))
        }

        fun cardMaskDrawable(context: Context): ShapeDrawable {
            val corner = cardRadiusDp.dp(context)
            return ShapeDrawable(RoundRectShape(floatArrayOf(corner, corner, corner, corner, corner, corner, corner, corner),
                    null, null))
        }

        fun toolStrokeDrawable(context: Context): ShapeDrawable {
            return toolMaskDrawable(context).apply {
                paint.apply {
                    style = Paint.Style.STROKE
                    color = context.color(R.color.white)
                    strokeWidth = toolStrokeDp.dp(context)
                }
            }
        }
    }

    /**
     * Custom layout parameters for the children of this view group
     */
    class LayoutParams(width: Int, height: Int) : ViewGroup.LayoutParams(width, height) {

        internal var widthRatio: Float? = null
        internal var heightRatio: Float? = null


        /**
         *  Generate a Ratio based width and height [LayoutParams]
         */
        constructor(widthRatio: Float, heightRatio: Float) : this(RATIO, RATIO) {
            this.widthRatio = widthRatio
            this.heightRatio = heightRatio
        }

        /**
         * Generate a Square ratio-width based [LayoutParams]
         */
        constructor(widthRatio: Float) : this(RATIO, SQUARE) {
            this.widthRatio = widthRatio
        }


        companion object {

            /**
             * Special value for the height or width requested by a View.
             * MATCH_PARENT means that the view wants to be as big as its parent,
             * minus the parent's padding, if any. Introduced in API Level 8.
             */
            const val MATCH_PARENT = -1

            /**
             * Special value for the height or width requested by a View.
             * WRAP_CONTENT means that the view wants to be just large enough to fit
             * its own internal content, taking its own padding into account.
             */
            const val WRAP_CONTENT = -2

            /**
             * Special value for the height or width requested by a View.
             * [RATIO] means that the view wants to be a ratio of it's parent's measured dimensions
             * by applying [widthRatio] and [heightRatio]
             */
            const val RATIO = -3

            /**
             * Special value for the height or width requested by a View.
             * [SQUARE] means that the view's dimension wants to be the same size as it's other dimension
             * typically favoring the width of the view as the baseline
             */
            const val SQUARE = -4
        }
    }

    override val cardType: SuperType
        get() = card?.pokemons?.first?.supertype ?: SuperType.UNKNOWN

    override val cardSubtype: SubType
        get() = card?.pokemons?.first?.subtype ?: SubType.UNKNOWN

    /**
     * The [Board.Card] State that this view will be rendering
     */
    var card: Board.Card? = card
        set(value) {
            if (value != field) {
                field = value
                balanceAttachmentViews()
            }
        }

    /**
     * The class of size that this view is
     */
    var size: Size = cardSize
        set(value) {
            field = value

            toolMaskDrawable = value.toolMaskDrawable(context)
            toolStrokeDrawable = value.toolStrokeDrawable(context)

            image.maskDrawable = value.cardMaskDrawable(context)
            tools.forEach {
                it.maskDrawable = toolMaskDrawable
                it.borderDrawable = toolStrokeDrawable
            }

            requestLayout()
        }

    var isFaceDown: Boolean = startFaceDown
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
                balanceAttachmentViews()
            }
        }

    /**
     * Enable or disable debugging mode to print log statements and debug renderering
     */
    var debug = false

    private val debugRect = Rect()
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(1f)
    }

    private var toolMaskDrawable: Drawable?
    private var toolStrokeDrawable: Drawable?
    private var energyStrokeDrawable: Drawable?

    private val pool = Pools.SimplePool<BezelImageView>(10)
    private val energies = ArrayList<BezelImageView>()
    private val tools = ArrayList<BezelImageView>()
    private val image = BezelImageView(context)
    private val damage = TextView(context)
    private val status = ImageView(context)

    private val paddingRightExtra by lazy { dipToPx(4f) }
    private val paddingBottomExtra by lazy { dipToPx(4f) }


    init {
        setWillNotDraw(false)
        clipChildren = false // make sure energies and tools don't get clipped
        clipToPadding = false
        clipToOutline = false

        toolMaskDrawable = size.toolMaskDrawable(context)
        toolStrokeDrawable = size.toolStrokeDrawable(context)
        energyStrokeDrawable = context.getDrawable(R.drawable.dr_mask_energy_card_stroke)

        image.maskDrawable = size.cardMaskDrawable(context)
        val imageLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(image, imageLp)

        // setup damage textview
        damage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        damage.setTextColor(color(R.color.white))
        damage.setBackgroundResource(R.drawable.dr_damage_background)
        val padLR = dipToPx(8f)
        val padTB = dipToPx(2f)
        damage.setPaddingRelative(padLR, padTB, padLR, padTB)
        damage.gravity = Gravity.CENTER
        val damageLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(damage, damageLp)

        // Setup status marker
        status.gone()
        status.setPadding(dipToPx(3f))
        status.imageTintList = ColorStateList.valueOf(Color.WHITE)
        val fillDrawable = ShapeDrawable(OvalShape()).apply {
            paint.style = Paint.Style.FILL
        }
        val borderDrawable = ShapeDrawable(OvalShape()).apply {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = dpToPx(1f)
        }
        status.background = LayerDrawable(arrayOf(fillDrawable, borderDrawable))
        val statusLp = LayoutParams(dipToPx(24f), dipToPx(24f))
        addView(status, statusLp)

        balanceAttachmentViews()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /*
         * Configure base spec'd element width
         */
        val specWidth: Int
        val specHeight: Int
        if (parent is BoardView) {
            val boardView = parent as BoardView
            specWidth = boardView.elementWidth.roundToInt()
            specHeight = boardView.elementHeight.roundToInt()
        } else {
            // Get the requested sizing of this view, must be added to parent with
            specWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec) /* Parent Width*/
            specHeight = (specWidth * PokemonCardView.RATIO).roundToInt() /* Parent Height */
        }

        if (debug) Timber.i("onMeasure(specWidth=$specWidth, specHeight=$specHeight")

        if (!isFaceDown) {

            /*
             * Measure the root image to essentially 'MATCH_PARENT' as it will take up the entire
             * space of this view
             */
            image.measure(
                    MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.EXACTLY)
            )

            if (debug) Timber.i("imageMeasure(w=${image.measuredWidth}, h=${image.measuredHeight}")

            /*
             * Calculate right padding if tools are attached
             */
            if (tools.isNotEmpty()) {
                val padRight = ((specWidth * TOOL_WIDTH_RATIO) / 2f).roundToInt() + paddingRightExtra
                updatePadding(right = padRight)
            }

            /*
             * Calculate bottom padding if energy is attached
             */
            if (energies.isNotEmpty()) {
                val padBottom = ((specWidth * ENERGY_SIZE_RATIO) / 2f).roundToInt() + paddingBottomExtra

                // Update the padding for future child measurements
                updatePadding(bottom = padBottom)
            }

            var parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth + paddingRight, MeasureSpec.EXACTLY)
            var parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(specHeight + paddingBottom, MeasureSpec.EXACTLY)

            /*
             * Let the damage text view measure it self to account for the custom text and spacing
             */
            if (card?.damage?.let { it > 0 } == true) {
                measureChild(damage, parentWidthMeasureSpec, parentHeightMeasureSpec)
            }

            if (debug) Timber.i("damageMeasure(w=${damage.measuredWidth}, h=${damage.measuredHeight}")

            // Update the top padding for our damage counter view
            val padTop = damage.measuredHeight / 2
            updatePadding(top = padTop)

            /*
             * Measure status marker
             */
            if (status.visibility != View.GONE) {
                measureChild(status, parentWidthMeasureSpec, parentHeightMeasureSpec)
            }

            // Update our parent measure spec with updating paddings
            parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth + paddingRight, MeasureSpec.EXACTLY)
            parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(specHeight + paddingBottom + padTop, MeasureSpec.EXACTLY)

            /*
             * Measure each tool taking into account the custom LayoutParams object for this ViewGroup
             */
            tools.forEach {
                measureChildAttachment(it, parentWidthMeasureSpec, parentHeightMeasureSpec)
                if (debug) Timber.i("toolMeasure(w=${it.measuredWidth}, h=${it.measuredHeight})")
            }

            /*
             * Measure each tool taking into account the custom LayoutParams object for this ViewGroup
             */
            energies.forEach {
                measureChildAttachment(it, parentWidthMeasureSpec, parentHeightMeasureSpec)
                if (debug) Timber.i("energyMeasure(w=${it.measuredWidth}, h=${it.measuredHeight})")
            }

        } else {
            setPadding(0, 0, 0, 0)
        }

        setMeasuredDimension(specWidth + paddingLeft + paddingRight, specHeight + paddingTop + paddingBottom)

        pivotX = paddingLeft + (specWidth / 2f)
        pivotY = paddingTop + (specHeight / 2f)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftPos = paddingLeft
        val rightPos = r - l - paddingRight
        val topPos = paddingTop
        val bottomPos = b - t - paddingBottom

        // Layout image
        image.layout(leftPos, topPos, rightPos, bottomPos)
        if (debug) Timber.i("Image Layout(l=$leftPos, t=$topPos, r=$rightPos, b=$bottomPos)")

        if (!isFaceDown) {

            // Layout damage
            val damageLeft = ((rightPos - leftPos) / 2) - (damage.measuredWidth / 2)
            val damageTop = 0
            damage.layout(damageLeft, damageTop, damageLeft + damage.measuredWidth, damageTop + damage.measuredHeight)
            if (debug) Timber.i("Damage Layout(l=$damageLeft, t=$damageTop, r=${damageLeft + damage.measuredWidth}, b=${damageTop + damage.measuredHeight})")

            val statusLeft = ((rightPos - leftPos) / 2) - (status.measuredWidth / 2)
            val statusTop = ((bottomPos - topPos) / 2) - (status.measuredHeight / 2)
            status.layout(statusLeft, statusTop, statusLeft + status.measuredWidth, statusTop + status.measuredHeight)
            if (debug) Timber.i("Status Layout(l=$statusLeft, t=$statusTop, r=${statusLeft + status.measuredWidth}, b=${statusTop + status.measuredHeight})")

            // Layout Tools
            val toolOffsetY = ((bottomPos - topPos) * TOOL_OFFSET_Y_RATIO).roundToInt()
            tools.forEachIndexed { index, view ->
                val toolX = rightPos - (view.measuredWidth / 2)
                val toolY = topPos + toolOffsetY + (index * view.measuredHeight) + (index * dipToPx(4f))
                view.layout(toolX, toolY, toolX + view.measuredWidth, toolY + view.measuredHeight)
                if (debug) Timber.i("Tool($index) Layout(l=$toolX, t=$toolY, r=${toolX + view.measuredWidth}, b=${toolY + view.measuredHeight})")
            }

            // Layout Energies
            val energyPadding = size.energyPaddingDp.dip(context)
            energies.forEachIndexed { index, view ->
                val energyX = leftPos + energyPadding + (index * view.measuredWidth) + (index * energyPadding)
                val energyY = bottomPos - (view.measuredHeight / 2)
                view.layout(energyX, energyY, energyX + view.measuredWidth, energyY + view.measuredHeight)
                if (debug) Timber.i("Energy($index) Layout(l=$energyX, t=$energyY, r=${energyX + view.measuredWidth}, b=${energyY + view.measuredHeight})")
            }

        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (debug) {
            debugPaint.color = Color.RED

            canvas.drawRoundRect(RectF(debugRect.set(image)), dpToPx(8f), dpToPx(8f), debugPaint)
            canvas.drawRect(debugRect.set(damage), debugPaint)

            tools.forEach {
                canvas.drawRect(debugRect.set(it), debugPaint)
            }

            energies.forEach {
                canvas.drawRect(debugRect.set(it), debugPaint)
            }

            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), debugPaint)

            debugPaint.color = Color.GREEN
            val radius = dpToPx(2f)
            canvas.drawCircle(pivotX, pivotY, radius, debugPaint)
        }
    }

    override fun onDragStart() {
        translationZ = dpToPx(2f)
        if (rotation != 0f) {
            animate().rotation(0f)
                    .setDuration(200L)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .start()
        }
    }

    override fun onDragStopped() {
        translationZ = dpToPx(0f)
        if (!isFaceDown && card?.statusEffect != null) {
            animate().rotation(getStatusEffectRotation())
                    .setDuration(200L)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .start()
        }
    }

    private fun measureChildAttachment(
            child: View,
            parentWidthMeasureSpec: Int,
            parentHeightMeasureSpec: Int
    ) {
        (child.layoutParams as? LayoutParams)?.let { lp ->
            val widthSpec = when(lp.width) {
                LayoutParams.RATIO -> MeasureSpec.makeMeasureSpec(((lp.widthRatio ?: 0f) * image.measuredWidth).roundToInt(), MeasureSpec.EXACTLY)
                else -> getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft + paddingRight, lp.width)
            }

            val heightSpec = when(lp.height) {
                LayoutParams.RATIO -> MeasureSpec.makeMeasureSpec(((lp.heightRatio ?: 0f) * image.measuredHeight).roundToInt(), MeasureSpec.EXACTLY)
                LayoutParams.SQUARE -> widthSpec
                else -> getChildMeasureSpec(parentHeightMeasureSpec, paddingTop + paddingBottom, lp.height)
            }

            child.measure(widthSpec, heightSpec)
        } ?: measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
    }


    private fun balanceAttachmentViews() {
        if (card != null) {
            val c = card!!
            // Load pokemon on top of stack's image into the image view
            if (!isFaceDown) {
                GlideApp.with(this)
                        .load(c.pokemons.peek()?.imageUrl)
                        .placeholder(R.drawable.pokemon_card_back)
                        .into(image)
            } else {
                image.setImageResource(R.drawable.pokemon_card_back)
            }

            if (!isFaceDown) {

                // Balance tools
                trimViews(c.tools.size, tools)
                c.tools.forEachIndexed { index, pokemonCard ->
                    if (index < tools.size) {
                        val oldImage = tools[index]
                        applyToolCard(pokemonCard, oldImage)
                    } else {
                        applyToolCard(pokemonCard)
                    }
                }

                // Balance energy cards
                trimViews(c.energy.size, energies)
                c.energy.forEachIndexed { index, pokemonCard ->
                    if (index < energies.size) {
                        val oldImage = energies[index]
                        applyEnergyCard(pokemonCard, oldImage)
                    } else {
                        applyEnergyCard(pokemonCard)
                    }
                }

                // Damage
                damage.setVisible(c.damage > 0)
                damage.text = "${c.damage}"

                // Status
                status.setVisible(c.isBurned || c.isPoisoned)
                (status.background as LayerDrawable).apply {
                    (getDrawable(0) as ShapeDrawable).paint.apply {
                        color = if (c.isPoisoned) color(R.color.green_400) else color(R.color.red_400)
                    }
                    (getDrawable(1) as ShapeDrawable).paint.apply {
                        color = if (c.isPoisoned) color(R.color.green_300) else color(R.color.red_300)
                    }
                }

                // TODO: Replace this with a configurable status marker system that users can
                // TODO: change and configure
                status.setImageResource(when {
                    c.isPoisoned -> R.drawable.ic_poison_small
                    else -> R.drawable.ic_burn_small
                })

                // TODO: Potentially add some other visual effects to this state
                rotation = getStatusEffectRotation()

            } else {
                damage.gone()
                rotation = 0f
            }
        } else {
            image.setImageResource(R.drawable.pokemon_card_back)
            damage.gone()
            rotation = 0f
        }
    }


    private fun <T : BezelImageView> trimViews(dataCount: Int, views: MutableList<T>) {
        val difference = views.size - dataCount
        if (difference > 0) {
            val viewsToRemove = views.subList(dataCount, views.size)
            views.removeAll(viewsToRemove)
            viewsToRemove.forEach {
                removeView(it)
                pool.release(it)
            }
        }
    }


    private fun applyToolCard(tool: PokemonCard, imageView: BezelImageView? = null) {
        val view = imageView ?: acquireImageView(tools)
        view.maskDrawable = toolMaskDrawable
        view.borderDrawable = toolStrokeDrawable
        view.scaleType = ImageView.ScaleType.CENTER_CROP

        // Apply tool card specifics to this image view, then add it to the view
        // Matrix clip the card image into
        GlideApp.with(view)
                .load(tool.imageUrl)
                .transform(ToolCropTransformation())
                .into(view)

        addView(view, generateToolLayoutParams())
    }


    private fun applyEnergyCard(energy: PokemonCard, imageView: BezelImageView? = null) {
        val view = imageView ?: acquireImageView(energies)
        view.maskDrawable = null
        view.borderDrawable = energyStrokeDrawable
        view.scaleType = ImageView.ScaleType.FIT_CENTER

        // Apply energy card specs to this image view and add to the parent
        when(energy.subtype) {
            SubType.BASIC -> {
                val type = energy.types?.firstOrNull() ?: Type.COLORLESS
                view.setImageResource(type.drawable())
            }
            else -> {
                // Matrix clip card image....somehow
                GlideApp.with(view)
                        .load(energy.imageUrl)
                        .transform(EnergyCropTransformation())
                        .into(view)
            }
        }

        addView(view, generateEnergyLayoutParams())
    }


    private fun acquireImageView(views: MutableList<BezelImageView>): BezelImageView {
        val view = pool.acquire() ?: BezelImageView(context)
        views.add(view)
        return view
    }


    private fun generateToolLayoutParams(): LayoutParams {
        return LayoutParams(TOOL_WIDTH_RATIO, TOOL_HEIGHT_RATIO)
    }


    private fun generateEnergyLayoutParams(): LayoutParams {
        return LayoutParams(ENERGY_SIZE_RATIO)
    }

    private fun getStatusEffectRotation(): Float = when(card?.statusEffect) {
        Board.Card.Status.CONFUSED -> 180f
        Board.Card.Status.PARALYZED -> 90f
        Board.Card.Status.SLEEPING ->  -90f
        else -> 0f
    }

    companion object {
        const val TOOL_WIDTH_RATIO = 0.2302158273f
        const val TOOL_HEIGHT_RATIO = 0.1053984576f
        const val TOOL_OFFSET_Y_RATIO = 0.265060241f
        const val ENERGY_SIZE_RATIO = 0.1445783133f //0.1151079137f
    }
}