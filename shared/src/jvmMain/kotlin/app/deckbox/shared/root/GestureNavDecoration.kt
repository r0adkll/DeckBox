// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.deckbox.shared.root

import com.slack.circuit.backstack.NavDecoration
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.runtime.Navigator

internal actual class GestureNavDecoration actual constructor(
  navigator: Navigator,
) : NavDecoration by NavigatorDefaults.DefaultDecoration
