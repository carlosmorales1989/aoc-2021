import java.io.File

fun main() {
    //val lines = File("data/inputDay21.txt").readLines()
    val lines = File("data/day21.txt").readLines()

    val regex = Regex("Player [0-9] starting position: ([0-9]*)")
    var match = regex.find(lines[0])
    val positions = arrayOf(0,0)
    positions[0] = match!!.destructured.toList().first().toInt()
    match = regex.find(lines[1])
    positions[1] = match!!.destructured.toList().first().toInt()
    val scores = arrayOf(0,0)
    var rounds = 0
    var current = 0
    var lastDie = 0
    while(scores.all { it < 1000 }){
        var currentDie = 0
        for(i in 1..3){
            currentDie += ((lastDie++)%100)+1
        }

        positions[current] = ((positions[current] + currentDie-1)%10)+1
        scores[current]+=positions[current]
        println("Player ${current+1} rolls $currentDie and moves to space ${positions[current]} for a total score of ${scores[current]}")
        current = (current+1)%2
        rounds+=1
    }
    println("$rounds,${scores.minOf { it }}")
    println(rounds * scores.minOf { it }*3)

}
