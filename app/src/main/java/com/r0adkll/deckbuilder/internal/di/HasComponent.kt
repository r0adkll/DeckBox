package gov.scstatehouse.houseofcards.di


interface HasComponent<out C> {
    fun getComponent(): C
}