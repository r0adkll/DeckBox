package com.r0adkll.deckbuilder.arch.data.features.offline.repository

import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus

interface OfflineStatusConsumer {

    var status: OfflineStatus
}
