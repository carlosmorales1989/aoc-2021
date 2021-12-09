import java.io.File
import java.math.BigInteger
import kotlin.math.abs

fun main() {
    val lines = File("data/day7.txt").readLines()

    val crabPositions = lines[0].split(",")
        .map{ it.toInt() }

    var minFuelCost = -1

    println(crabPositions)

    val minIndex = crabPositions.minOf { it }
    val maxIndex = crabPositions.maxOf { it }

    for (i in minIndex until maxIndex) {
        var currentFuelCost = 0
        for (j in crabPositions.indices) {
            currentFuelCost += part2Distance(i, crabPositions[j])
        }

        if (minFuelCost == -1 || currentFuelCost < minFuelCost){
            minFuelCost = currentFuelCost
        }
    }
    println(minFuelCost)

}

fun part1Distance(pos1: Int, pos2: Int): Int{
    return abs(pos1 - pos2)
}

fun part2Distance(pos1: Int, pos2: Int): Int{
    val distance = abs(pos1 - pos2)
    return distance * (distance + 1)/2
}

