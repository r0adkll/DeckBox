package app.deckbox.core.settings

interface EnumSetting {
  val storageKey: String
}

interface EnumSettingProvider<T> where T : Enum<T>, T : EnumSetting {
  fun fromStorageKey(key: String?): T
}
