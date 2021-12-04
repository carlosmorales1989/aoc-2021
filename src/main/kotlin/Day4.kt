import java.io.File

fun main() {
    val lines = File("data/day4.txt").readLines()

    val calledNumbers = lines[0].split(",").map { it.toInt() }

    var index = 1

    val boards = mutableListOf<Board>()

    while (index < lines.size){
        index++
        val matrixLines = Array(5){""}
        for(i in 0 until 5){
            matrixLines[i] = lines[index++]
            println("::"+matrixLines[i])
        }

        boards.add(Board(matrixLines))
    }

    var winnerCount = 0

    for( number in calledNumbers){
        for (board in boards ){
            if(board.winningPosition == -1 && board.markNumber(number)) {
                val unmarkedSum = board.sumUnmarkedNumbers()
                println("Unmarked sum: $unmarkedSum")
                println("Winner: $number")
                println(unmarkedSum*number)
                board.winningPosition = ++winnerCount
            }
        }
    }

}

class Number (var value: Int){
    var marked = false
}

class Board (lines: Array<String>) {
    val matrix: Array<Array<Number>> = Array(5){ emptyArray() }
    val numberMap: MutableMap<Int, Pair<Int,Int>> = mutableMapOf()
    var rowCounts = Array(5 ){0}
    var colCounts = Array(5 ){0}
    val diagCounts = Array(2){0}
    var winningPosition = -1

    init {
        for(inputRow in lines.indices) {
            matrix[inputRow] = lines[inputRow].split(" ")
                .filter { it.isNotEmpty() }
                .map{ Number(it.toInt()) }
                .toTypedArray()
            matrix.forEachIndexed{rowNumber, row ->
                row.forEachIndexed { colNumber, number ->
                    numberMap[number.value] = Pair(rowNumber, colNumber)
                }
            }
        }
    }

    fun isComplete(): Boolean{
        return rowCounts.any{ it == 5} ||
                colCounts.any{ it == 5} ||
                diagCounts.any{ it == 5}
    }

    fun markNumber(number: Int): Boolean{
        val position = numberMap[number] ?: return false
        matrix[position.first][position.second].marked = true
        rowCounts[position.first]+=1
        colCounts[position.second]+=1
        if (position.first == position. second){
            diagCounts[0]+=1
        }
        if ((5-position.first) == position.second){
            diagCounts[1]+=1
        }
        return isComplete()
    }

    fun sumUnmarkedNumbers (): Int{
        return matrix.flatMap { it.asIterable() }
            .filter {!it.marked}
            .map {it.value}
            .sum()
    }

    fun printMatrix(){
        matrix.forEach {
            it.forEach { print("$it ") }
            println()
        }
    }


}