import java.io.File
import java.math.BigInteger
import java.util.*

fun main() {
    val lines = File("data/day16_2.txt").readLines()
    //val lines = File("data/inputDay16.txt").readLines()



    lines.forEach { line->
        println(line)
        val binaryNum: Queue<Char> = LinkedList<Char>()
        // transform hex to binary
        line.forEach { binaryNum.addAll(it.toString().toInt(16).toString(2).padStart(4,'0').toCharArray().toList()) }
        println(binaryNum)

        val mainPacket = Packet(binaryNum)
        println(mainPacket)
        println(mainPacket.addVersionNumbers())
        println(mainPacket.process())

    }

}
class Packet(val binaryNum: Queue<Char>){

    val version: Int
    val type: Int
    val isLiteral: Boolean
    var literalValue = 0.toBigInteger()
    var subPackets = mutableListOf<Packet>()
    var packetSize = 0

    init {
        //println("Started new packet")
        version = read(3)
        type = read(3)
        isLiteral = type == 4
        if(isLiteral){
            processLiteral()
        }else{
            processOperation()
        }
    }

    fun processLiteral(){
        do {
            val group = read(5)
            literalValue = literalValue.multiply(16.toBigInteger())
            literalValue = literalValue.add((group % 16).toBigInteger())
        } while (group >= 16)
    }

    fun processOperation(){
        val lengthType = read(1)
        var bitLength = 0
        var subPacketNum = 0
        if(lengthType == 0){
            bitLength = read(15)
        }else{
            subPacketNum = read(11)
        }
        var totalPacketSize = 0
        var totalPacketsRead = 0
        if(bitLength == 0 && subPacketNum == 0)
            return
        while(totalPacketSize== 0 || totalPacketSize < bitLength || totalPacketsRead < subPacketNum){
            val newPacket = Packet(binaryNum)
            totalPacketsRead+=1
            totalPacketSize += newPacket.packetSize
            packetSize += newPacket.packetSize
            subPackets.add(newPacket)
            //println("Finished reading subpacket [$newPacket]. Count: $totalPacketsRead, Size: $totalPacketSize Aiming for: $bitLength,$subPacketNum")
        }


    }


    override fun toString(): String {
        var stringValue = "Version: $version, Type $type, "
        if(isLiteral){
            stringValue += "Value: $literalValue"
        }else{
            stringValue += subPackets
        }
        return stringValue
    }

    fun read(n: Int): Int{
        var result = ""
        for(i in 0 until n){
            result += binaryNum.poll()
        }
        packetSize += n
        return result.toInt(2)
    }

    fun addVersionNumbers(): Int{
        return version + subPackets.sumOf { it.addVersionNumbers() }
    }

    fun process(): BigInteger{
        return when(type){
            0 -> subPackets.sumOf { it.process() }
            1 -> subPackets.map{it.process()}.reduce { acc, i -> acc.multiply(i) }
            2 -> subPackets.minOf { it.process() }
            3 -> subPackets.maxOf { it.process() }
            4 -> literalValue
            5 -> if(subPackets[0].process() > subPackets[1].process()) 1.toBigInteger() else 0.toBigInteger()
            6 -> if(subPackets[0].process() < subPackets[1].process()) 1.toBigInteger() else 0.toBigInteger()
            7 -> if(subPackets[0].process().equals(subPackets[1].process())) 1.toBigInteger() else 0.toBigInteger()
            else -> 0.toBigInteger()
        }

    }
}






