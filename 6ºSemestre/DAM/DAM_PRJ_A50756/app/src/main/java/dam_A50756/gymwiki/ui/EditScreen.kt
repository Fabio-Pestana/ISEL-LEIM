package dam_A50756.gymwiki.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dam_A50756.gymwiki.R
import dam_A50756.gymwiki.data.MyFirebase
import dam_A50756.gymwiki.data.UserData
import coil.compose.rememberAsyncImagePainter
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil3.Bitmap


@Composable
fun EditScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val firebase = remember { MyFirebase() }
    var userData by remember { mutableStateOf<UserData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    fun load(){
        firebase.getCurrentUserData(
            onSuccess = { data ->
                userData = data
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

    var currentName = user.name
    var newName by remember { mutableStateOf(TextFieldValue(currentName)) }

    val userBitmap = user.profilePhotoBase64.let { base64ToBitmap(it) }

    val profilePainter = when {
        selectedImageUri != null -> rememberAsyncImagePainter(model = selectedImageUri)
        userBitmap != null -> rememberAsyncImagePainter(model = userBitmap)
        else -> painterResource(id = R.drawable.profile)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PageTop(
            title = "Edit Profile",
            navController = navController,
            searchBar = false
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(170.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                ) {
                    Image(
                        painter = profilePainter,
                        contentDescription = "Profile picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit photo",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Current Name:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
            )

            Text(
                text = currentName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "New Name:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Enter new name") }
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = {
                    val nameChanged = newName.text.isNotBlank() && newName.text != currentName
                    val imageChanged = selectedImageUri != null

                    val base64Image = selectedImageUri?.let { uriToBase64(context, it) }

                    if (!nameChanged && !imageChanged) {
                        Toast.makeText(context, "No changes to save", Toast.LENGTH_SHORT).show()
                    }else{
                        firebase.updateUserProfile(
                            newName = if (nameChanged) newName.text else null,
                            imageBase64 = base64Image,
                            onSuccess = {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onFailure = {
                                Toast.makeText(context, "Failed to update profile: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D5CAF)
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Changes")
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

fun uriToBase64(context: Context, uri: Uri): String? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        if (bytes != null) {
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    } catch (e: Exception) { }
    return null
}

fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}