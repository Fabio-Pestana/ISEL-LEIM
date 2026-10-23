package dam_A50756.gymwiki.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MyFirebase(
    private val auth: FirebaseAuth = Firebase.auth,
    val firestore: FirebaseFirestore = Firebase.firestore
)
{

    fun createUser(name: String, email: String, password: String, confirmPassword: String, context: Context, navController: NavHostController)
    {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(
                context,
                "Please fill in all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword){
            Toast.makeText(
                context,
                "Passwords don't match!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = Firebase.auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {

                                // Cria documento na coleção "users"
                                val userData = hashMapOf(
                                    "id" to user.uid,
                                    "profilePhotoBase64" to "",
                                    "favorites" to emptyList<String>(),
                                    "listas" to emptyList<Map<String, String>>()
                                )

                                firestore.collection("users")
                                    .document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Account created successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("login")
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            context,
                                            "Failed to create user document: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    updateTask.exception?.message ?: "Failed to set user name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        context,
                        task.exception?.message ?: "Failed while creating account",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    fun loginUser (email: String, password: String, context: Context, navController: NavHostController)
    {
        if (email.isBlank() || password.isBlank()){
            Toast.makeText(
                context,
                "Please fill in all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        context,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("homePage")
                }else{
                    Toast.makeText(
                        context,
                        task.exception?.message?:"Sign up failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun logoutUser(navController: NavHostController){
        auth.signOut()
        navController.navigate("login")
    }

    fun isUserLoggedIn(): Boolean{
        return auth.currentUser != null
    }

    fun resetPassword(email: String, context: Context): Boolean{
        var aux = false
        if(email.isNotBlank()){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(
                            context,
                            "Check your email to reset password!",
                            Toast.LENGTH_SHORT
                        ).show()
                        aux = true
                    }else{
                        Toast.makeText(
                            context,
                            "Registered email not found!",
                            Toast.LENGTH_SHORT
                        ).show()

                        aux = false
                    }
                }
        }else{
            Toast.makeText(
                context,
                "Please enter your email!",
                Toast.LENGTH_SHORT
            ).show()
        }
        return aux
    }

    fun getExercisesByField(
        field: String,
        value: String,
        onSuccess: (List<ExerciseData>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("exercicios")
            .whereEqualTo(field, value)
            .get()
            .addOnSuccessListener { result ->
                val exercises = result.mapNotNull { doc ->
                    val exercise = doc.toObject(ExerciseData::class.java)
                    exercise.id = doc.id
                    exercise
                }
                onSuccess(exercises)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getExerciseById(
        exerciseId: String,
        onSuccess: (ExerciseData) -> Unit
    ){
        firestore.collection("exercicios")
            .document(exerciseId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val exercise = ExerciseData(
                        id = document.id,
                        nome = document.getString("nome") ?: "",
                        grupoMuscular = document.getString("grupoMuscular") ?: "",
                        tipo = document.getString("tipo") ?: "",
                        equipamento = document.getString("equipamento") ?: "",
                        dificuldade = document.getString("dificuldade") ?: "",
                        instrucoes = document.getString("instrucoes") ?: "",
                        youtube = document.getString("youtube") ?: ""
                    )
                    onSuccess(exercise)
                }
            }
    }

    fun toggleFavorite(
        exerciseId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            return
        }

        val userDoc = firestore.collection("users").document(user.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)

            val currentFavorites = snapshot.get("favorites") as? List<String> ?: emptyList()
            val updatedFavorites = if (currentFavorites.contains(exerciseId)) {
                currentFavorites - exerciseId
            } else {
                currentFavorites + exerciseId
            }

            transaction.update(userDoc, "favorites", updatedFavorites)
            !currentFavorites.contains(exerciseId) //return de runTransaction
        }.addOnSuccessListener { isNowFavorite ->
            onComplete(isNowFavorite)
        }
    }

    fun getCurrentUserData(
        onSuccess: (UserData) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    val listasRaw = document.get("listas") as? List<Map<String, Any>> ?: emptyList()

                    val listas = listasRaw.map { map ->
                        val name = map["nome"] as? String ?: ""
                        val exercises = (map["exercicios"] as? List<String>) ?: emptyList()
                        ExerciseList(nome = name, exercicios = exercises)
                    }

                    val userData = UserData(
                        id = user.uid,
                        name = user.displayName ?: "",
                        email = user.email ?: "",
                        profilePhotoBase64 = document.getString("profilePhotoBase64") ?: "",
                        favorites = document.get("favorites") as? List<String> ?: emptyList(),
                        listas = listas
                    )
                    onSuccess(userData)
                } else {
                    onFailure(Exception("User document does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun addNewListToUser(
        listName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            return
        }

        val userDoc = firestore.collection("users").document(user.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)

            val listas = snapshot.get("listas") as? List<Map<String, Any>> ?: emptyList()

            if (listas.any { it["nome"] == listName }) {
                throw Exception("List with this name already exists")
            }

            val newList = mapOf("nome" to listName, "exercicios" to emptyList<String>())

            val updatedListas = listas + newList

            transaction.update(userDoc, "listas", updatedListas)
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    fun getUserListNames(
        onSuccess: (List<String>) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val listasRaw = document.get("listas") as? List<Map<String, Any>> ?: emptyList()
                    val listNames = listasRaw.mapNotNull { it["nome"] as? String }
                    onSuccess(listNames)
                }
            }
    }

    fun addExerciseToList(
        listName: String,
        exerciseId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            return
        }

        val userDoc = firestore.collection("users").document(user.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)
            val listas = snapshot.get("listas") as? MutableList<Map<String, Any>> ?: mutableListOf()

            val updatedListas = listas.map { lista ->
                if (lista["nome"] == listName) {
                    val exercises = (lista["exercicios"] as? List<String>)?.toMutableList() ?: mutableListOf()
                    if (!exercises.contains(exerciseId)) {
                        exercises.add(exerciseId)
                    }else{
                        throw Exception("List already has this exercise!")
                    }

                    mapOf("nome" to listName, "exercicios" to exercises)
                } else {
                    lista
                }
            }

            transaction.update(userDoc, "listas", updatedListas)
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    fun updateUserProfile(
        newName: String?,
        imageBase64: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .apply {
                if (!newName.isNullOrBlank()) {
                    setDisplayName(newName)
                }
            }.build()

        user.updateProfile(profileUpdates)
            .addOnSuccessListener {
                val updates = mutableMapOf<String, Any>()
                if (!imageBase64.isNullOrBlank()) {
                    updates["profilePhotoBase64"] = imageBase64
                }

                firestore.collection("users")
                    .document(user.uid)
                    .update(updates)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getListByName(
        listName: String,
        onSuccess: (ExerciseList) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val listasRaw = document.get("listas") as? List<Map<String, Any>> ?: emptyList()

                    val matchingList = listasRaw.find { it["nome"] == listName }

                    if (matchingList != null) {
                        val name = matchingList["nome"] as? String ?: ""
                        val exercises = matchingList["exercicios"] as? List<String> ?: emptyList()
                        val exerciseList = ExerciseList(nome = name, exercicios = exercises)
                        onSuccess(exerciseList)
                    }
                }
            }
    }

    fun deleteUserListByName(
        listName: String,
        onSuccess: () -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            return
        }

        val userDoc = firestore.collection("users").document(user.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)

            val listas = snapshot.get("listas") as? List<Map<String, Any>> ?: emptyList()

            val updatedListas = listas.filterNot { it["nome"] == listName }

            if (updatedListas.size == listas.size) {
                throw Exception("List '$listName' not found")
            }

            transaction.update(userDoc, "listas", updatedListas)
        }.addOnSuccessListener {
            onSuccess()
        }
    }

    fun removeExerciseFromList(
        listName: String,
        exerciseId: String,
        onSuccess: () -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            return
        }

        val userDoc = firestore.collection("users").document(user.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)
            val listas = snapshot.get("listas") as? MutableList<Map<String, Any>> ?: mutableListOf()

            val updatedListas = listas.map { lista ->
                if (lista["nome"] == listName) {
                    val exercises = (lista["exercicios"] as? List<String>)?.toMutableList() ?: mutableListOf()
                    exercises.remove(exerciseId)
                    mapOf("nome" to listName, "exercicios" to exercises)
                } else {
                    lista
                }
            }

            transaction.update(userDoc, "listas", updatedListas)
        }.addOnSuccessListener {
            onSuccess()
        }
    }



    fun getCurrentUserId(): String?{
        return auth.currentUser?.uid
    }
}