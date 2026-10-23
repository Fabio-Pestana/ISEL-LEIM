package dam_A50756.gymwiki.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.net.URLEncoder

@Composable
fun ExerciseCategorySection(
    title: String,
    onNavigate: () -> Unit,
    items: List<Pair<String, Int>>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go to $title",
                modifier = Modifier.size(24.dp)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items) { (name, imageRes) ->
                ExerciseCard(
                    name = name,
                    imageRes = imageRes,
                    onClick = {
                        val route = when (title) {
                            "Type of Exercise" -> "exercises/tipo/${URLEncoder.encode(name, "UTF-8")}"
                            "Muscle Group Exercises" -> "exercises/grupoMuscular/${URLEncoder.encode(name, "UTF-8")}"
                            else -> "exercises/$name"
                        }
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}