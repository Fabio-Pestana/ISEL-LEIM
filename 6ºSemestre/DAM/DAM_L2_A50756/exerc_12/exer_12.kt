class MagicBox<T> where T : Any
{
    var item : T? = null

    fun store(item: T)
    {
        this.item = item
    }

    fun retrieve() : T?
    {
        return item
    }

    fun transform(action: (T) -> T) : T?
    {
        // ? -> Save call operator, dentro da scope function let "it" não é null
        // https://kotlinlang.org/docs/scope-functions.html#functions

        return item?.let {
            val transformed = action(it)
            item = transformed
            transformed
        }
    }
}

fun main () {

    // Magic Box for Numbers
    val numberBox = MagicBox <Int>()
    numberBox.store (5)
    println (" Retrieved : ${ numberBox.retrieve()}") // Retrieved: 5
    println (" Transformed : ${ numberBox.transform { it * 2 }}") // Transformed: 10


    // Magic Box for Strings
    val stringBox = MagicBox <String>()
    stringBox.store (" Hello ")
    println (" Transformed : ${ stringBox.transform { it.reversed() }}") // Transformed: "olleH"

    // Magic Box for a Custom Object
    data class Person ( val name : String )
    val personBox = MagicBox <Person>()
    personBox.store ( Person (" Alice ") )
    println (" Transformed : ${ personBox.transform { Person (it.name.uppercase ()) }}")
    // Transformed: Person(name=ALICE)
}

