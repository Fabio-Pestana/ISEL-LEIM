package dam_A50756.gymwiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_A50756.gymwiki.data.ExerciseData
import dam_A50756.gymwiki.data.MyFirebase
import dam_A50756.gymwiki.ui.SearchUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {
    val firebase = MyFirebase()
    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState: StateFlow<SearchUIState> = _uiState.asStateFlow()

    private var allExercises: List<ExerciseData> = emptyList()

    init {
        getAllExercises()
    }

    private fun getAllExercises() {
        viewModelScope.launch {

            _uiState.update { it.copy(isSearching = true) }

            firebase.firestore.collection("exercicios")
                .get()
                .addOnSuccessListener { result ->
                    allExercises = result.map { doc ->
                        ExerciseData(
                            id = doc.id,
                            nome = doc.getString("nome") ?: "",
                            tipo = doc.getString("tipo") ?: "",
                            grupoMuscular = doc.getString("grupoMuscular") ?: "",
                            equipamento = doc.getString("equipamento") ?: "",
                            dificuldade = doc.getString("dificuldade") ?: "",
                            instrucoes = doc.getString("instrucoes") ?: "",
                            youtube = doc.getString("youtube") ?: ""
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isSearching = false,
                            searchResults = allExercises
                        )
                    }
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isSearching = false) }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(searchInput = query)
        }

        if (query.isBlank()) {
            _uiState.update { state ->
                state.copy(searchResults = allExercises)
            }
        } else {
            val filtered = allExercises.filter { exercise ->
                exercise.nome.contains(query, ignoreCase = true)
            }
            _uiState.update { state ->
                state.copy(searchResults = filtered)
            }
        }
    }

    fun clearSearch() {
        _uiState.update {
            SearchUIState(
                searchResults = allExercises
            )
        }
    }
}