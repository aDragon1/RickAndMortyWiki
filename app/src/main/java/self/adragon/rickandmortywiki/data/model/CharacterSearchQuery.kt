package self.adragon.rickandmortywiki.data.model

data class CharacterSearchQuery(
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null
) {
    fun isEmpty() = name.isNullOrBlank()
            && status.isNullOrBlank()
            && species.isNullOrBlank()
            && type.isNullOrBlank()
            && gender.isNullOrBlank()
}