package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Shader
import android.graphics.drawable.*
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.DECK
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.PREVIEW
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.bindView
import com.r0adkll.deckbuilder.util.extensions.toBitmap
import com.r0adkll.deckbuilder.util.svg.SvgSoftwareLayerSetter
import com.r0adkll.deckbuilder.util.svg.SvgViewTarget
import timber.log.Timber


sealed class UiViewHolder<in I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)


    class PreviewViewHolder(
            itemView: View,
            private val dismissPreview: Relay<Unit>,
            private val viewPreview: Relay<ExpansionPreview>
    ) : UiViewHolder<Item.Preview>(itemView) {

        private val background: LinearLayout by bindView(R.id.background)
        private val foreground: ImageView by bindView(R.id.foreground)
        private val logo: ImageView by bindView(R.id.logo)
        private val title: TextView by bindView(R.id.title)
        private val description: TextView by bindView(R.id.description)
        private val actionDismiss: Button by bindView(R.id.actionDismiss)
        private val actionView: Button by bindView(R.id.actionView)


        override fun bind(item: Item.Preview) {
            val spec = item.spec.preview

            // Load logo
            GlideApp.with(itemView)
                    .load(spec.logoUrl)
                    .into(logo)

            // Configure Background
            background.background = createDrawable(spec.background)

            // Configure Foreground
            spec.foreground?.let {
                applyDrawable(it, foreground)
            }

            // Set Title & Description
            title.text = spec.title
            description.text = spec.description

            // Apply text color
            Color.parseColor(spec.textColor).apply {
                title.setTextColor(this)
                description.setTextColor(this)
                actionDismiss.setTextColor(this)
                actionView.setTextColor(this)
            }

            // Set action listeners
            actionDismiss.setOnClickListener { dismissPreview.accept(Unit) }
            actionView.setOnClickListener { viewPreview.accept(item.spec) }
        }


        private fun createDrawable(specs: List<ExpansionPreview.PreviewSpec.DrawableSpec>): Drawable {
            val drawables = ArrayList<Drawable>(specs.size)
            specs.forEach {
                val drawable = createDrawable(it)
                if (drawable != null) {
                    drawables += drawable
                }
            }
            return LayerDrawable(drawables.toTypedArray())
        }


        private fun createDrawable(spec: ExpansionPreview.PreviewSpec.DrawableSpec): Drawable? {
            return when(spec.type) {
                "color" -> {
                    val color = Color.parseColor(spec.data)
                    ColorDrawable(color)
                }
                "tile" -> {
                    val bitmap = spec.data.toBitmap()
                    if (bitmap != null) {
                        BitmapDrawable(itemView.resources, bitmap).apply {
                            setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                            setTargetDensity(itemView.resources.displayMetrics.densityDpi * 4)
                        }
                    } else {
                        null
                    }
                }
                "base64" -> {
                    val bitmap = spec.data.toBitmap()
                    if (bitmap != null) {
                        Timber.d("Base64 > Bitmap (w: ${bitmap.width}, h: ${bitmap.height})")
                        BitmapDrawable(itemView.resources, bitmap)
                    } else {
                        null
                    }
                }
                else -> null
            }
        }

        private fun applyDrawable(spec: ExpansionPreview.PreviewSpec.DrawableSpec, imageView: ImageView) {
            when(spec.type) {
                "url" -> {
                    GlideApp.with(imageView)
                            .`as`(SVG::class.java)
                            .load(spec.data)
                            .listener(SvgSoftwareLayerSetter())
                            .into(SvgViewTarget(imageView))
                }
                "svg" -> {
                    imageView.post {
                        try {
                            val svg = SVG.getFromString(spec.data)
                            val ratio = svg.documentViewBox.height() / svg.documentViewBox.width()
                            svg.documentWidth = imageView.width.toFloat()
                            svg.documentHeight = imageView.width.toFloat() * ratio
                            imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
                            imageView.setImageDrawable(PictureDrawable(svg.renderToPicture()))
                        } catch (e: SVGParseException) {
                            Timber.e(e, "Error parsing SVG data, please check remote config")
                        }
                    }
                }
                else -> imageView.setImageDrawable(createDrawable(spec))
            }
        }
    }


    class DeckViewHolder(
            itemView: View,
            private val shareClicks: Relay<Deck>,
            private val duplicateClicks: Relay<Deck>,
            private val testClicks: Relay<Deck>,
            private val deleteClicks: Relay<Deck>
    ) : UiViewHolder<Item.DeckItem>(itemView) {

        private val image: DeckImageView by bindView(R.id.image)
        private val title: TextView by bindView(R.id.title)
        private val actionShare: ImageView by bindView(R.id.action_share)
        private val actionMore: ImageView by bindView(R.id.action_more)
        private val actionTest: ImageView by bindView(R.id.action_test)


        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: Item.DeckItem) {
            val deck = item.deck
            title.text = deck.name

            deck.image?.let {
                when(it) {
                    is DeckImage.Pokemon -> {
                        GlideApp.with(itemView)
                                .load(it.imageUrl)
                                .placeholder(R.drawable.pokemon_card_back)
                                .into(image)
                    }
                    is DeckImage.Type -> {
                        image.primaryType = it.type1
                        image.secondaryType = it.type2
                    }
                }
            } ?: mostProminentCard(deck.cards)?.let {
                GlideApp.with(itemView)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.pokemon_card_back)
                        .into(image)
            }

            val popupMenu = PopupMenu(itemView.context, actionMore)
            popupMenu.inflate(R.menu.deck_actions)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.action_duplicate -> { duplicateClicks.accept(deck); true }
                    R.id.action_delete -> { deleteClicks.accept(deck); true }
                    else -> false
                }
            }

            actionMore.setOnTouchListener(popupMenu.dragToOpenListener)
            actionMore.setOnClickListener {
                popupMenu.show()
            }

            actionShare.setOnClickListener { shareClicks.accept(deck) }
            actionTest.setOnClickListener { testClicks.accept(deck) }
        }


        private fun mostProminentCard(cards: List<PokemonCard>): PokemonCard? {
            val stacks = CardUtils.stackCards().invoke(cards)
            val evolutions = EvolutionChain.build(stacks)
            val largestEvolutionLine = evolutions.maxBy { it.size }
            return largestEvolutionLine?.last()?.cards?.firstOrNull()?.card
        }
    }


    private enum class ViewType(@LayoutRes val layoutId: Int) {
        PREVIEW(R.layout.item_set_preview),
        DECK(R.layout.item_deck);

        companion object {
            val VALUES by lazy { values() }

            fun of(layoutId: Int): ViewType {
                val match = VALUES.firstOrNull { it.layoutId == layoutId }
                match?.let { return match }

                throw EnumConstantNotPresentException(ViewType::class.java, "could not find view type for $layoutId")
            }
        }
    }


    companion object {

        @Suppress("UNCHECKED_CAST")
        fun create(itemView: View,
                   layoutId: Int,
                   shareClicks: Relay<Deck>,
                   duplicateClicks: Relay<Deck>,
                   testClicks: Relay<Deck>,
                   deleteClicks: Relay<Deck>,
                   dismissPreview: Relay<Unit>,
                   viewPreview: Relay<ExpansionPreview>): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                PREVIEW -> PreviewViewHolder(itemView, dismissPreview, viewPreview) as UiViewHolder<Item>
                DECK -> DeckViewHolder(itemView, shareClicks, duplicateClicks, testClicks, deleteClicks) as UiViewHolder<Item>
            }
        }
    }
}