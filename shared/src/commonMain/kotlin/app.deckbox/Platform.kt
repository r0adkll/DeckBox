package app.deckbox

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
