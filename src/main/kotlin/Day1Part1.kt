import java.io.File

fun main() {
    val lines = File("data/inputDay1.txt").readLines()
    var previousMeasurement = -1
    var increases = 0
    for (line in lines) {
        val measurement = line.toInt()

        if (previousMeasurement != -1 && measurement > previousMeasurement)
            increases+=1
        previousMeasurement = measurement
    }
    println(increases)
}