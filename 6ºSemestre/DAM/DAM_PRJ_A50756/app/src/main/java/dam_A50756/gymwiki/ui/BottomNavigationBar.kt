package dam_A50756.gymwiki.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A50756.gymwiki.data.MyFirebase


@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    isHome: Boolean = false,
    isProfile: Boolean = false,
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onListClick: () -> Unit = {}
) {

    var showDialog by remember { mutableStateOf(false) }
    val firebase = remember { MyFirebase() }
    val context = LocalContext.current

    if (showDialog) {
        var list by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            title = { Text("Create New List") },
            text = {
                OutlinedTextField(
                    value = list,
                    onValueChange = { list = it },
                    label = { Text("List name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color(0xFF0D5CAF),
                        unfocusedLabelColor = Color.Gray,
                        focusedIndicatorColor = Color(0xFF0D5CAF),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color(0xFF0D5CAF)
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if(list.isNotBlank()){
                        firebase.addNewListToUser(
                            listName = list,
                            onSuccess = {
                                showDialog = false
                                onListClick()
                            },
                            onFailure = { e ->
                                Toast.makeText(
                                    context,
                                    e.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }) {
                    Text("Create", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = Color.Black)
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF0D5CAF),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onHomeClick)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (isHome) Color.Black else Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Home",
                    color = if (isHome) Color.Black else Color.White,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = { showDialog = true })
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add List",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                    }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add List",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onProfileClick)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (isProfile) Color.Black else Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Profile",
                    color = if (isProfile) Color.Black else Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}