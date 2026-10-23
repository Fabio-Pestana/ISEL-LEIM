/*
Código do Enunciado:

fun isPrime(n: Int): Boolean{
    if(n<2) return false
    if(n==2) return true
    if(n%2==0) return false
    val sqrtN = kotlin.math.sqrt(n.toDouble()).toInt()
    return (3..sqrtN step 2).all { n % it != 0 }
}

fun main(args: Array<String>) {

    val primes = generateSequence(2) { it + 1 }
        .filter { isPrime(it) } // filtra para guardar apenas números primos
        .take(10) // só os 10 primeiros números
        .toList() // converte o resultado numa lista

    println(primes.joinToString())
}
*/


fun main(args: Array<String>) {

    val palavras = generateSequence("hello") { it + ('a'..'z').random()  }
        .filter() { it.length % 2 == 0 }
        .take(10)
        .toList()

    println(palavras.joinToString())
}