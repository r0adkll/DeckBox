package com.r0adkll.deckbuilder.arch.data.features.exporter.tournament

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.ftinc.kit.extensions.pt
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.SubTypes
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.PlayerInfo
import com.r0adkll.deckbuilder.util.AgeDivisionUtils
import com.r0adkll.deckbuilder.util.stack
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import javax.inject.Inject

class DefaultTournamentExporter @Inject constructor(
    val deckRepository: DeckRepository,
    val editRepository: EditRepository
) : TournamentExporter {

    private class ViewHolder(val parent: ViewGroup) {

        val formatStandard: ImageView = parent.findViewById(R.id.formatStandard)
        val formatExpanded: ImageView = parent.findViewById(R.id.formatExpanded)
        val playerName: EditText = parent.findViewById(R.id.playerName)
        val playerId: EditText = parent.findViewById(R.id.playerID)
        val dateOfBirth: EditText = parent.findViewById(R.id.dateOfBirth)
        val ageJunior: ImageView = parent.findViewById(R.id.optionAgeDivisionJunior)
        val ageSenior: ImageView = parent.findViewById(R.id.optionAgeDivisionSenior)
        val ageMaster: ImageView = parent.findViewById(R.id.optionAgeDivisionMasters)
        val ageJuniorLabel: TextView = parent.findViewById(R.id.optionAgeDivisionJuniorLabel)
        val ageSeniorLabel: TextView = parent.findViewById(R.id.optionAgeDivisionSeniorLabel)
        val ageMasterLabel: TextView = parent.findViewById(R.id.optionAgeDivisionMastersLabel)
        val extraColumn: LinearLayout = parent.findViewById(R.id.extraColumn)
        val tablePokemon: TableLayout = parent.findViewById(R.id.tablePokemon)
        val tablePokemon2: TableLayout = parent.findViewById(R.id.tablePokemon2)
        val tablePokemon2Title: TextView = parent.findViewById(R.id.tablePokemon2Title)
        val tableTrainer: TableLayout = parent.findViewById(R.id.tableTrainer)
        val tableEnergy: TableLayout = parent.findViewById(R.id.tableEnergy)

        fun setAgeDivision(ageDivision: AgeDivision) {
            when (ageDivision) {
                AgeDivision.JUNIOR -> ageJunior
                    .setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
                AgeDivision.SENIOR -> ageSenior
                    .setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
                AgeDivision.MASTERS -> ageMaster
                    .setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
            }
        }

        fun setFormat(format: Format) {
            when (format) {
                Format.STANDARD -> formatStandard.setImageResource(R.drawable.ic_checkbox_marked_outline_black_24dp)
                Format.EXPANDED -> formatExpanded.setImageResource(R.drawable.ic_checkbox_marked_outline_black_24dp)
            }
        }
    }

    override fun export(activityContext: Context, deckId: String, playerInfo: PlayerInfo): Observable<File> {
        return editRepository.observeSession(deckId)
            .take(1)
            .map { createDocument(activityContext, it.cards, it.name, playerInfo) }
    }

    private fun createDocument(context: Context, cards: List<PokemonCard>, name: String, playerInfo: PlayerInfo): File {
        val margin = context.pt(MARGIN.toFloat()).toInt()
        val width = context.pt(WIDTH.toFloat() * DOCUMENT_SIZE_SCALE).toInt()
        val height = context.pt(HEIGHT.toFloat() * DOCUMENT_SIZE_SCALE).toInt()
        val content = Rect(margin, margin, width - margin, height - margin)

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 0)
            .setContentRect(content)
            .create()
        val page = document.startPage(pageInfo)

        // Print the deck to the page
        printDeck(cards, playerInfo, page.canvas, context)

        // Finish the page
        document.finishPage(page)

        val outputDir = File(context.cacheDir, "exports")
        outputDir.mkdir()
        val outputFile = File.createTempFile("${URLEncoder.encode(name, "UTF-8")}-decklist-", ".pdf", outputDir)
        val fos = FileOutputStream(outputFile)

        document.writeTo(fos)
        fos.flush()
        fos.close()
        document.close()

        // return
        return outputFile
    }

    @SuppressLint("InflateParams")
    private fun printDeck(cards: List<PokemonCard>, playerInfo: PlayerInfo, canvas: Canvas, context: Context) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_tournament_pdf, null, false) as LinearLayout
        val viewHolder = ViewHolder(view)

        viewHolder.ageJuniorLabel.text = AgeDivisionUtils.divisionLabel(context, AgeDivision.JUNIOR)
        viewHolder.ageSeniorLabel.text = AgeDivisionUtils.divisionLabel(context, AgeDivision.SENIOR)
        viewHolder.ageMasterLabel.text = AgeDivisionUtils.divisionLabel(context, AgeDivision.MASTERS)

        viewHolder.setFormat(playerInfo.format)
        viewHolder.setAgeDivision(playerInfo.ageDivision)
        viewHolder.playerName.setText(playerInfo.name)
        viewHolder.playerId.setText(playerInfo.id)
        viewHolder.dateOfBirth.setText(playerInfo.displayDate())

        val stacked = cards.stack()
        val stackedGroups = stacked.groupBy { it.card.supertype }

        addPokemon(inflater, stackedGroups[SuperType.POKEMON], viewHolder, stacked.size)
        addSuperType(inflater, stackedGroups[SuperType.TRAINER], view, viewHolder.tableTrainer)
        addSuperType(inflater, stackedGroups[SuperType.ENERGY], view, viewHolder.tableEnergy)

        val width = canvas.width
        val height = canvas.height
        val measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        Timber.i("Preparing Export PT(width: $width, height: $height)")

        view.measure(measureWidth, measuredHeight)
        view.layout(0, 0, width, height)
        view.draw(canvas)
    }

    private fun addPokemon(
        inflater: LayoutInflater,
        pokemon: List<StackedPokemonCard>?,
        viewHolder: ViewHolder,
        stackSize: Int
    ) {
        pokemon
            ?.sortedByDescending { it.count }
            ?.let {
                if (stackSize >= MAX_COLUMN_SIZE) {
                    addSplitPokemon(inflater, pokemon, viewHolder)
                } else {
                    viewHolder.tablePokemon2Title.setText(R.string.tournament_pdf_table_pokemon)
                    it.map { createRow(inflater, viewHolder.parent, it) }
                        .forEach { viewHolder.tablePokemon2.addView(it) }
                }
            }
    }

    private fun addSplitPokemon(
        inflater: LayoutInflater,
        pokemon: List<StackedPokemonCard>,
        viewHolder: ViewHolder
    ) {
        viewHolder.extraColumn.isVisible = true

        if (pokemon.size >= MAX_POKEMON_COLUMN_SIZE) {
            val pokemon1 = pokemon.subList(0, MAX_POKEMON_COLUMN_SIZE)
            val pokemon2 = pokemon.subList(MAX_POKEMON_COLUMN_SIZE, pokemon.size)

            pokemon1.map { createRow(inflater, viewHolder.parent, it) }
                .forEach { viewHolder.tablePokemon.addView(it) }

            pokemon2.map { createRow(inflater, viewHolder.parent, it) }
                .forEach { viewHolder.tablePokemon2.addView(it) }
        } else {
            viewHolder.tablePokemon2.isGone = true
            pokemon.map { createRow(inflater, viewHolder.parent, it) }
                .forEach { viewHolder.tablePokemon.addView(it) }
        }
    }

    private fun addSuperType(
        inflater: LayoutInflater,
        stackedCards: List<StackedPokemonCard>?,
        parent: ViewGroup,
        table: TableLayout
    ) {
        stackedCards?.reduce()
            ?.sortedByDescending { it.count }
            ?.map { createRow(inflater, parent, it) }
            ?.forEach { table.addView(it) }
    }

    private fun createRow(inflater: LayoutInflater, parent: ViewGroup, card: StackedPokemonCard): View {
        val row = inflater.inflate(R.layout.layout_tournament_card_row, parent, false)
        val quantity = row.findViewById<TextView>(R.id.quantity)
        val name = row.findViewById<TextView>(R.id.name)
        val setCode = row.findViewById<TextView>(R.id.setCode)
        val setNumber = row.findViewById<TextView>(R.id.setNumber)

        quantity.text = card.count.toString()
        name.text = card.card.name
        setCode.text = card.card.expansion?.ptcgoCode
        setNumber.text = card.card.number

        setCode.isVisible = card.card.supertype == SuperType.POKEMON
        setNumber.isVisible = card.card.supertype == SuperType.POKEMON

        return row
    }

    private fun List<StackedPokemonCard>.reduce(): List<StackedPokemonCard> {
        val reducedStackedTrainers = HashMap<String, StackedPokemonCard>()

        this.forEach {
            val name = if (it.card.supertype == SuperType.ENERGY && it.card.subtype == SubTypes.BASIC) {
                // Make sure to trim the term "Basic" out of energy cards
                it.card.name.replace("Basic", "").trim()
            } else {
                it.card.name
            }

            val stack = reducedStackedTrainers[name]
            if (stack == null) {
                reducedStackedTrainers[name] = it
            } else {
                val newCount = stack.count + it.count
                reducedStackedTrainers[name] = stack.copy(count = newCount)
            }
        }

        return reducedStackedTrainers.values.toList()
    }

    companion object {
        private const val MAX_COLUMN_SIZE = 31
        private const val MAX_POKEMON_COLUMN_SIZE = 34
        private const val DOCUMENT_SIZE_SCALE = 0.75f
        private const val WIDTH = 612 // 8"
        private const val HEIGHT = 792 // 11"
        private const val MARGIN = 32 // 1/2"
    }
}
