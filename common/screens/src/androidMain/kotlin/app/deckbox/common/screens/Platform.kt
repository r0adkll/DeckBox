// Copyright 2022, Drew Heavner and the Deckbox project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.common.screens

import android.os.Parcelable

/**
 * Copied from https://github.com/realityexpander/NoteAppKMM
 */

actual typealias CommonParcelize = kotlinx.parcelize.Parcelize
actual typealias CommonParceler<T> = kotlinx.parcelize.Parceler<T>
actual typealias CommonTypeParceler<T, P> = kotlinx.parcelize.TypeParceler<T, P>
actual typealias CommonIgnoredOnParcel = kotlinx.parcelize.IgnoredOnParcel
actual interface CommonParcelable : Parcelable
