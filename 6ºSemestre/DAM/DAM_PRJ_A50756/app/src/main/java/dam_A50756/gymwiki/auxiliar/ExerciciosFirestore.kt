import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dam_A50756.gymwiki.data.ExerciseData

class ExerciciosFirestore(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Lê os dados do ficheiro JSON e retorna uma lista de exercícios
    private fun lerExerciciosDoJson(context: Context): List<ExerciseData> {
        val inputStream = context.assets.open("triceps.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseData>>() {}.type
        return gson.fromJson(json, type)
    }

    fun adicionarExerciciosDoJson(context: Context) {
        val exercicios = lerExerciciosDoJson(context)
        val colecao = firestore.collection("exercicios")

        for (exercicio in exercicios) {
            colecao.whereEqualTo("nome", exercicio.nome)
                .get()
                .addOnSuccessListener { docs ->
                    if (docs.isEmpty) {
                        colecao.add(exercicio)
                    }
                }
        }
    }
}
