import java.io.File
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val lines = File("data/day8.txt").readLines()

    val entries = lines.map { Entry(it) }


    entries.forEach {
        println(it.sumOutputs())
    }

    println(entries.map { it.sumOutputs() }.sum())
}

class Entry(entryString: String)
{

    //acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
    //cdfeb fcadb cdfeb cdbaf
    val signalToNum: MutableMap<String, Int>
    val numToSignal: Array<String>

    val outputSignals: Array<String>

    init {
        val info = entryString.split("|")
        val uniqueSignalPatterns = info[0].split(" ").toTypedArray()
        outputSignals = info[1].split(" ").toTypedArray()
        numToSignal = Array(10) { "--" }
        signalToNum = mutableMapOf()

        val candidateMap = Array(8){ mutableListOf<String>()}

        uniqueSignalPatterns.forEach {
            candidateMap[it.length].add(it)
        }

        println(candidateMap.toList())

        storeSignal((candidateMap[2])[0],1)
        storeSignal((candidateMap[3])[0],7)
        storeSignal((candidateMap[4])[0],4)
        storeSignal((candidateMap[7])[0],8)

        candidateMap[6].forEach{
            if (isContained(numToSignal[4], it)){
                storeSignal(it, 9)
            }
        }
        candidateMap[6].removeIf { it.equals(numToSignal[9]) }

        candidateMap[5].forEach{
            if (isContained(numToSignal[1], it)){
                storeSignal(it, 3)
            }
        }
        candidateMap[5].removeIf { it.equals(numToSignal[3]) }

        candidateMap[6].forEach{
            if (isContained(numToSignal[1], it)){
                storeSignal(it, 0)
            }
        }
        candidateMap[6].removeIf { it.equals(numToSignal[0]) }

        storeSignal((candidateMap[6])[0],6)

        candidateMap[5].forEach{
            if (isContained(it, numToSignal[6])){
                storeSignal(it, 5)
            }
        }
        candidateMap[5].removeIf { it.equals(numToSignal[5]) }

        storeSignal((candidateMap[5])[0],2)

        println(signalToNum)
        println(numToSignal.toList())
        println("--")
    }

    fun isContained(signal1: String, signal2: String): Boolean {
        return signal1.all { signal2.contains(it) }
    }

    fun storeSignal(signal:String, num: Int){
        signalToNum[ signal.toSortedSet().joinToString("")] = num
        numToSignal[num] = signal
    }

    fun sumOutputs(): Int{
        var result = 0
        outputSignals.mapNotNull { signalToNum[it.toSortedSet().joinToString("")] }
            .forEach{
                result *= 10
                result += it
            }
        return result
    }

}
