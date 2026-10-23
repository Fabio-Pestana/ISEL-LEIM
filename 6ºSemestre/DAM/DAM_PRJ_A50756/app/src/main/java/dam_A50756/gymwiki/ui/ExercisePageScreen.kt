package dam_A50756.gymwiki.ui

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dam_A50756.gymwiki.data.ExerciseData
import dam_A50756.gymwiki.data.MyFirebase

@Composable
fun ExercisePageScreen(
    exerciseId: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    var ex by remember { mutableStateOf<ExerciseData?>(null) }
    val firebase = remember { MyFirebase() }
    var showDialog by remember { mutableStateOf(false) }
    var listNames by remember { mutableStateOf<List<String>>(emptyList()) }
    val selectedLists = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(exerciseId) {
        firebase.getExerciseById(
            exerciseId,
            onSuccess = { fetchedExercise ->
                ex = fetchedExercise

                val userId = firebase.getCurrentUserId()
                if (userId != null) {
                    firebase.firestore.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { doc ->
                            val favorites = doc.get("favorites") as? List<String> ?: emptyList()
                            isFavorite = favorites.contains(exerciseId)
                        }
                }
            }
        )
    }


    if (ex == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val exercise = ex!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PageTop(title = exercise.nome, navController = navController, searchBar = false)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoChip(text = exercise.grupoMuscular)
                InfoChip(text = exercise.tipo)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { firebase.toggleFavorite(
                        exerciseId = exercise.id,
                        onComplete = { isNowFavorite ->
                            isFavorite = isNowFavorite
                        })
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    // para realizar a animacao vi tutorial
                    // do Phillip Lackner sobre animacoes

                    val scale by animateFloatAsState(
                        targetValue = if (isFavorite) 1.2f else 1f,
                        animationSpec = tween(durationMillis = 300),
                        label = "favoriteScaleAnimation"
                    )
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(scale)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Difficulty",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = exercise.equipamento,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = exercise.dificuldade,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.instrucoes,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Video Tutorial",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            YouTubePlayerPaused(videoId = exercise.youtube)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        firebase.getUserListNames(
                            onSuccess = { names ->
                                listNames = names
                                selectedLists.clear()
                                names.forEach { selectedLists[it] = false }
                                showDialog = true
                            }
                        )
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D5CAF)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to a List")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color.White,
                title = {
                    Text(text = "Select Lists", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        listNames.forEach { listName ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = selectedLists[listName] ?: false,
                                    onCheckedChange = { isChecked ->
                                        selectedLists[listName] = isChecked
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF0D5CAF)
                                    )
                                )
                                Text(text = listName)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedLists
                            .filter { it.value }
                            .forEach { (listName, _) ->
                                firebase.addExerciseToList(
                                    listName = listName,
                                    exerciseId = exercise.id,
                                    onSuccess = {
                                        showDialog = false
                                        Toast.makeText(
                                            context,
                                            "Exercise added successfully!",
                                            Toast.LENGTH_SHORT).show()},
                                    onFailure = { e ->
                                        Toast.makeText(
                                            context,
                                            e.message,
                                            Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        showDialog = false
                    }) {
                        Text("Confirm", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = Color.Black)
                    }
                }
            )
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
fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .background(
                color = Color(0xFF0D5CAF).copy(alpha = 0.56f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun YouTubePlayerPaused(videoId: String) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    // cueVideo fica parado
                    // loadVideo -> autoplay
                    }
                })
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
}