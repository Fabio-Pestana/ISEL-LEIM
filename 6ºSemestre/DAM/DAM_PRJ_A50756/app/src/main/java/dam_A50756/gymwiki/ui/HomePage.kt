
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.ui.BottomNavigationBar
import dam_A50756.gymwiki.ui.ExerciseCategorySection
import dam_A50756.gymwiki.ui.PageTop
import dam_A50756.gymwiki.viewmodel.SearchViewModel

@Composable
fun HomePage(
    onProfileClick: () -> Unit = {},
    navController: NavHostController,
    typeExercises: List<Pair<String, Int>>,
    muscleGroups: List<Pair<String, Int>>
) {
    val viewModel: SearchViewModel = viewModel()

    Column {
        PageTop(
            "Welcome to Gym Wiki!",
            navController,
            isHome = true,
            viewModel = viewModel
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ExerciseCategorySection(
                title = "Type of Exercise",
                onNavigate = {
                    navController.navigate("list/type_of_exercise")
                },
                items = typeExercises,
                navController,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ExerciseCategorySection(
                title = "Muscle Group Exercises",
                onNavigate = {
                    navController.navigate("list/muscle_group")
                },
                items = muscleGroups ,
                navController = navController
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            isHome = true,
            isProfile = false,
            onHomeClick = {},
            onProfileClick = onProfileClick
        )
    }
}

