import java.io.File
import java.lang.Integer.max

fun main() {
    val lines = File("data/day14.txt").readLines()

    val firstElement = LinkedListNode(lines[0][0].toString())
    var lastElement = firstElement
    for(i in 1 until lines[0].length){
        val newElement = LinkedListNode(lines[0][i].toString())
        lastElement.next = newElement
        lastElement = newElement
    }

    val sequenceMap = lines.filter { it.contains("->") }
        .map{ it.split(" -> ")}
        .map{ it[0] to it[1]}
        .toMap()

    val steps = 40
    for(step in 1 until steps+1){
        println("Processing step $step")
        var currentElement = firstElement
        while(currentElement.next != null ){
            val nextElement = currentElement.next
            val newElement = LinkedListNode<String>(sequenceMap.getOrDefault(currentElement.value + nextElement!!.value){"-"}.toString())
            currentElement.next = newElement
            newElement.next = nextElement
            currentElement = nextElement
        }
    }
    var result = String()
    var currentElement:LinkedListNode<String>? = firstElement
    while(currentElement != null){
        result += currentElement.value
        currentElement = currentElement.next
    }

    val occurenceMap = result.groupingBy { it }.eachCount()

    println(occurenceMap.maxOf { it.value } - occurenceMap.minOf { it.value })


}

class LinkedListNode<T>(val value:T, var next: LinkedListNode<T>? = null){

    override fun toString():String{
        return value.toString()+ (next?.toString()?:"")
    }

}