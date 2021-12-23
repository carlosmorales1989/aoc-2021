import java.io.File
import java.lang.Integer.max
import java.math.BigInteger
import java.util.*

fun main() {
    val lines = File("data/day17.txt").readLines()
    //val lines = File("data/inputDay17.txt").readLines()

    val regex = Regex("target area: x=([-]?[0-9]*)..([-]?[0-9]*), y=([-]?[0-9]*)..([-]?[0-9]*)")
    val match = regex.find(lines[0])
    val (minX, maxX, minY, maxY) = match!!.destructured.toList().map { it.toInt() }
    print("$minX, $maxX, $minY, $maxY")
    var maxReachedY = 0
    var count = 0
    for(x in -150..150){
        for(y in -150..150){
            var posX = 0
            var posY = 0
            var vX = x
            var vY = y
            //print("Processing Velocity $x,$y\n")
            var maxLocalY = 0
            while(vY >= 0 || posY >= minY) {
                //print("Velocity: $vX,$vY Position: $posX,$posY\n")
                if (posX in minX..maxX && posY in minY..maxY) {
                    maxReachedY = max(maxReachedY, maxLocalY)
                    count+=1
                    print("$x,$y\n")
                    break
                }
                posX += vX
                posY += vY
                vY -= 1
                if (vX > 0) {
                    vX -= 1
                } else if (vX < 0) {
                    vX += 1
                }
                maxLocalY = max(maxLocalY, posY)
            }

        }
    }
    print("Total max: $maxReachedY")
    print("Total count: $count")

}






