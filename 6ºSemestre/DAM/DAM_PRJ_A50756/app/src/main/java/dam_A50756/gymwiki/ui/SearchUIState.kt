package dam_A50756.gymwiki.ui

import dam_A50756.gymwiki.data.ExerciseData

data class SearchUIState (
    val searchInput: String = "",
    val searchResults: List<ExerciseData> = emptyList(),
    val isSearching: Boolean = false
)