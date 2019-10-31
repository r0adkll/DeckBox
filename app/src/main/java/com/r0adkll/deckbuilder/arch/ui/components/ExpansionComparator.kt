package com.r0adkll.deckbuilder.arch.ui.components

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.util.extensions.fromReleaseDate

class ExpansionComparator : Comparator<Expansion> {

    override fun compare(lhs: Expansion, rhs: Expansion): Int {
        val lhsDate = lhs.releaseDate.fromReleaseDate()
        val rhsDate = rhs.releaseDate.fromReleaseDate()

        return if (lhsDate == rhsDate) {
            val lhsPromo = if (lhs.ptcgoCode?.contains("PR", true) == true || lhs.name.contains("promo", true)) 1 else 0
            val rhsPromo = if (rhs.ptcgoCode?.contains("PR", true) == true || rhs.name.contains("promo", true)) 1 else 0
            lhsPromo.compareTo(rhsPromo)
        } else {
            rhsDate.compareTo(lhsDate)
        }
    }
}
