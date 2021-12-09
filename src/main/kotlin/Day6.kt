import java.io.File
import java.math.BigInteger

fun main() {
    val lines = File("data/day6.txt").readLines()

    val fish = lines[0].split(",")
        .map{ it.toInt() }
        .toList()

    val fishMap = Array(9){0.toBigInteger()}

    fish.forEach{
        fishMap[it] = fishMap[it].inc()
    }

    println("Initial state ${fishMap.toList()}")

    for (day in 1 until 257)
    {
        val newFish = fishMap[0]

        for (j in 1 until 9){
            fishMap[j-1] = fishMap[j]
        }

        fishMap[8] = newFish
        fishMap[6] += newFish
        println("After $day days: ${fishMap.toList()}")

    }
    var result = 0.toBigInteger()

    fishMap.forEach { result = result.add(it) }
    println("Sum: $result")

}

