import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day12.txt").readLines()

    val caveMap = mutableMapOf<String,Cave>()

    lines.forEach {
        val info = it.split("-")
        val origin = caveMap.getOrPut(info[0]){Cave(info[0])}
        val destination = caveMap.getOrPut(info[1]){Cave(info[1])}
        origin.caves.add(destination)
        destination.caves.add(origin)
    }

    val result = visitCave(caveMap.getOrDefault("start") {Cave("start")} as Cave, mutableListOf())
    println(result.size)
    val resultB = visitCaveWithSingleRepeat(caveMap.getOrDefault("start") {Cave("start")} as Cave, mutableListOf(), false)
    println(resultB.size)
}

fun visitCaveWithSingleRepeat(cave: Cave, visited: List<Cave>, repeated: Boolean): List<List<Cave>>{
    //println("visiting: " + cave.id)
    val path = visited.toMutableList()

    path.add(cave)

    if(cave.id == "end") return mutableListOf(path)

    return cave.caves.filter { it.canBeRevisited || !visited.contains(it) || (!repeated && it.id != "start") }
        .flatMap { visitCaveWithSingleRepeat(it, path, repeated || (!it.canBeRevisited && visited.contains(it))) }
}

fun visitCave(cave: Cave, visited: List<Cave>): List<List<Cave>>{
    //println("visiting: " + cave.id)
    val path = visited.toMutableList()

    path.add(cave)

    if(cave.id == "end") return mutableListOf(path)

    return cave.caves.filter { it.canBeRevisited || !visited.contains(it) }.flatMap { visitCave(it, path) }
}

class Cave(val id:String){
    val caves = mutableListOf<Cave>()

    val canBeRevisited = !id.all {it.isLowerCase()}

    override fun toString():String{
        return id
    }
}

