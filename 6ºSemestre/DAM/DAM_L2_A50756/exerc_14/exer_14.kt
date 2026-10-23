class BitNumbers(
    val bytes : ByteArray
){
    init {
        require(bytes.size == 64){
            "O byteArray introduzido tem de ter 64 bytes (512 bits)."
        }
    }

    operator fun plus(b: BitNumbers): BitNumbers{
        val resultado = ByteArray(64)
        var carry = 0

        // Ultimo do array é o bit mais à direita
        // -127 a 128
        for (i in 63 downTo 0){
            val soma = (bytes[i].toInt() and 0xFF) + (b.bytes[i].toInt() and 0xFF) + carry
            resultado[i] = soma.toByte()
            carry = soma ushr 8
        }

        return BitNumbers(resultado)
    }

    operator fun minus(b: BitNumbers): BitNumbers {
        val resultado = ByteArray(64)
        var borrow = 0

        for (i in 63 downTo 0) {

            var dif = (bytes[i].toInt() and 0xFF) - (b.bytes[i].toInt() and 0xFF) - borrow

            if (dif < 0) {
                dif += 256
                borrow = 1
            } else {
                borrow = 0
            }
            resultado[i] = dif.toByte()
        }

        return BitNumbers(resultado)
    }

    fun mostrarBytes(): String {
        return bytes.joinToString(" ") { "%02x".format(it) }
    }
}

fun main() {
    val one = ByteArray(64).also { it[63] = 0x01.toByte() }
    val ten = ByteArray(64).also { it[63] = 0x0A.toByte() }
    val maxByte = ByteArray(64).also { it[63] = 0xFF.toByte() }

    val zero = BitNumbers(ByteArray(64))
    val num1 = BitNumbers(one)
    val num10 = BitNumbers(ten)
    val num255 = BitNumbers(maxByte)

    val sum = num1 + zero
    println("1 + 0: ${sum.mostrarBytes()}")

    val diff = num10 - num1
    println("10 - 1: ${diff.mostrarBytes()}")

    // Ver o Carry
    val sumOverflow = num255 + num1
    println("255 + 1: ${sumOverflow.mostrarBytes()}")

    // Numero negativo
    val diffNegative = zero - num10
    println("0 - 10: ${diffNegative.mostrarBytes()}")
}