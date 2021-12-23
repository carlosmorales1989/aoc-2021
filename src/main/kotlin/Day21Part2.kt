import java.io.File
import java.math.BigInteger

fun main() {
    //val lines = File("data/inputDay21.txt").readLines()
    val lines = File("data/day21.txt").readLines()

    val regex = Regex("Player [0-9] starting position: ([0-9]*)")
    var match = regex.find(lines[0])
    val positions = mutableListOf(0,0)
    positions[0] = match!!.destructured.toList().first().toInt()
    match = regex.find(lines[1])
    positions[1] = match!!.destructured.toList().first().toInt()
    val scores = mutableListOf(0,0)
    var current = 0


    var currentStates = mutableMapOf(DiracGameState(positions,scores) to 1.toBigInteger())
    val wins = arrayOf(0.toBigInteger(), 0.toBigInteger())
    while(currentStates.isNotEmpty())
    {
        val newStates = mutableMapOf<DiracGameState, BigInteger>()
        for(state in currentStates.keys){
            for(roll1 in 1..3){
                for(roll2 in 1..3){
                    for(roll3 in 1..3){
                        val newPositions = state.positions.toMutableList()
                        newPositions[current] = ((newPositions[current] + roll1 + roll2 + roll3 - 1)%10)+1
                        val newScores = state.scores.toMutableList()
                        newScores[current] += newPositions[current]
                        if(newScores[current] >= 21){
                            wins[current] = wins[current].add(currentStates[state])
                        }else{
                            val newState = DiracGameState(newPositions, newScores)
                            newStates[newState] = newStates.getOrDefault(newState, 0.toBigInteger()).add(currentStates[state])
                        }
                    }
                }
            }
        }
        currentStates = newStates
        current = (current+1)%2
    }
    println(wins.maxOf { it })
}

class DiracGameState(val positions: List<Int>,val scores: List<Int>){
    override fun hashCode(): Int {
        return positions[0]*positions[1]*scores[0]*scores[1]
    }

    override fun equals(other: Any?): Boolean {
        return other is DiracGameState && positions == other.positions && scores == other.scores
    }

    override fun toString(): String {
        return "Pos: ${positions.toList()} Scores:${scores.toList()}"
    }
}