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
        if(currentPoint.first == riskMap.size-1 && currentPoint.second == riskMap[0].size-1){
            found = currentPath
        }else{
            visited.add(currentPoint)
            getNeighbours(currentPoint,riskMap.size,riskMap[0].size).forEach{
                if(!visited.contains(it)) {
                    val nextPointList = currentPath.listPoints.toMutableList()
                    nextPointList.add(it)
                    toVisit.add(Path(nextPointList, currentPath.risk + riskMap[it.first][it.second]))
                }
            }
        }
    }
    println(counter)
    println(found.listPoints)
    println(found.risk)

}
