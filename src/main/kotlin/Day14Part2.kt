import java.io.File
import java.lang.Integer.max
import java.math.BigInteger

fun main() {
    val lines = File("data/day14.txt").readLines()

    val sequenceMap = lines.filter { it.contains("->") }
        .map{ it.split(" -> ")}
        .map{ it[0] to it[1]}
        .toMap()


    var pairMap = sequenceMap.map { it.key to 0.toBigInteger() }.toMap().toMutableMap()


    for(i in 0 until lines[0].length-1){
        val pair = lines[0][i].toString()+lines[0][i+1].toString()
        val count = pairMap.getOrDefault(pair,0.toBigInteger())
        pairMap[pair] = count.add(1.toBigInteger())
    }




    val steps = 40
    for(step in 1 until steps+1){
        val newPairMap = sequenceMap.map { it.key to 0.toBigInteger() }.toMap().toMutableMap()
        pairMap.forEach{ pair->
            val newElement = sequenceMap.getOrDefault(pair.key,"")
            val pairs = mutableListOf(pair.key[0].toString() + newElement,newElement + pair.key[1].toString())
            //println("For ${pair.key}: $pairs")
            pairs.forEach{
                val count = newPairMap.getOrDefault(it,0.toBigInteger())
                newPairMap[it] = count.add(pair.value)
            }
        }
        pairMap = newPairMap
        println("For step $step(length: ${pairMap.values.reduce{acc,it -> acc.add(it)}.times(2.toBigInteger())}): $pairMap")
    }
    val occurrenceMap = mutableMapOf<Char, BigInteger>()

    pairMap.forEach{ pair ->
        println(pair)
        pair.key.forEach {
            val count = occurrenceMap.getOrDefault(it, 0.toBigInteger() )
            occurrenceMap[it] = count.add(pair.value)
        }
        println(occurrenceMap)
    }
    occurrenceMap[lines[0].first()] = occurrenceMap[lines[0].first()]!!.add(1.toBigInteger())
    occurrenceMap[lines[0].last()] = occurrenceMap[lines[0].last()]!!.add(1.toBigInteger())
    print(occurrenceMap)

    println(occurrenceMap.maxOf { it.value.divide(2.toBigInteger()) } - occurrenceMap.minOf { it.value.divide(2.toBigInteger()) })
}
