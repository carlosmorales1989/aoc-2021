import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day15.txt").readLines()

    val riskMap = lines.map { it.toCharArray().map { it - '0'} }

    val visited = mutableSetOf(Pair(0,0))

    val compareByRisk: Comparator<Path> = compareBy { it.risk }

    val toVisit = PriorityQueue<Path>(compareByRisk)

    val minPathMap = mutableMapOf<Pair<Int,Int>,Int>()

    toVisit.add(Path(mutableListOf(Pair(0,0)),0))
    var found: Path? = null
    var counter = 0
    while(found == null){
        counter++
        if(counter%100000 ==0){
            print("Currently at $counter\n")
        }
        val currentPath = toVisit.poll()
        val currentPoint = currentPath.listPoints.last()
        if(currentPoint.first == 5*riskMap.size-1 && currentPoint.second == 5*riskMap[0].size-1){
            found = currentPath
        }else{
            visited.add(currentPoint)
            getNeighbours(currentPoint,5*riskMap.size,5*riskMap[0].size).forEach{
                if(!visited.contains(it)) {
                    var currentRisk = riskMap[it.first % riskMap.size][it.second % riskMap[0].size]
                    currentRisk = currentRisk + (it.first / riskMap.size) + (it.second / riskMap[0].size)
                    if(currentRisk>9) currentRisk-=9
                    val nextPointList = currentPath.listPoints.toMutableList()
                    val lastSeenCost = minPathMap.getOrDefault(it, 1000000)
                    if(currentPath.risk + currentRisk < lastSeenCost) {
                        nextPointList.add(it)
                        toVisit.add(Path(nextPointList, currentPath.risk + currentRisk))
                        minPathMap[it] = currentPath.risk + currentRisk
                    }
                }
            }
        }
    }
    println(counter)
    println(found.listPoints)
    println(found.risk)

}

class Path(val listPoints:List<Pair<Int,Int>>, val risk: Int)