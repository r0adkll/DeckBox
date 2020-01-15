package com.r0adkll.deckbuilder.arch.data.features.editing.repository

import com.f2prateek.rx.preferences2.Preference
import com.nhaarman.mockitokotlin2.eq
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.editing.source.EditSource
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.tools.ModelUtils
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.amshove.kluent.Verify
import org.amshove.kluent.When
import org.amshove.kluent.any
import org.amshove.kluent.called
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.amshove.kluent.on
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class DefaultEditRepositoryTest {

    lateinit var localSource: EditSource
    lateinit var remoteSource: EditSource
    lateinit var preferences: AppPreferences
    lateinit var deckRepository: DeckRepository
    lateinit var repository: DefaultEditRepository

    @Before
    fun setUp() {
        preferences = mock()
        localSource = mock()
        remoteSource = mock()
        deckRepository = mock()
        repository = DefaultEditRepository(localSource, remoteSource, preferences, deckRepository)
    }

    @Test
    fun `test creating new session`() {
        val sessionId = repository.createNewSession()
        UUID.fromString(sessionId)
        repository.sessions.containsKey(sessionId).shouldBeTrue()
        repository.sessions[sessionId].shouldBeNull()
    }

    @Test
    fun `test observe session with valid deckId`() {
        val someDeckId = "some_valid_id"
        val deck = Deck("", "", "", null, false, emptyList(), false, 0L)
        val testObserver = TestObserver<Deck>()
        When calling deckRepository.observeDeck(someDeckId) itReturns Observable.just(deck)

        repository.observeSession(someDeckId)
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertValue(deck)
    }

    @Test
    fun `test observe session with mapped deckId`() {
        val editId = "some_edit_id"
        val mappedDeckId = "mapped_deck_id"
        val deck = Deck(mappedDeckId, "", "", null, false, emptyList(), false, 0L)
        val testObserver = TestObserver<Deck>()
        repository.sessions[editId] = mappedDeckId
        When calling deckRepository.observeDeck(mappedDeckId) itReturns Observable.just(deck)

        repository.observeSession(editId)
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertValue(deck)
        testObserver.values().first().id.shouldEqual(mappedDeckId)
    }

    @Test
    fun `test observe session updates with new mapped deckId while observing`() {
        val editId = repository.createNewSession()
        val mappedDeckId = "mapped_deck_id"
        val deck = Deck(mappedDeckId, "", "", null, false, emptyList(), false, 0L)
        val testObserver = TestObserver<Deck>()
        val testScheduler = TestScheduler()
        mockPreferencesForLocal()
        When calling localSource.startSession(null) itReturns Observable.just(deck)
        When calling localSource.addCards(any(), any()) itReturns Observable.just(Unit)
        When calling deckRepository.observeDeck(mappedDeckId) itReturns Observable.just(deck)

        repository.observeSession(editId)
            .observeOn(testScheduler)
            .subscribe(testObserver)

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertEmpty()

        repository.submit(editId, Edit.AddCards())
            .subscribeOn(Schedulers.trampoline())
            .subscribe()

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue(deck)
        repository.sessions[editId].shouldEqual(mappedDeckId)
    }

    @Test
    fun `test submitting edits locally`() {
        val deckId = "some_deck_id"
        mockPreferencesForLocal()

        var testObserver = TestObserver<String>()
        When calling localSource.changeName(any(), any()) itReturns Observable.just("")
        repository.submit(deckId, Edit.Name("test"))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.changeName(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling localSource.changeDescription(any(), any()) itReturns Observable.just("")
        repository.submit(deckId, Edit.Description("test"))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.changeDescription(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling localSource.changeDeckImage(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.Image(DeckImage.Pokemon("test")))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.changeDeckImage(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling localSource.changeCollectionOnly(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.CollectionOnly(true))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.changeCollectionOnly(eq(deckId), eq(true)) was called

        testObserver = TestObserver()
        When calling localSource.addCards(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.AddCards())
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.addCards(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling localSource.removeCard(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.RemoveCard(ModelUtils.createPokemonCard()))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on localSource that localSource.removeCard(eq(deckId), any()) was called
    }

    @Test
    fun `test submitting edits remotely`() {
        val deckId = "some_deck_id"
        mockPreferencesForRemote()

        var testObserver = TestObserver<String>()
        When calling remoteSource.changeName(any(), any()) itReturns Observable.just("")
        repository.submit(deckId, Edit.Name("test"))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.changeName(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling remoteSource.changeDescription(any(), any()) itReturns Observable.just("")
        repository.submit(deckId, Edit.Description("test"))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.changeDescription(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling remoteSource.changeDeckImage(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.Image(DeckImage.Pokemon("test")))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.changeDeckImage(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling remoteSource.changeCollectionOnly(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.CollectionOnly(true))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.changeCollectionOnly(eq(deckId), eq(true)) was called

        testObserver = TestObserver()
        When calling remoteSource.addCards(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.AddCards())
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.addCards(eq(deckId), any()) was called

        testObserver = TestObserver()
        When calling remoteSource.removeCard(any(), any()) itReturns Observable.just(Unit)
        repository.submit(deckId, Edit.RemoveCard(ModelUtils.createPokemonCard()))
            .subscribeOn(Schedulers.trampoline())
            .subscribe(testObserver)
        testObserver.assertValue(deckId)
        Verify on remoteSource that remoteSource.removeCard(eq(deckId), any()) was called
    }

    private fun mockPreferencesForLocal() {
        val offlineId = mock<Preference<String>>()
        When calling offlineId.isSet itReturns true
        When calling offlineId.get() itReturns "some_offline_id"
        When calling preferences.offlineId itReturns offlineId
    }

    private fun mockPreferencesForRemote() {
        val offlineId = mock<Preference<String>>()
        When calling offlineId.isSet itReturns false
        When calling preferences.offlineId itReturns offlineId
    }
}
