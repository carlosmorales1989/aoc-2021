import java.io.File

operator fun <T> List<T>.component6() = this[5]
operator fun <T> List<T>.component7() = this[6]

fun main() {
    //val lines = File("data/inputDay22_2.txt").readLines()
    val lines = File("data/day22.txt").readLines()

    val regex = Regex("(on|off) x=([-0-9]*)..([-0-9]*),y=([-0-9]*)..([-0-9]*),z=([-0-9]*)..([-0-9]*)")

    val turnedOn = mutableSetOf<Triple<Int,Int,Int>>()

    for(line in lines){
        val match = regex.find(line)
        println(line)
        val (status, sMinX, sMaxX, sMinY, sMaxY, sMinZ, sMaxZ) = match!!.destructured.toList()
        val boundaries = listOf(sMinX, sMaxX, sMinY, sMaxY, sMinZ, sMaxZ).map { it.toInt() }
        if(boundaries.any { it < -50 || it > 50 }){
            continue
        }
        val (minX, maxX, minY, maxY, minZ, maxZ) = boundaries

        println(status)
        for(x in minX..maxX){
            for(y in minY..maxY){
                for(z in minZ..maxZ){
                    val coords = Triple(x,y,z)
                    if(status == "on"){
                        turnedOn.add(coords)
                    }else{
                        if(coords in turnedOn)
                            turnedOn.remove(coords)
                    }
                }
            }
        }

    }
    println(turnedOn.size)

}
