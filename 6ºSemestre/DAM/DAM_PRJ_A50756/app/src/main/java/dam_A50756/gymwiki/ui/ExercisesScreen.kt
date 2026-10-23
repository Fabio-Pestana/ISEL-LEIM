package dam_A50756.gymwiki.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.data.ExerciseData
import dam_A50756.gymwiki.data.MyFirebase
import dam_A50756.gymwiki.viewmodel.SearchViewModel
import java.net.URLEncoder

@Composable
fun ExercisesScreen(
    exerciseType: String,
    name: String,
    navController: NavHostController
) {
    val exercisesState = remember { mutableStateOf<List<ExerciseData>>(emptyList()) }
    val context = LocalContext.current
    val viewModel: SearchViewModel = viewModel()


    LaunchedEffect(name, exerciseType) {
        val firebase = MyFirebase()
        firebase.getExercisesByField(
            field = exerciseType,
            value = name,
            onSuccess = { exercises ->
                exercisesState.value = exercises
            },
            onFailure = {
                Toast.makeText(context, "Erro ao carregar exercícios", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageTop(title = "Exercises", navController = navController, viewModel = viewModel)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .offset(y = (-12).dp),
        ) {
            items(exercisesState.value) { exercise ->
                ExerciseListItem(
                    exercise = exercise,
                    onClick = {
                        navController.navigate("exercise_page/${URLEncoder.encode(exercise.id, "UTF-8")}")
                    }
                )
                Divider(color = Color.LightGray, thickness = 0.5.dp)
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

@Composable
fun ExerciseListItem(
    exercise: ExerciseData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D5CAF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Difficulty: ${exercise.dificuldade}",
                style = MaterialTheme.typography.bodyLarge,
                color = when (exercise.dificuldade) {
                    "Beginner" -> Color.White
                    "Intermediate" -> Color.LightGray
                    "Advanced" -> Color.Black
                    else -> Color.Red
                }
            )
        }
    }
}