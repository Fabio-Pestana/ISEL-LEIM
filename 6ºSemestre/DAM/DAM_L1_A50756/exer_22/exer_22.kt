import java.lang.Exception

fun isNumber(x: String): Boolean{
    if (x.isEmpty())
        return false

    var temPonto = false

    for (i in x.indices) {

        if (i == 0 && x[i] == '-') continue

        if (x[i] == '.' && !temPonto) {
            temPonto = true
        } else if (!x[i].isDigit()) {
            println("$x não é um número")
            return false
        }
    }
    return true
}

fun stringParaDouble(): Double{
    var aux: String
    do {
        println("Insira um valor:")
        aux = readln()
    }while(!isNumber(aux))

    return aux.toDouble()
}

fun main(args: Array<String>) {
    println("**** Calculadora ****")
    val num1 = stringParaDouble()
    var num2 = 0.0

    println("Selecione a operação que deseja executar")
    println("+, -, *, /, &&, ||, !, shl, shr.")
    val operacao = readln()

    if (operacao != "!"){
        num2 = stringParaDouble()
    }

    val resultado = when(operacao){
        "+" -> num1 + num2
        "-" -> num1 - num2
        "*" -> num1 * num2
        "/" -> if (num2 != 0.0) num1/num2 else throw Exception("Divisão por zero.")
        "&&" -> num1.toInt() and(num2.toInt())
        "||" -> num1.toInt() or(num2.toInt())
        "!" -> num1.toInt().inv()
        "shl" -> num1.toInt() shl (num2.toInt())
        "shr" -> num1.toInt() shr (num2.toInt())
        else -> throw Exception("Operação inválida.")
    }

    val resultadoHex = resultado.toInt().toString(16)
    val resultadoBin = resultado.toInt().toString(2)

    println()
    println("**** Resultados ****")
    println("Decimal -> $resultado")
    println("Hexadecimal -> 0x$resultadoHex")
    println("Binário -> 0b$resultadoBin")
}