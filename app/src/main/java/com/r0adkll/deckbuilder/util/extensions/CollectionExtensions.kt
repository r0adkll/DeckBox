package com.r0adkll.deckbuilder.util.extensions



fun <T1, T2> Collection<T1>.combine(other: Iterable<T2>): List<Pair<T1, T2>> {
    return combine(other, {thisItem: T1, otherItem: T2 -> Pair(thisItem, otherItem) })
}


fun <T1, T2, R> Collection<T1>.combine(other: Iterable<T2>, transformer: (thisItem: T1, otherItem:T2) -> R): List<R> {
    return this.flatMap { thisItem -> other.map { otherItem -> transformer(thisItem, otherItem) }}
}


fun <T> Collection<T>.shuffle(iterations: Int): List<T> {
    val items = this.toMutableList()
    (0 until iterations).forEach {
        items.shuffle()
    }
    return items.toList()
}

/**
 * Find and replace the first occurence of an item, as defined by the [selector] and if no item was found add it to
 * the end of the list
 *
 * @param item the item you wish to swap with
 * @param selector the selector to determine which item to swap with
 * @return a list with the first item found by [selector] replaced with [item], or added to end of list if no item found to replace
 */
fun <Item> List<Item>.findAndReplace(item: Item, selector: (Item) -> Boolean): List<Item> {
    val items = toMutableList()
    var didReplace = false
    for(index in (0 until items.size)) {
        if (selector(items[index])) {
            items[index] = item
            didReplace = true
            break
        }
    }

    if (!didReplace) {
        items += item
    }

    return items
}

fun <Item> List<Item>.findAndUpdate(selector: (Item) -> Boolean, updater: (Item?) -> Item): List<Item> {
    val items = toMutableList()
    var didReplace = false
    for(index in (0 until items.size)) {
        if (selector(items[index])) {
            items[index] = updater(items[index])
            didReplace = true
            break
        }
    }

    if (!didReplace) {
        items += updater(null)
    }

    return items
}