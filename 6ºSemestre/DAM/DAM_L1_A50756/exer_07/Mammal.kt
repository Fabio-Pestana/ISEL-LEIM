class Mammal(
    name: String,
    age: Int,
    species: String,
    val furColor: String
) : Animal(name, age, species) {

    /*
    init {
        println("O $name é um mamífero com pelo $furColor.")
    }
    */

    override fun reproduce(newbornName: String): Animal {
        println("$name está a dar à luz a um(a) novo(a) $species chamado(a) $newbornName.")
        return Mammal(newbornName, 0, species, furColor)
    }

    override fun toString(): String {
        return "O mamífero $name da espécie $species tem $age anos ($getAge) e a cor do seu pelo é $furColor."
    }

}
fun main(){
    val nala = Mammal("Nala",  5,"leão", "Dourado")
    println(nala)

    nala.age = 2
    println(nala)

    val simba = nala.reproduce("Simba")
    println(simba)
}
