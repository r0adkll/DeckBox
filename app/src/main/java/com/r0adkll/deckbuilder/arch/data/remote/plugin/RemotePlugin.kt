package com.r0adkll.deckbuilder.arch.data.remote.plugin

import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote


/**
 * Plugin to take action when the remote has fetched an activated updated remote config
 * values.
 */
interface RemotePlugin {

    /**
     * Remote has fetched new values and activated them.
     */
    fun onFetchActivated(remote: Remote)
}