package com.r0adkll.deckbuilder.arch.data.features.tournament.exporter


import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.tournament.exporter.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.PlayerInfo
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.extensions.drawableStart
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import javax.inject.Inject


class DefaultTournamentExporter @Inject constructor(
) : TournamentExporter {

    override fun export(activityContext: Context, deck: Deck, playerInfo: PlayerInfo): Observable<File> {
        return Observable.create { s ->

            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, MARGIN.toFloat(), activityContext.resources.displayMetrics).toInt()
            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, WIDTH.toFloat() * 0.75f, activityContext.resources.displayMetrics).toInt()
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, HEIGHT.toFloat() * 0.75f, activityContext.resources.displayMetrics).toInt()
            val content = Rect(margin, margin, width - margin, height - margin)

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(width, height, 0)
                    .setContentRect(content)
                    .create()
            val page = document.startPage(pageInfo)

            // Print the deck to the page
            printDeck(deck, playerInfo, page.canvas, activityContext)

            // Finish the page
            document.finishPage(page)

            try {
                val outputDir = File(activityContext.cacheDir, "exports")
                outputDir.mkdir()
                val outputFile = File.createTempFile("${URLEncoder.encode(deck.name, "UTF-8")}-decklist-", ".pdf", outputDir)
                val fos = FileOutputStream(outputFile)

                document.writeTo(fos)
                fos.flush()
                fos.close()
                document.close()

                s.onNext(outputFile)
                s.onComplete()
            } catch(e: IOException) {
                s.onError(e)
            }
        }
    }


    fun printDeck(deck: Deck, playerInfo: PlayerInfo, canvas: Canvas, context: Context) {
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

        val stacked = CardUtils.stackCards().invoke(deck.cards)
        stacked.forEach { card ->
            val row = inflater.inflate(R.layout.layout_tournament_card_row, view, false)
            val quantity = row.findViewById<TextView>(R.id.quantity)
            val name = row.findViewById<TextView>(R.id.name)
            val setCode = row.findViewById<TextView>(R.id.setCode)
            val setNumber = row.findViewById<TextView>(R.id.setNumber)

            quantity.text = card.count.toString()
            name.text = card.card.name
            setCode.text = card.card.expansion?.ptcgoCode
            setNumber.text = card.card.number

            when(card.card.supertype) {
                SuperType.POKEMON -> tablePokemon.addView(row)
                SuperType.TRAINER -> tableTrainer.addView(row)
                SuperType.ENERGY -> tableEnergy.addView(row)
            }
        }

        val width = canvas.width
        val height = canvas.height
        val measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        Timber.i("Preparing Export PT(width: $width, height: $height)")

        view.measure(measureWidth, measuredHeight)
        view.layout(0, 0, width, height)
        view.draw(canvas)
    }


    companion object {

        const val WIDTH = 612 // 8"
        const val HEIGHT = 792 // 11"
        const val MARGIN = 32 // 1/2"
    }
}