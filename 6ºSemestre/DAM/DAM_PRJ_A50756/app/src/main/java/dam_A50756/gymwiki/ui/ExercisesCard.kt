package dam_A50756.gymwiki.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseCard(
    name: String,
    imageRes: Int,
    onClick: () -> Unit = {},
    heightCard: Int = 150,
    heightImg: Int = 110
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
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(heightImg.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}