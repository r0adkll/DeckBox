package app.deckbox.common.resources.strings

import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Locale

actual object CurrencyFormatter {

  actual fun format(value: Double, type: CurrencyType): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      formatAndroid30(value, type)
    } else {
      formatLegacy(value, type)
    }
  }

  @RequiresApi(Build.VERSION_CODES.R)
  private fun formatAndroid30(value: Double, type: CurrencyType): String {
    return NumberFormatter.withLocale(Locale.getDefault())
      .notation(Notation.compactLong())
      .unit(type.asCurrency())
      .precision(Precision.maxSignificantDigits(2))
      .format(value)
      .toString()
  }

  private fun formatLegacy(value: Double, type: CurrencyType): String {
    return NumberFormat.getInstance(Locale.getDefault())
      .apply {
        currency = type.asCurrency()
        maximumFractionDigits = 2
      }
      .format(value)
  }

  private fun CurrencyType.asCurrency(): Currency {
    return when (this) {
      CurrencyType.USD -> Currency.getInstance(Locale.US)
      CurrencyType.EUR -> Currency.getInstance("EUR")
    }
  }
}
