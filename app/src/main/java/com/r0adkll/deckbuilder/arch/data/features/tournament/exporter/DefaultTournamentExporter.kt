package com.r0adkll.deckbuilder.arch.data.features.tournament.exporter


import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ftinc.kit.kotlin.extensions.setVisible
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.ExportTask
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.tournament.exporter.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.PlayerInfo
import com.r0adkll.deckbuilder.util.CardUtils
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import javax.inject.Inject


class DefaultTournamentExporter @Inject constructor(
        val deckRepository: DeckRepository,
        val editRepository: EditRepository
) : TournamentExporter {

    override fun export(activityContext: Context, task: ExportTask, playerInfo: PlayerInfo): Observable<File> {
        return when{
            task.deckId != null -> deckRepository.getDeck(task.deckId)
                    .map { createDocument(activityContext, it.cards, it.name, playerInfo) }
            task.sessionId != null -> editRepository.getSession(task.sessionId)
                    .map { createDocument(activityContext, it.cards, it.name, playerInfo) }
            else -> Observable.error(IOException("Unable to export your deck"))
        }
    }


    private fun createDocument(context: Context, cards: List<PokemonCard>, name: String, playerInfo: PlayerInfo): File {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, MARGIN.toFloat(), context.resources.displayMetrics).toInt()
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, WIDTH.toFloat() * 0.75f, context.resources.displayMetrics).toInt()
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, HEIGHT.toFloat() * 0.75f, context.resources.displayMetrics).toInt()
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


    private fun printDeck(cards: List<PokemonCard>, playerInfo: PlayerInfo, canvas: Canvas, context: Context) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_tournament_pdf, null, false) as LinearLayout

        val formatStandard = view.findViewById<ImageView>(R.id.formatStandard)
        val formatExpanded = view.findViewById<ImageView>(R.id.formatExpanded)
        val playerName = view.findViewById<EditText>(R.id.playerName)
        val playerId = view.findViewById<EditText>(R.id.playerID)
        val dateOfBirth = view.findViewById<EditText>(R.id.dateOfBirth)
        val ageJunior = view.findViewById<ImageView>(R.id.optionAgeDivisionJunior)
        val ageSenior = view.findViewById<ImageView>(R.id.optionAgeDivisionSenior)
        val ageMaster = view.findViewById<ImageView>(R.id.optionAgeDivisionMasters)
        val tablePokemon = view.findViewById<TableLayout>(R.id.tablePokemon)
        val tableTrainer= view.findViewById<TableLayout>(R.id.tableTrainer)
        val tableEnergy = view.findViewById<TableLayout>(R.id.tableEnergy)

        when(playerInfo.format) {
            Format.STANDARD -> formatStandard.setImageResource(R.drawable.ic_checkbox_marked_outline_black_24dp)
            Format.EXPANDED -> formatExpanded.setImageResource(R.drawable.ic_checkbox_marked_outline_black_24dp)
        }

        when(playerInfo.ageDivision) {
            AgeDivision.JUNIOR -> ageJunior.setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
            AgeDivision.SENIOR -> ageSenior.setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
            AgeDivision.MASTERS -> ageMaster.setImageResource(R.drawable.ic_checkbox_marked_circle_outline_black_24dp)
        }

        playerName.setText(playerInfo.name)
        playerId.setText(playerInfo.id)
        dateOfBirth.setText(playerInfo.displayDate())

        val stacked = CardUtils.stackCards().invoke(cards)
        val stackedGroups = stacked.groupBy { it.card.supertype }

        stackedGroups[SuperType.POKEMON]
                ?.map { createRow(inflater, view, it) }
                ?.forEach { tablePokemon.addView(it) }

        stackedGroups[SuperType.TRAINER]?.reduce()
                ?.map { createRow(inflater, view, it) }
                ?.forEach { tableTrainer.addView(it) }

        stackedGroups[SuperType.ENERGY]?.reduce()
                ?.map { createRow(inflater, view, it) }
                ?.forEach { tableEnergy.addView(it) }

        val width = canvas.width
        val height = canvas.height
        val measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        Timber.i("Preparing Export PT(width: $width, height: $height)")

        view.measure(measureWidth, measuredHeight)
        view.layout(0, 0, width, height)
        view.draw(canvas)
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

        setCode.setVisible(card.card.supertype == SuperType.POKEMON)
        setNumber.setVisible(card.card.supertype == SuperType.POKEMON)

        return row
    }


    private fun List<StackedPokemonCard>.reduce(): List<StackedPokemonCard> {
        val reducedStackedTrainers = HashMap<String, StackedPokemonCard>()

        this.forEach {

            val name = if (it.card.supertype == SuperType.ENERGY && it.card.subtype == SubType.BASIC) {
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

        const val WIDTH = 612 // 8"
        const val HEIGHT = 792 // 11"
        const val MARGIN = 32 // 1/2"
    }
}