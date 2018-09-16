package com.r0adkll.deckbuilder.arch.ui

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class ShortcutActivity : Activity() {

    @Inject lateinit var deckRepository: DeckRepository
    @Inject lateinit var editor: EditRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeckApp.component.inject(this)

        val action = intent?.action
        when(action) {
            ACTION_NEW_DECK -> {
                Shortcuts.reportUsage(this, Shortcuts.CREATE_DECK_ID)
                editor.createSession()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ sessionId ->
                            TaskStackBuilder.create(this)
                                    .addParentStack(DeckBuilderActivity::class.java)
                                    .addNextIntent(DeckBuilderActivity.createIntent(this, sessionId, true))
                                    .startActivities()
                            finish()
                        }, {
                            startActivity(HomeActivity.createIntent(this))
                            finish()
                        })
            }
            ACTION_OPEN_DECK -> {
                val deckId = intent?.getStringExtra(EXTRA_DECK_ID)
                if (deckId != null) {
                    Shortcuts.reportUsage(this, deckId)
                    deckRepository.getDeck(deckId)
                            .flatMap { editor.createSession(it) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ sessionId ->
                                TaskStackBuilder.create(this)
                                        .addParentStack(DeckBuilderActivity::class.java)
                                        .addNextIntent(DeckBuilderActivity.createIntent(this, sessionId))
                                        .startActivities()
                                finish()
                            }, {
                                startActivity(HomeActivity.createIntent(this))
                                finish()
                            })
                } else {
                    finish()
                }
            }
            else -> {
                startActivity(HomeActivity.createIntent(this))
                finish()
            }
        }
    }


    companion object {
        private const val ACTION_NEW_DECK = "com.r0adkll.deckbuilder.intent.ACTION_NEW_DECK"
        private const val ACTION_OPEN_DECK = "com.r0adkll.deckbuilder.intent.ACTION_OPEN_DECK"
        private const val EXTRA_DECK_ID = "com.r0adkll.deckbuilder.intent.EXTRA_DECK_ID"


        fun createIntent(context: Context): Intent {
            return Intent(context, ShortcutActivity::class.java)
        }


        fun createNewDeckIntent(context: Context): Intent {
            val intent = createIntent(context)
            intent.action = ACTION_NEW_DECK
            return intent
        }


        fun createOpenDeckIntent(context: Context, deckId: String): Intent {
            val intent = createIntent(context)
            intent.action = ACTION_OPEN_DECK
            intent.putExtra(EXTRA_DECK_ID, deckId)
            return intent
        }
    }
}