package com.r0adkll.deckbuilder.arch.data.features.tournament.exporter


import android.content.Context
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
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
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class DefaultTournamentExporter @Inject constructor(
) : TournamentExporter {

    override fun export(activityContext: Context, deck: Deck, playerInfo: PlayerInfo): Observable<File> {
        return Observable.create { s ->

            val document = PdfDocument()

            val pageInfo = PdfDocument.PageInfo.Builder(WIDTH, HEIGHT, 1)
                    .setContentRect(CONTENT)
                    .create()

            val page = document.startPage(pageInfo)

            val inflater = LayoutInflater.from(activityContext)
            val view = inflater.inflate(R.layout.layout_tournament_pdf, null, false) as LinearLayout

            val formatStandard = view.findViewById<CheckBox>(R.id.formatStandard)
            val formatExpanded = view.findViewById<CheckBox>(R.id.formatExpanded)
            val playerName = view.findViewById<EditText>(R.id.playerName)
            val playerId = view.findViewById<EditText>(R.id.playerID)
            val dateOfBirth = view.findViewById<EditText>(R.id.dateOfBirth)
            val ageDivisions = view.findViewById<RadioGroup>(R.id.optionsAgeDivision)
            val tablePokemon = view.findViewById<TableLayout>(R.id.tablePokemon)
            val tableTrainer= view.findViewById<TableLayout>(R.id.tableTrainer)
            val tableEnergy = view.findViewById<TableLayout>(R.id.tableEnergy)

            when(playerInfo.format) {
                Format.STANDARD -> formatStandard.isChecked = true
                Format.EXPANDED -> formatExpanded.isChecked = true
            }

            ageDivisions.check(when(playerInfo.ageDivision) {
                AgeDivision.JUNIOR -> R.id.optionAgeDivisionJunior
                AgeDivision.SENIOR -> R.id.optionAgeDivisionSenior
                AgeDivision.MASTERS -> R.id.optionAgeDivisionMasters
            })

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

            val measureWidth = View.MeasureSpec.makeMeasureSpec(page.canvas.width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(page.canvas.height, View.MeasureSpec.EXACTLY)

            view.measure(measureWidth, measuredHeight)
            view.layout(0, 0, page.canvas.width, page.canvas.height)

            view.draw(page.canvas)

            document.finishPage(page)

            try {
                val outputDir = File(activityContext.filesDir, "exports")
                outputDir.mkdir()
                val outputFile = File(outputDir, "${deck.name}_DeckList.pdf")
                if (outputFile.exists()) {
                    outputFile.delete()
                }

                outputFile.createNewFile()
                document.writeTo(FileOutputStream(outputFile))
                document.close()

                s.onNext(outputFile)
                s.onComplete()
            } catch(e: IOException) {
                s.onError(e)
            }
        }
    }


    companion object {
        const val WIDTH = 576 // 8"
        const val HEIGHT = 792 // 11"
        const val MARGIN = 32 // 1/2"
        const val ACTUAL_WIDTH = WIDTH - (2 * MARGIN)
        private val CONTENT = Rect(MARGIN, MARGIN, ACTUAL_WIDTH, HEIGHT - (MARGIN * 2))
    }
}