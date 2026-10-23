import kotlin.math.pow
import kotlin.math.sqrt



fun main() {
    // Primeira forma, em uma linha
    val fibonacci = Array(100) { i ->
        ((((1 + sqrt(5.0)) / 2).pow(i) -
                ((1 - sqrt(5.0)) / 2).pow(i)) / sqrt(5.0)).toInt()
    }

    // Segunda forma
    val f = IntArray(100)
    f[0] = 0
    f[1] = 1

    for (i in 2 .. 99){
        f[i] = f[i-1] + f[i-2]
    }

    println(fibonacci.joinToString())
    println(f.joinToString())

    // Comparação entre as formas anteriores.
    for(j in fibonacci.indices){
        if(fibonacci[j] != f[j]){
            print("Os valores deixam de ser iguais no índice: $j")
            break
        }
    }
}
