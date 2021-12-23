import java.io.File
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    //val lines = File("data/inputDay22_3.txt").readLines()
    val lines = File("data/day22.txt").readLines()

    val regex = Regex("(on|off) x=([-0-9]*)..([-0-9]*),y=([-0-9]*)..([-0-9]*),z=([-0-9]*)..([-0-9]*)")

    val positiveBoxes = mutableListOf<BoundingBox>()
    val negativeBoxes = mutableListOf<BoundingBox>()

    for(line in lines){
        val match = regex.find(line)
        println(line)
        val (status, sMinX, sMaxX, sMinY, sMaxY, sMinZ, sMaxZ) = match!!.destructured.toList()

        val mins = listOf(sMinX, sMinY, sMinZ).map { it.toInt() }
        val maxs = listOf(sMaxX, sMaxY, sMaxZ).map { it.toInt() }

        val box = BoundingBox(mins,maxs)
        val newPositiveBoxes = mutableListOf<BoundingBox>()
        val newNegativeBoxes = mutableListOf<BoundingBox>()
        if(status == "on"){
            positiveBoxes.forEach {
                val intersection = box.getIntersection(it)
                if(intersection != null){
                    newNegativeBoxes.add(intersection)
                }
            }
            negativeBoxes.forEach {
                val intersection = box.getIntersection(it)
                if(intersection != null){
                    newPositiveBoxes.add(intersection)
                }
            }
            newPositiveBoxes.add(box)
        }else{
            positiveBoxes.forEach {
                val intersection = box.getIntersection(it)
                if(intersection != null){
                    newNegativeBoxes.add(intersection)
                }
            }
            negativeBoxes.forEach {
                val intersection = box.getIntersection(it)
                if(intersection != null){
                    newPositiveBoxes.add(intersection)
                }
            }
        }
        positiveBoxes.addAll(newPositiveBoxes)
        negativeBoxes.addAll(newNegativeBoxes)
        //println("After $box")
        //println(positiveBoxes)
        //println(negativeBoxes)
        val positiveBounds = positiveBoxes.map { it.getVolume() }.reduce{acc,value -> acc.add(value)}
        var negativeBounds = 0.toBigInteger()
        if(negativeBoxes.isNotEmpty())
            negativeBounds = negativeBoxes.map { it.getVolume() }.reduce{acc,value -> acc.add(value)}
        //println(positiveBounds)
        //println(negativeBounds)
        println(positiveBounds.subtract(negativeBounds))
    }


}

class BoundingBox(val mins: List<Int>,val maxs: List<Int>){

    override fun toString(): String {
        return "$mins<>$maxs"
    }

    fun getVolume(): BigInteger {
        return maxs.mapIndexed{i,maxVal ->
            (abs(maxVal-mins[i])+1).toBigInteger()
        }.reduce{acc, value ->
            acc.multiply(value)
        }
    }

    fun getIntersection(box: BoundingBox): BoundingBox?{
        val intersectionMins = mins.mapIndexed{i, minVal->
            max(minVal,box.mins[i])
        }
        val intersectionMaxs = maxs.mapIndexed{i, maxVal->
            min(maxVal,box.maxs[i])
        }
        if (intersectionMaxs.mapIndexed { index, i -> i >= intersectionMins[index] }.any { !it }){
            return null
        }
        return BoundingBox(intersectionMins, intersectionMaxs)
    }
}