package dam_A50756.gymwiki.data

data class UserData(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profilePhotoBase64: String = "",
    val favorites: List<String> = emptyList(),
    val listas: List<ExerciseList> = emptyList()
)

data class ExerciseList(
    val nome: String = "",
    val exercicios: List<String> = emptyList()
)