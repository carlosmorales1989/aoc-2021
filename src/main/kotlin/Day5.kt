import java.io.File

fun main() {
    val lines = File("data/day5.txt").readLines()

    val vents = mutableListOf<Vent>()

    val mappedPoints = mutableMapOf<Point, Int>()

    val dangerPoints = mutableSetOf<Point>()

    for (line in lines)
    {
        val info = line.split(" -> ")
            .map{ coordToMap(it) }
        val vent = Vent(info[0], info[1])
        vents.add(vent)
        vent.getCoords().forEach { point->
            var count = mappedPoints.getOrPut(point) { 0 }
            mappedPoints[point] = ++count
            if (count > 1){
                dangerPoints.add(point)
            }
        }

    }
    println(mappedPoints)
    println(dangerPoints)
    println(dangerPoints.size)

}


fun coordToMap(coord: String): Point{
    val coords = coord.split(",")
        .map{ it.toInt() }
    return Point(coords[0], coords[1])
}

class Point(val x: Int,val y: Int)
{
    override fun equals(other: Any?) = (other is Point) && other.x == x && other.y == y

    override fun toString() = "$x,$y"

    override fun hashCode() = x*10000 + y
}

class Vent(val start: Point, val end: Point)
{
    fun getCoords(): List<Point>
    {
        val coords = mutableListOf<Point>()
        if(start.x == end.x){
            val initialCoord = Math.min(start.y, end.y)
            val endCoord = Math.max(start.y, end.y)

            for (index in initialCoord until endCoord + 1) {
                coords.add(Point(start.x, index))
            }
        }else if (start.y == end.y){
            val initialCoord = Math.min(start.x, end.x)
            val endCoord = Math.max(start.x, end.x)

            for (index in initialCoord until endCoord + 1) {
                coords.add(Point(index, start.y))
            }
        }else{
            val initialP = if (start.x < end.x) start else end
            val finalP = if (start.x < end.x) end else start

            var currentPoint = initialP
            val isAsc = initialP.y < finalP.y

            while (currentPoint.x <= finalP.x){
                coords.add(currentPoint)
                currentPoint = Point(currentPoint.x + 1,
                currentPoint.y + (if (isAsc) 1 else -1))
            }

        }
        return coords
    }
}