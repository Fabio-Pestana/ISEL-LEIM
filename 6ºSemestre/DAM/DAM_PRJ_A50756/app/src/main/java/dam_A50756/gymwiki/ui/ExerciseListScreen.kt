package dam_A50756.gymwiki.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.viewmodel.SearchViewModel
import java.net.URLEncoder

@Composable
fun ExerciseListScreen(
    categoryType: String,
    navController: NavHostController,
    typeExercises: List<Pair<String, Int>>,
    muscleGroups: List<Pair<String, Int>>
) {
    val viewModel: SearchViewModel = viewModel()

    val title = when (categoryType) {
        "type_of_exercise" -> "Type of Exercise"
        "muscle_group" -> "Muscle Group Exercises"
        else -> "Exercises"
    }

    val exercises = when (categoryType) {
        "type_of_exercise" -> typeExercises
        "muscle_group" -> muscleGroups
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageTop(title, navController, viewModel = viewModel)


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .offset(y = (-10).dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(exercises) { (name, imageRes) ->
                ExerciseCard(
                    name = name,
                    imageRes = imageRes,
                    onClick = {
                        val route = when (categoryType) {
                            "type_of_exercise" -> "exercises/tipo/${URLEncoder.encode(name, "UTF-8")}"
                            "muscle_group" -> "exercises/grupoMuscular/${URLEncoder.encode(name, "UTF-8")}"
                            else -> "exercises/${URLEncoder.encode(name, "UTF-8")}"
                        }
                        navController.navigate(route)
                    },
                    heightCard = 200,
                    heightImg = 150
                )
            }
        }

        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            onHomeClick = { navController.navigate("homePage") },
            onProfileClick = { navController.navigate("profile") }
        )
    }
}

