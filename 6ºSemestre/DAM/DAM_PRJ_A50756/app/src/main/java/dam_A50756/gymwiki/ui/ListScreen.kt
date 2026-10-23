package dam_A50756.gymwiki.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.data.ExerciseData
import dam_A50756.gymwiki.data.MyFirebase

@Composable
fun ListScreen(
    navController: NavHostController,
    listName: String
) {
    val exercisesState = remember { mutableStateOf<List<ExerciseData>>(emptyList()) }
    val firebase = remember { MyFirebase() }
    val showDeleteDialog = remember { mutableStateOf(false) }

    LaunchedEffect(listName) {
        firebase.getListByName(
            listName = listName,
            onSuccess = { exerciseList ->
                exerciseList.exercicios.forEach { exerciseId ->
                    firebase.getExerciseById(exerciseId) { exercise ->
                        exercisesState.value += exercise
                    }
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    color = Color(0xFF0D5CAF),
                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                )
                .padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = listName,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete List",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { showDeleteDialog.value = true }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(exercisesState.value) { exercise ->
                ListExerciseItem(
                    exercise = exercise,
                    onDeleteClick = {
                        firebase.removeExerciseFromList(
                            listName = listName,
                            exerciseId = exercise.id,
                            onSuccess = {
                                exercisesState.value = exercisesState.value.filter { it.id != exercise.id }
                            }
                        )
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

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Delete List") },
            text = { Text("Are you sure you want to delete this list?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        firebase.deleteUserListByName(
                            listName = listName,
                            onSuccess = {
                                navController.popBackStack()
                            }
                        )
                        showDeleteDialog.value = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ListExerciseItem(
    exercise: ExerciseData,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D5CAF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = exercise.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove from list",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onDeleteClick)
            )
        }
    }
}