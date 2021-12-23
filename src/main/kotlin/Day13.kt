import java.io.File
import java.lang.Integer.max

fun main() {
    val lines = File("data/day13.txt").readLines()

    var points = lines.filter { it.contains(',') }
        .map { it.split(",")}
        .map { Pair(it[0].toInt(),it[1].toInt()) }
        .toSet()
    val foldingInstructions = lines.filter { it.startsWith("fold along") }
        .map { it.takeLast(it.length-11) }
        .map { it.split("=")}
        .map { Pair(it[0],it[1].toInt()) }
        .toList()

    var xDimension = points.map { it.first }.maxOf { it }
    var yDimension = points.map { it.second }.maxOf { it }

    //y
    var foldingLine: Int

    foldingInstructions.forEach {
        foldingLine = it.second
        val newPoints = mutableSetOf<Pair<Int,Int>>()
        if(it.first == "x"){
            val upperHalf = points.filter { it.first < foldingLine }
            val lowerHalf = points.filter { it.first > foldingLine }

            if (foldingLine > xDimension - foldingLine){
                // Lower half is bigger than upper half
                newPoints.addAll(upperHalf.map { Pair(it.first + (2*foldingLine - xDimension), it.second)})
                newPoints.addAll(lowerHalf.map { Pair(xDimension-it.first-1, it.second) })
            }else{
                // Upper half is bigger than lower half
                newPoints.addAll(upperHalf)
                newPoints.addAll(lowerHalf.map { Pair(2*foldingLine-it.first,it.second) })
            }
        }else{
            val upperHalf = points.filter { it.second < foldingLine }
            val lowerHalf = points.filter { it.second > foldingLine }

            if (foldingLine > yDimension - foldingLine){
                // Lower half is bigger than upper half
                newPoints.addAll(upperHalf.map { Pair(it.first, it.second + (2*foldingLine - yDimension))})
                newPoints.addAll(lowerHalf.map { Pair(it.first, yDimension-it.second-1) })
            }else{
                // Upper half is bigger than lower half
                newPoints.addAll(upperHalf)
                newPoints.addAll(lowerHalf.map { Pair(it.first, 2*foldingLine-it.second) })
            }
        }
        points = newPoints
        xDimension = points.map { it.first }.maxOf { it }
        yDimension = points.map { it.second }.maxOf { it }
        println(points)
        println("Size after fold: " + points.size)
    }
    for(row in 0 until yDimension+1){
        for(col in 0 until xDimension+1){
            if (points.contains(Pair(col,row)))
                print("#")
            else
                print(".")
        }
        println()
    }











}