import java.util.*

fun main() {

    println("Digite uma expressão matemática (mínimo 9 caracteres):")
    val input = readLine() ?: ""

    // Validação do tamanho mínimo
    if (input.length < 9) {
        println("Erro: a expressão deve ter no mínimo 9 caracteres.")
        return
    }

    // tratamento de exceções no cálculo: Evita que o programa quebre, Captura erros como: divisão por zero/expressão inválida
    try {
        val result = calcular(input)
        println("Resultado: $result")
    } catch (e: Exception) {
        println("Erro ao calcular: ${e.message}")
    }
}

// Função principal que resolve a expressão
fun calcular(expressao: String): Double {
    val valores = Stack<Double>() // guarda números
    val operadores = Stack<Char>() // guarda operadores

    var i = 0

     // Percorre a string/Analisa cada caractere

    while (i < expressao.length) {
        val c = expressao[i]

        when {
            c == ' ' -> {} // ignora espaços

            c.isDigit() -> {
                var numero = ""
                // junta números com mais de 1 dígito
                while (i < expressao.length && (expressao[i].isDigit() || expressao[i] == '.')) {
                    numero += expressao[i]
                    i++
                }
                valores.push(numero.toDouble())
                i-- // corrige o incremento extra
            }
                     //Resolve expressões internas primeiro Ex: (2 + 3) * 4
            c == '(' -> operadores.push(c)

            c == ')' -> {
                while (operadores.peek() != '(') {
                    valores.push(aplicarOperacao(operadores.pop(), valores.pop(), valores.pop()))
                }
                operadores.pop() // remove '('
            }

            c in listOf('+', '-', '*', '/') -> {
                while (operadores.isNotEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                    valores.push(aplicarOperacao(operadores.pop(), valores.pop(), valores.pop()))
                }
                operadores.push(c)
            }
        }
        i++
    }

    // resolve o resto da pilha
    while (operadores.isNotEmpty()) {
        valores.push(aplicarOperacao(operadores.pop(), valores.pop(), valores.pop()))
    }

    return valores.pop()
}

// Define prioridade dos operadores
fun precedencia(op: Char): Int {
    return when (op) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> 0
    }
}

// Aplica a operação matemática
fun aplicarOperacao(op: Char, b: Double, a: Double): Double {
    return when (op) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> {
            if (b == 0.0) throw ArithmeticException("Divisão por zero")
            a / b
        }
        else -> 0.0
    }
}
