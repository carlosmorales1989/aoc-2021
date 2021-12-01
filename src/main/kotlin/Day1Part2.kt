import java.io.File

fun main() {
    val lines = File("data/inputDay1.txt").readLines()
    lines.stream().map {  }
    var previousMeasurement = -1
    var increases = 0
    val windows = Array(lines.size+3){ 0 }
    for (i in lines.indices)
    {
        val measurement = lines[i].toInt()
        for (j in 0..2){
            windows[i+j] += measurement
        }

    }

    for (i in 2 until windows.size) {
        val measurement = windows[i]

        if (previousMeasurement != -1 && measurement > previousMeasurement)
            increases+=1
        previousMeasurement = measurement
    }
    println(increases)
}
