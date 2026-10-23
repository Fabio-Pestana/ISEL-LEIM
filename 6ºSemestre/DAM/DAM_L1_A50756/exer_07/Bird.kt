class Bird(
    name: String,
    age: Int,
    species: String,
    val canFly: Boolean = true
) : Animal(name, age, species) {

    fun consegueVoar(): String{
        return if (canFly) "consegue voar" else "não consegue voar"
    }

    /*
    init {
        println("O $name é uma ave e ${consegueVoar()}.")
    }
    */

    override fun reproduce(newbornName: String): Animal {
        println("O filho(a) da $name está a sair do ovo. O seu nome vai ser $newbornName.")
        return Bird(newbornName, 0, species, canFly)
    }

    override fun toString(): String {
        return "A ave $name da espécie $species tem $age anos ($getAge) e ${consegueVoar()}."
    }

}
fun main(){
    val jade = Bird("Jade", 10, "Cardeal-Amarelo")
    println(jade)

    val chuck = jade.reproduce("Chuck")
    println(chuck)
}
