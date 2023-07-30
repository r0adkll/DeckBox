package app.deckbox.android.logging

import android.util.Log
import app.deckbox.core.logging.Extras
import app.deckbox.core.logging.Heartwood
import app.deckbox.core.logging.LogPriority

class AndroidBark : Heartwood.Bark {

  override fun log(priority: LogPriority, tag: String?, extras: Extras?, message: String) {
    var msg = message
    if (extras != null) {
      msg += "\nExtras[${extras.entries.joinToString { "${it.key}:${it.value}" }}]"
    }

    Log.println(priority.priority, tag, msg)
  }
}
