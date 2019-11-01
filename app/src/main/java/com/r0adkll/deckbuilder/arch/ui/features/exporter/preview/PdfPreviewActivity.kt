package com.r0adkll.deckbuilder.arch.ui.features.exporter.preview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.util.bindSerializable
import com.jakewharton.rxbinding2.view.clicks
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.PdfDocumentAdapter
import kotlinx.android.synthetic.main.activity_pdf_preview.*
import java.io.File
import java.io.IOException

class PdfPreviewActivity : BaseActivity() {

    private val file by bindSerializable<File>(EXTRA_FILE)
    private val printManager: PrintManager by lazy {
        getSystemService(Context.PRINT_SERVICE) as PrintManager
    }

    private var renderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var currentPage: PdfRenderer.Page? = null

    @SuppressLint("RxSubscribeOnError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_preview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = file?.name
        appbar?.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        disposables += actionPrint.clicks()
            .subscribe { printFile() }

        try {
            openRenderer()
            showPage(0)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_export_tournament, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                Analytics.event(Event.SelectContent.Action("share_tournament_decklist"))
                val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file!!)
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.type = "application/pdf"
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(intent, "Share DeckList..."))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        file?.deleteOnExit()
    }

    override fun setupComponent() {
    }

    private fun printFile() {
        Analytics.event(Event.SelectContent.Action("print_decklist"))

        val jobName = "DeckBox Deck list"
        val attrs = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
            .build()

        printManager.print(jobName, PdfDocumentAdapter(this, file!!), attrs)
    }

    /**
     * Sets up a [android.graphics.pdf.PdfRenderer] and related resources.
     */
    @Throws(IOException::class)
    private fun openRenderer() {
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        fileDescriptor?.let {
            renderer = PdfRenderer(it)
        }
    }

    /**
     * Closes the [android.graphics.pdf.PdfRenderer] and related resources.
     *
     * @throws java.io.IOException When the PDF file cannot be closed.
     */
    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage?.close()
        renderer?.close()
        fileDescriptor?.close()
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private fun showPage(index: Int) {
        if (renderer?.pageCount ?: 0 <= index) {
            return
        }

        // Make sure to close the current page before opening another one.
        currentPage?.close()

        // Use `openPage` to open a specific page in PDF.
        currentPage = renderer?.openPage(index)

        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage?.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        // We are ready to show the Bitmap to user.
        image.setImageBitmap(bitmap)
    }

    companion object {
        const val EXTRA_FILE = "PdfPreviewActivity.File"

        fun createIntent(context: Context, file: File): Intent {
            val intent = Intent(context, PdfPreviewActivity::class.java)
            intent.putExtra(EXTRA_FILE, file)
            return intent
        }
    }
}
