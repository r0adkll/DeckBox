package app.deckbox.features.boosterpacks.impl.ids

import app.deckbox.core.di.MergeAppScope
import com.benasher44.uuid.uuid4
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

interface BoosterPackIdGenerator {
  fun generate(): String
}

@ContributesBinding(MergeAppScope::class)
@Inject
class UuidBoosterPackIdGenerator : BoosterPackIdGenerator {

  override fun generate(): String {
    return uuid4().toString()
  }
}
