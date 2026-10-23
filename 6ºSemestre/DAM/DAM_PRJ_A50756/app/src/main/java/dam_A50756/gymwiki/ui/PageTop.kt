package dam_A50756.gymwiki.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import dam_A50756.gymwiki.R
import dam_A50756.gymwiki.viewmodel.SearchViewModel


@Composable
fun PageTop(
    title: String,
    navController: NavHostController,
    searchBar: Boolean = true,
    isHome: Boolean = false,
    isProfile: Boolean = false,
    viewModel: SearchViewModel? = null,
    image: String = ""
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(SearchUIState()) }

    if (isHome) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    color = Color(0xFF0D5CAF),
                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                )
                .padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Search and learn new exercises!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 22.sp
                )
            }
        }
    } else {
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
                    text = title,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    if(searchBar) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-24).dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel?.updateSearchQuery(it)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                modifier = Modifier.clickable {
                                    searchQuery = ""
                                    viewModel?.clearSearch()
                                },
                                tint = Color.Gray
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search exercises",
                            color = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    textStyle = TextStyle.Default.copy(fontSize = 16.sp)
                )
            }

            if (searchQuery.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                ) {
                    when {
                        uiState.isSearching -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        uiState.searchResults.isEmpty() -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No exercises found",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Try a different search term",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        else -> {
                            LazyColumn {
                                items(uiState.searchResults) { exercise ->
                                    Text(
                                        text = exercise.nome,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .clickable {
                                                navController.navigate("exercise_page/${exercise.id}")
                                                searchQuery = ""
                                                viewModel?.clearSearch()
                                            },
                                        fontSize = 16.sp
                                    )
                                    if (exercise != uiState.searchResults.last()) {
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    if (isProfile) {
        val userBitmap = base64ToBitmap(image)

        val profilePainter = if(userBitmap != null){
            rememberAsyncImagePainter(model = userBitmap)
        }else{
            painterResource(id = R.drawable.profile)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-50).dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(158.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            ) {
                Image(
                    painter = profilePainter,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}