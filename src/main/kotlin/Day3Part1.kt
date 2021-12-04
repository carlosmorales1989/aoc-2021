import java.io.File

fun main() {
    val lines = File("data/inputDay3.txt").readLines()

    val oneCount = Array(lines[0].length){0}
    println(lines[0])
    println(lines[0].length)

    for (line in lines)
    {
        for (i in line.indices){
            if (line[i] == '1'){
                oneCount[i] += 1
            }
        }
    }

    var gammaRate = 0
    for (digit in oneCount){
        gammaRate *= 2
        if (digit >= lines.size/2)
            gammaRate += 1
    }

    val epsilonRate = Math.pow(2.0, lines[0].length+0.0) - 1 - gammaRate

    println(gammaRate)
    println(epsilonRate)
    println((gammaRate * epsilonRate).toInt())



}