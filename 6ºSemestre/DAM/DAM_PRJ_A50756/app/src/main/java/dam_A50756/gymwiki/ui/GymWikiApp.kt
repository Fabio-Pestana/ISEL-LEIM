package dam_A50756.gymwiki.ui

import HomePage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dam_A50756.gymwiki.R
import dam_A50756.gymwiki.data.MyFirebase
import java.net.URLDecoder

@Composable
fun GymWikiApp() {
    val navController = rememberNavController()

    val firebase = MyFirebase()
    val isUserLogged = firebase.isUserLoggedIn()

    LaunchedEffect(isUserLogged) {
        if (isUserLogged) {
            navController.navigate("homePage") {
            }
        }
    }

    val typeExercises = listOf(
        "Cardio" to R.drawable.cardio,
        "Olympic Weightlifting" to R.drawable.olympic_weightlifting,
        "Strength" to R.drawable.strength,
        "Stretching" to R.drawable.stretching,
        "Strongman" to R.drawable.strongman
    )

    val muscleGroups = listOf(
        "Abdominals" to R.drawable.abdominals,
        "Biceps" to R.drawable.biceps,
        "Chest" to R.drawable.chest,
        "Hamstrings" to R.drawable.hamstrings,
        "Lats" to R.drawable.lats,
        "Quadriceps" to R.drawable.quadriceps,
        "Triceps" to R.drawable.triceps
    )

    NavHost(
        navController = navController,
        startDestination = if (isUserLogged) "homePage" else "login"
    ) {
        composable("login") {
            Login(
                navController
            )
        }

        composable("createAccount") {
            CreateAccount(
                navController
            )
        }

        composable("homePage") {
            HomePage(
                onProfileClick = { navController.navigate("profile") },
                navController,
                typeExercises,
                muscleGroups
            )
        }

        composable("profile") {
            ProfileScreen(
                navController
            )
        }

        composable("edit") {
            EditScreen(
                navController
            )
        }

        composable("list/{categoryType}") { backStackEntry ->
            val categoryType = backStackEntry.arguments?.getString("categoryType") ?: ""
            ExerciseListScreen(
                categoryType = categoryType,
                navController,
                typeExercises,
                muscleGroups
            )
        }

        composable(
            "exercises/{exerciseType}/{name}",
            arguments = listOf(
                navArgument("exerciseType") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
        )) { backStackEntry ->
            val exerciseType = backStackEntry.arguments?.getString("exerciseType") ?: ""
            val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", "UTF-8")
            ExercisesScreen(
                exerciseType = exerciseType,
                name = name,
                navController = navController
            )
        }

        composable(
            "exercise_page/{id}") { backStackEntry ->
            val id = URLDecoder.decode(backStackEntry.arguments?.getString("id") ?: "", "UTF-8")
            ExercisePageScreen(
                exerciseId = id,
                navController = navController
            )
        }

        composable(
            "exercise_list/{nome}") { backStackEntry ->
            val nome = URLDecoder.decode(backStackEntry.arguments?.getString("nome") ?: "", "UTF-8")
            ListScreen(
                navController = navController,
                listName = nome
            )
        }
    }
}