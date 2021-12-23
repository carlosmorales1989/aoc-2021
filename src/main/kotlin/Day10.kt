import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day10.txt").readLines()

    val invalidChars = mutableListOf<Char>()

    //val closingCharMap = mutableMapOf(')' to '(', '}' to '{', ']' to '[', '>' to '<')
    val closingCharMap = mutableMapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
    val openingChars = mutableListOf('(','[','{','<')

    val autoCompleteScores = mutableListOf<BigInteger>()

    lines.forEach { line ->
        val stack = Stack<Char>()
        var correct = true
        for(i in line.indices){
            val char = line[i]
            if (char in openingChars) {
                stack.add(char)
            }else {
                if (char != closingCharMap[stack.peek()]){
                    println("$line expected ${closingCharMap[stack.peek()]} but got $char. Position: $i")
                    invalidChars.add(char)
                    correct = false
                    break
                }else{
                    stack.pop()
                }
            }
        }

        if(correct){
            val score = stack.map {
                when (it) {
                    '(' -> 1
                    '[' -> 2
                    '{' -> 3
                    '<' -> 4
                    else -> 0
                }
            }.reversed().map{it.toBigInteger()}
                .reduce { acc, i -> acc.times(5.toBigInteger()).plus(i) }
            autoCompleteScores.add(score)
        }

    }
    println(invalidChars)
    println("Invalid char score: " + invalidChars.map {
        when(it){
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }.sum())

    println(autoCompleteScores)
    println(autoCompleteScores.sorted()[autoCompleteScores.size/2])

}

