import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day11.txt").readLines()

    val energyLevels = lines.map { it.toCharArray().map { it - '0' }.toMutableList() }.toMutableList()

    val steps = 10000

    println(energyLevels)

    var totalFlashes = 0

    val debug = false

    for(step in 1 until steps+1){
        val flashed = mutableSetOf<Pair<Int,Int>>()
        val flashUpdateQueue: Queue<Pair<Int,Int>> = LinkedList()
        energyLevels.forEachIndexed { row, ints -> ints.forEachIndexed { col, value ->
            energyLevels[row][col] += 1
            if (energyLevels[row][col] > 9) {
                flashed.add(Pair(row, col))
                flashUpdateQueue.add(Pair(row, col))
            }
        } }

        if(debug) {
            println("Step $step:After initial increase")
            printEnergyLevels(energyLevels)
        }

        while(!flashUpdateQueue.isEmpty()){
            val nextPoint = flashUpdateQueue.poll()
            getNeighboursWithDiagonals(nextPoint,energyLevels.size, energyLevels[0].size).forEach {
                energyLevels[it.first][it.second] += 1
                if(energyLevels[it.first][it.second] > 9 && !flashed.contains(Pair(it.first,it.second))){
                    flashed.add(Pair(it.first,it.second))
                    flashUpdateQueue.add(Pair(it.first,it.second))
                }

            }
        }

        if (debug){
            println("Step $step:After flash increase")
            printEnergyLevels(energyLevels)
        }

        energyLevels.forEachIndexed { row, ints -> ints.forEachIndexed { col, value ->
            if (energyLevels[row][col] > 9)
                energyLevels[row][col] = 0
        } }

        if (debug) {
            println("Step $step:After energy reset")
            printEnergyLevels(energyLevels)
        }

        totalFlashes += flashed.size
        //println("Step $step: Flashes: $totalFlashes")
        if(flashed.size == energyLevels.size*energyLevels[0].size){
            println("Step: $step")
        }


    }

}

fun printEnergyLevels(levels: List<List<Int>>){
    levels.forEach {
        println(it)
    }
}

fun getNeighboursWithDiagonals(center: Pair<Int,Int> , height: Int, width: Int): List<Pair<Int,Int>> {
    return listOf(Pair(center.first-1,center.second),
        Pair(center.first+1,center.second),
        Pair(center.first,center.second-1),
        Pair(center.first,center.second+1),
        Pair(center.first+1,center.second+1),
        Pair(center.first-1,center.second+1),
        Pair(center.first+1,center.second-1),
        Pair(center.first-1,center.second-1))
        .filter { it.first in 0 until height && it.second in 0 until width }
}
