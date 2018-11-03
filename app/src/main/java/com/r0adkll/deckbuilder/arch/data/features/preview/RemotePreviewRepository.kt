package com.r0adkll.deckbuilder.arch.data.features.preview

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.data.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.preview.PreviewRepository
import com.r0adkll.deckbuilder.util.extensions.iso8601
import io.reactivex.Observable
import java.lang.NullPointerException
import javax.inject.Inject


class RemotePreviewRepository @Inject constructor(
        val remote: Remote,
        val preferences: AppPreferences
) : PreviewRepository {

    override fun getExpansionPreview(): Observable<ExpansionPreview> {
        return preferences.previewVersion
                .asObservable()
                .map { version ->
                    val preview = remote.expansionPreview
                    if (preview != null && // If preview exists
                            preview.version > version && // If we haven't dismissed this version
                            preview.expiresAt.iso8601() > System.currentTimeMillis()) { // If the preview hasn't expired
                        preview
                    } else {
                        // Throw an exception if preview is null or stale. The UI should handle this case
                        throw NullPointerException()
                    }
                }
    }

    override fun dismissPreview() {
        remote.expansionPreview?.let {
            preferences.previewVersion.set(it.version)
        }
    }
}