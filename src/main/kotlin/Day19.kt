import java.io.File
import java.lang.Integer.max
import java.util.*
import kotlin.math.abs

fun main() {
    //val lines = File("data/inputDay19.txt").readLines()
    val lines = File("data/day19.txt").readLines()

    val iterator = lines.iterator()
    val scanners = mutableSetOf<BeaconScanner>()
    var currentScanner: BeaconScanner? = null
    while(iterator.hasNext()){
        val line = iterator.next()
        val regex = Regex("--- scanner ([0-9]*) ---")
        if(regex.matches(line)){
            val match = regex.find(line)
            val id:Int = match!!.destructured.toList().first().toInt()
            currentScanner = BeaconScanner(id)
            scanners.add(currentScanner)
        }else if(line.isNotEmpty()){
            val info = line.split(",").map{it.toInt()}
            currentScanner?.readings?.add(BeaconReading(info[0], info[1], info[2]))
        }
    }

    val rotations = calculateRotations()

    val unmatchedScanners = scanners.toMutableList()

    val checkedCombinations = mutableSetOf<Pair<Int,Int>>()

    currentScanner = scanners.first()
    val adjustedScanners = mutableListOf(currentScanner)
    unmatchedScanners.remove(currentScanner)
    val offsets = mutableListOf<BeaconReading>(BeaconReading(0,0,0))
    while(unmatchedScanners.isNotEmpty()){
        val newMatchedScanners = mutableListOf<BeaconScanner>()
        scannerLoop@ for(unmatchedScanner in unmatchedScanners){
            for(adjustedScanner in adjustedScanners){
                if(Pair(unmatchedScanner.id, adjustedScanner.id) in checkedCombinations){
                    continue
                }
                for(unmatchedReading in unmatchedScanner.readings){
                    for(rotation in rotations){
                        val unmatchedRotatedReading = unmatchedReading.rotate(rotation)
                        for(adjustedReading in adjustedScanner.readings){
                            val offset = unmatchedRotatedReading.substract(adjustedReading)
                            if(unmatchedScanner.countCommonPoints(adjustedScanner, offset, rotation)>=12){
                                unmatchedScanner.adjust(rotation,offset)
                                newMatchedScanners.add(unmatchedScanner)
                                offsets.add(offset)
                                continue@scannerLoop
                            }
                        }
                    }
                }
                checkedCombinations.add(Pair(unmatchedScanner.id, adjustedScanner.id))
                checkedCombinations.add(Pair(adjustedScanner.id, unmatchedScanner.id))
            }
        }
        newMatchedScanners.forEach {
            unmatchedScanners.remove(it)
            adjustedScanners.add(it)
        }
        println("Adjusted scanners: ${adjustedScanners.size}")
    }

    val beacons = adjustedScanners.flatMap { it.readings }.toSet()
    println(beacons.size)

    var maxDistance = 0
    for(scanner in offsets) {
        for (scanner2 in offsets) {
            val distance = abs(scanner.x - scanner2.x) + abs(scanner.y - scanner2.y) + abs(scanner.z - scanner2.z)
            maxDistance = max(maxDistance, distance)
        }
    }
    println(maxDistance)

}

fun calculateRotations(): List<List<String>>{
    val neg = mutableMapOf(
        "x" to "-x",
        "-x" to "x",
        "y" to "-y",
        "-y" to "y",
        "z" to "-z",
        "-z" to "z")
    var rots = mutableSetOf(listOf("x","y","z"))
    for(i in 1..4){
        val update = mutableSetOf<List<String>>()
        for(rot in rots){
            val (a,b,c) = rot
            update.add(listOf(a,b,c))
            update.add(listOf(a,c,neg.getOrDefault(b,b)))
            update.add(listOf(neg.getOrDefault(c,c),b,a))
            update.add(listOf(b,neg.getOrDefault(a,a),c))
        }
        rots = update
    }
    println(rots.size)
    println(rots.toList())
    return rots.toList()
}

class BeaconScanner(val id: Int){
    var readings = mutableListOf<BeaconReading>()

    fun adjust(rotation: List<String>, offset: BeaconReading){
        readings = readings.map { it.rotate(rotation).substract(offset) }.toMutableList()
    }

    override fun toString(): String {
        return "Scanner:$id"
    }

    override fun equals(other: Any?): Boolean {
        return other is BeaconScanner && id.equals(other.id)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
    fun countCommonPoints(scanner: BeaconScanner, offset: BeaconReading, rotation: List<String>): Int{
        var counter = 0
        for(reading in readings){
            val offsetReading = reading.rotate(rotation).substract(offset)
            if(offsetReading in scanner.readings){
                counter += 1
            }
        }
        return counter
    }
}

class BeaconReading(var x:Int, var y:Int, var z:Int){

    override fun toString(): String {
        return "$x,$y,$z"
    }

    fun substract(paramReading: BeaconReading): BeaconReading{
        return BeaconReading(x - paramReading.x,
            y- paramReading.y,
            z - paramReading.z)
    }

    fun add(paramReading: BeaconReading): BeaconReading{
        return BeaconReading(x + paramReading.x,
            y + paramReading.y,
            z + paramReading.z)
    }

    fun rotate(rotation: List<String>): BeaconReading{
        return BeaconReading(valueOf(rotation[0]),
            valueOf(rotation[1]),
            valueOf(rotation[2]))
    }

    fun valueOf(name: String): Int{
        when(name){
            "x" -> return x
            "y" -> return y
            "z" -> return z
            "-x" -> return -x
            "-y" -> return -y
            "-z" -> return -z
            else -> throw Exception("WTH is $name?")
        }
    }
    override fun equals(other: Any?): Boolean {
        return other is BeaconReading && x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        return "$x,$y,$z".hashCode()
    }

}