package dam_A50756.gymwiki.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.R
import dam_A50756.gymwiki.data.ExerciseData
import dam_A50756.gymwiki.data.MyFirebase
import java.net.URLEncoder
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import dam_A50756.gymwiki.data.ExerciseList
import dam_A50756.gymwiki.data.UserData

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val firebase = remember { MyFirebase() }
    var userData by remember { mutableStateOf<UserData?>(null) }
    var favoriteExercises by remember { mutableStateOf<List<ExerciseData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }


    fun load(){
        firebase.getCurrentUserData(
            onSuccess = { data ->
                userData = data

                if (data.favorites.isNotEmpty()) {
                    val exercisesList = mutableListOf<ExerciseData>()
                    data.favorites.forEach { id ->
                        firebase.getExerciseById(id) { exercise ->
                            exercisesList.add(exercise)
                            favoriteExercises = exercisesList.toList()
                        }
                    }
                }
                isLoading = false
            },
            onFailure = {
                isLoading = false
                Toast.makeText(
                    context,
                    "Failed to load user data",
                    Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(Unit) {
        load()
    }

    if (isLoading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val user = userData!!
    val userName = user.name
    val userEmail = user.email


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PageTop(
            title = "Profile",
            navController = navController,
            searchBar = false,
            isProfile = true,
            image = user.profilePhotoBase64
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { navController.navigate("edit") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit profile",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                FavoriteExercises(
                    items = favoriteExercises,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth()
                )

                UserExerciseLists(
                    lists = userData?.listas ?: emptyList(),
                    navController = navController,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        firebase.logoutUser(navController)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D5CAF)
                    ),
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }

        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            isProfile = true,
            onHomeClick = { navController.navigate("homePage") },
            onProfileClick = { navController.navigate("profile") },
            onListClick = { load()}
        )
    }
}

@Composable
fun FavoriteExercises(
    items: List<ExerciseData>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    if(items.isEmpty()) return

    val muscleGroups = mapOf(
        "Abdominals" to R.drawable.abdominals,
        "Biceps" to R.drawable.biceps,
        "Chest" to R.drawable.chest,
        "Hamstrings" to R.drawable.hamstrings,
        "Lats" to R.drawable.lats,
        "Quadriceps" to R.drawable.quadriceps,
        "Triceps" to R.drawable.triceps
    )


    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Favorite Exercises",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items) { exercise ->
                val imageRes = muscleGroups[exercise.grupoMuscular] ?: R.drawable.chest

                ExerciseCard(
                    name = exercise.nome,
                    imageRes = imageRes,
                    onClick = {
                        navController.navigate("exercise_page/${URLEncoder.encode(exercise.id, "UTF-8")}")
                    },
                    heightCard = 150,
                    heightImg = 110
                )
            }
        }
    }
}


@Composable
fun UserExerciseLists(
    lists: List<ExerciseList>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    if(lists.isEmpty()) return

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "My Workout Lists",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(lists) { list ->
                ExerciseListCard(
                    list = list,
                    onClick = {
                        navController.navigate("exercise_list/${URLEncoder.encode(list.nome, "UTF-8")}")
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseListCard(
    list: ExerciseList,
    onClick: () -> Unit = {},
    heightCard: Int = 150
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(140.dp)
            .height(heightCard.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D5CAF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = list.nome,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${list.exercicios.size} exercises",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}