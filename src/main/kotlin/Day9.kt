import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day9.txt").readLines()

    val heightMap = lines.map { it.toCharArray().map { it - '0'} }

    val lowPoints = mutableListOf<Pair<Int,Int>>()

    for(i in heightMap.indices){
        for(j in heightMap[i].indices){
            val center = Pair(i,j)
            val neighbours = getNeighbours(center, heightMap.size, heightMap[0].size)
            if (neighbours.all {heightMap[it.first][it.second] > heightMap[i][j]})
                lowPoints.add(center)
        }
    }

    println(lowPoints)
    println(lowPoints.sumOf { (heightMap[it.first][it.second] + 1) })

    val basinMap = Array(heightMap.size){Array(heightMap[0].size){-1} }
    val basinSizes = Array(lowPoints.size){ 0 }

    for(i in lowPoints.indices){
        val lowPoint = lowPoints[i]
        val nextPoints: Queue<Pair<Int,Int>> = LinkedList()
        nextPoints.add(lowPoint)
        while(!nextPoints.isEmpty()){
            val nextPoint = nextPoints.poll()
            if(basinMap[nextPoint.first][nextPoint.second] != -1) continue
            basinMap[nextPoint.first][nextPoint.second] = i
            basinSizes[i] += 1
            nextPoints.addAll(getNeighbours(nextPoint, heightMap.size, heightMap[0].size)
                .filter { heightMap[it.first][it.second] < 9 })
        }
    }
    println(basinMap)
    basinSizes.sortDescending()
    println(basinSizes.take(3).reduce { acc, i -> acc*i })
}

fun getNeighbours(center: Pair<Int,Int> , height: Int, width: Int): List<Pair<Int,Int>> {
    return listOf(Pair(center.first-1,center.second),
        Pair(center.first+1,center.second),
        Pair(center.first,center.second-1),
        Pair(center.first,center.second+1))
        .filter { it.first in 0 until height && it.second in 0 until width }
}

