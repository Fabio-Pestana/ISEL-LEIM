class Zoo {
    private val animals = mutableListOf<Animal>()

    fun addAnimal(animal: Animal) {
        animals.add(animal)
        println("Novo animal adicionado ao jardim zoológico: \n$animal")
    }

    fun showAnimals() {
        if (animals.isEmpty()) {
            println("Jardim zoológico não tem animais.")
        } else {
            println("Os animais do jardim zoológico são")
            animals.forEach { println(it) }
        }
    }

    fun animalReproduce(name : String, newbornName : String){
        if (animals.isEmpty()) return

        for (a in animals){
            if (a.name == name){
                val filho = a.reproduce(newbornName)
                addAnimal(filho)
                break
            }
        }
    }
}

fun main() {
    val zoo = Zoo()

    val nala = Mammal("Nala",  5,"leão", "Dourado")
    val cody = Bird("Cody",  2, "penguim",false)
    val jade = Bird("Jade", 10, "Cardeal-Amarelo")

    println()

    zoo.addAnimal(nala)
    zoo.addAnimal(cody)
    zoo.addAnimal(jade)

    println()

    zoo.animalReproduce(jade.name, "Chuck")

    println()

    zoo.animalReproduce(nala.name, "Simba")

    println()

    zoo.showAnimals()

}