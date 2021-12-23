import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    /*val startingRoomPositionsTest = listOf(
        RoomAmphipodPosition('B','A',0),

        RoomAmphipodPosition('A','A',1),
        RoomAmphipodPosition('C','B',0),

        RoomAmphipodPosition('D','B',1),
        RoomAmphipodPosition('B','C',0),

        RoomAmphipodPosition('C','C',1),
        RoomAmphipodPosition('D','D',0),

        RoomAmphipodPosition('A','D',1),
    )*/
    /*val startingRoomPositionsTest = listOf(
        RoomAmphipodPosition('B','A',0),
        RoomAmphipodPosition('D','A',1),
        RoomAmphipodPosition('D','A',2),
        RoomAmphipodPosition('A','A',3),
        RoomAmphipodPosition('C','B',0),
        RoomAmphipodPosition('C','B',1),
        RoomAmphipodPosition('B','B',2),
        RoomAmphipodPosition('D','B',3),
        RoomAmphipodPosition('B','C',0),
        RoomAmphipodPosition('B','C',1),
        RoomAmphipodPosition('A','C',2),
        RoomAmphipodPosition('C','C',3),
        RoomAmphipodPosition('D','D',0),
        RoomAmphipodPosition('A','D',1),
        RoomAmphipodPosition('C','D',2),
        RoomAmphipodPosition('A','D',3),
    )*/
    val startingRoomPositions = listOf(
        RoomAmphipodPosition('A','A',0),
        RoomAmphipodPosition('D','A',1),
        RoomAmphipodPosition('D','A',2),
        RoomAmphipodPosition('B','A',3),
        RoomAmphipodPosition('D','B',0),
        RoomAmphipodPosition('C','B',1),
        RoomAmphipodPosition('B','B',2),
        RoomAmphipodPosition('C','B',3),
        RoomAmphipodPosition('A','C',0),
        RoomAmphipodPosition('B','C',1),
        RoomAmphipodPosition('A','C',2),
        RoomAmphipodPosition('D','C',3),
        RoomAmphipodPosition('B','D',0),
        RoomAmphipodPosition('A','D',1),
        RoomAmphipodPosition('C','D',2),
        RoomAmphipodPosition('C','D',3),
    )


    val startingState = BurrowState(startingRoomPositions, listOf<HallwayAmphipodPosition>())

    val compareByEnergy: Comparator<BurrowState> = compareBy { it.energy + it.heuristic}

    val toVisit = PriorityQueue(compareByEnergy)

    var currentState: BurrowState? = startingState

    while(currentState!= null && !currentState.isDone()){
        //println(currentState)
        println(toVisit.size)
        toVisit.addAll(currentState.generateStates())
        currentState = toVisit.poll()
    }
    println(currentState)
}
fun getEnergyPerAmphiphod(id: Int): Int{
    return when(id){
        0 -> 1
        1 -> 10
        2 -> 100
        3 -> 1000
        else -> 0
    }
}

fun isBanned(pos:Int): Boolean{
    return pos==2 || pos==4 || pos==6 || pos==8
}

class RoomAmphipodPosition(val amphiphodId: Char, val roomId: Char, val position: Int)
class HallwayAmphipodPosition(val amphiphodId: Char, val position: Int)

class BurrowState(){

    var hallway:Array<Int> = Array(11){-1}

    var rooms = Array(4){Array(4){-1} }

    var energy = 0

    var heuristic = 0

    fun calculateHeuristic(){
        heuristic = 0
        rooms.forEachIndexed { index, ints ->
            ints.forEachIndexed { index2, i ->
                if(i != -1 && i != index)
                    heuristic += (abs(i-index)*2 + index2 + 1)*getEnergyPerAmphiphod(i)
            }
        }
        hallway.forEachIndexed { index, i ->
            if(i != -1){
                heuristic +=  abs(index - roomToPos(i))*getEnergyPerAmphiphod(i)
            }
        }
    }

    constructor(roomPositions: List<RoomAmphipodPosition>,
                hallwayPositions: List<HallwayAmphipodPosition>) : this() {
        roomPositions.forEach {
            rooms[it.roomId-'A'][it.position] = it.amphiphodId-'A'
        }
        hallwayPositions.forEach {
            hallway[it.position] = it.amphiphodId-'A'
        }
    }

