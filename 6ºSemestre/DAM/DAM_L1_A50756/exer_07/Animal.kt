abstract class Animal (
    val name : String,
    age : Int,
    val species : String
){
    val getAge: String
        get() = if (age < 3) "Jovem" else "Adulto"

    var age : Int = age
        set (value){
            field = if(value >= 0) value else throw Exception("Idade não pode ser negativa.")
        }

    init {
        println("O animal $name da espécie $species foi criado.")
    }

    abstract fun reproduce(newbornName: String):Animal

    override fun toString(): String {
        return "O animal $name da espécie $species tem $age anos ($getAge)."
    }
}