    constructor(pHallway:Array<Int>, pRooms:Array<Array<Int>>): this(){
        hallway = pHallway.clone()
        rooms = pRooms.map { it.clone() }.toTypedArray()
    }

    fun clone(): BurrowState{
        return BurrowState(hallway, rooms)
    }

    fun isDone():Boolean{
        return rooms.mapIndexed{index, room->
            room.all { it == index }
        }.all{ it }
    }

    override fun toString(): String {
        return "Hallway: ${hallway.joinToString("|")}\n Rooms: ${rooms.map{it.toList()}.toList()}\nEnergy:$energy\n"
    }

    fun generateStates(): List<BurrowState>{
        val nextStates = mutableListOf<BurrowState>()

        // States from hallways to rooms
        for(hallwayPosition in hallway.indices){
            // Position is empty, no movement required
            if(hallway[hallwayPosition]==-1)
                continue

            val expectedRoomPos = roomToPos(hallway[hallwayPosition])

            val subHallway = if(expectedRoomPos > hallwayPosition)
                hallway.slice(hallwayPosition+1 .. expectedRoomPos)
            else
                hallway.slice(expectedRoomPos until hallwayPosition)
            // Path is blocked
            if(!subHallway.all { it == -1 })
                continue

            val expectedRoomNumber = hallway[hallwayPosition]
            val room = rooms[expectedRoomNumber]

            // Room has something that doesn't belong. I can't add anything there
            if(room.any{ it != -1 && it != expectedRoomNumber})
                continue

            var expectedPosInRoom = room.size-1
            while(expectedPosInRoom >0 && room[expectedPosInRoom] != -1){
                expectedPosInRoom--
            }

            val nextState = clone()
            nextState.hallway[hallwayPosition] = -1
            nextState.rooms[expectedRoomNumber][expectedPosInRoom] = hallway[hallwayPosition]
            nextState.energy = energy + getEnergyPerAmphiphod(hallway[hallwayPosition])*(abs(hallwayPosition - expectedRoomPos)+1+expectedPosInRoom)
            nextState.calculateHeuristic()
            nextStates.add(nextState)
        }

        // States from rooms to hallways
        for (roomIndex in rooms.indices){
            val room = rooms[roomIndex]
            if(room.all { it == roomIndex || it == -1 }){
                // Room is correctly allocated (even if partially)
                continue
            }

            var counter: Int
            val limit = 4

            var posInsideRoom = 0
            while(posInsideRoom < room.size && room[posInsideRoom] == -1)
                posInsideRoom+=1

            if(posInsideRoom < room.size){
                counter = 0
                // Left
                for (i in roomToPos(roomIndex) downTo 0) {
                    if(isBanned(i))
                        continue
                    if (!moveFromRoomToHallway(i, nextStates, roomIndex, posInsideRoom))
                        break
                    if (++counter >= limit)
                        break
                }
                counter = 0
                // Right
                for (i in roomToPos(roomIndex) until 11) {
                    if(isBanned(i))
                        continue
                    if (!moveFromRoomToHallway(i, nextStates, roomIndex, posInsideRoom))
                        break
                    if (++counter >= limit)
                        break
                }
                //}
            }
        }

        return nextStates
    }

    fun moveFromRoomToHallway(hallwayPos: Int, nextStates: MutableList<BurrowState>, roomIndex: Int, posInsideRoom: Int): Boolean{

        if(hallway[hallwayPos]!=-1)
            return false

        val newState = clone()
        newState.rooms[roomIndex][posInsideRoom] = -1
        newState.hallway[hallwayPos] = rooms[roomIndex][posInsideRoom]
        newState.energy = energy + getEnergyPerAmphiphod(rooms[roomIndex][posInsideRoom])*(abs(hallwayPos - roomToPos(roomIndex)) + 1 + posInsideRoom)
        newState.calculateHeuristic()
        nextStates.add(newState)

        return true
    }

}

fun roomToPos(roomId: Int): Int{
    return 2*(roomId)+2
}