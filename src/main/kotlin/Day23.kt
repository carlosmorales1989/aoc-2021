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
    val startingRoomPositions = listOf(
        RoomAmphipodPosition('A','A',0),
        RoomAmphipodPosition('B','A',1),
        RoomAmphipodPosition('D','B',0),
        RoomAmphipodPosition('C','B',1),
        RoomAmphipodPosition('A','C',0),
        RoomAmphipodPosition('D','C',1),
        RoomAmphipodPosition('B','D',0),
        RoomAmphipodPosition('C','D',1),
    )

    val startingState = BurrowState(startingRoomPositions, listOf<HallwayAmphipodPosition>())

    val compareByEnergy: Comparator<BurrowState> = compareBy { it.energy + it.heuristic}

    val toVisit = PriorityQueue(compareByEnergy)

    var currentState: BurrowState? = startingState

    var reps = 0

    while(currentState!= null && !currentState.isDone()){
        //println(currentState)
        //println(toVisit.size)
        toVisit.addAll(currentState.generateStates())
        currentState = toVisit.poll()
        if(reps++==10){
            println(toVisit)
        }
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

    var rooms = Array(4){Array(2){-1} }

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
            var expectedPosInRoom: Int

            if(rooms[expectedRoomNumber][1] == -1 && rooms[expectedRoomNumber][0] == -1){
                expectedPosInRoom = 1
            }else if(rooms[expectedRoomNumber][1] == hallway[hallwayPosition] && rooms[expectedRoomNumber][0] == -1){
                expectedPosInRoom = 0
            }else{
                //Room is blocked
                continue
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
            if(room.all { it == roomIndex }){
                // Room is correctly allocated
                continue
            }

            var counter: Int
            val limit = 3
            // Outermost position in room
            if(room[0] != -1){
                //if(room[0] < roomIndex) {
                counter = 0
                    // Left
                    for (i in roomToPos(roomIndex) downTo 0) {
                        if(isBanned(i))
                            continue
                        if (!moveFromRoomToHallway(i, nextStates, roomIndex, 0))
                            break
                        if (++counter >= limit)
                            break
                    }
                //}else {
                counter = 0
                    // Right
                    for (i in roomToPos(roomIndex) until 11) {
                        if(isBanned(i))
                            continue
                        if (!moveFromRoomToHallway(i, nextStates, roomIndex, 0))
                            break
                        if (++counter >= limit)
                            break
                    }
                //}
            }

            // Innermost position in room
            if(room[1] != -1 && room[0] == -1 && room[1] != roomIndex){
                counter = 0
                //if(room[1] < roomIndex) {
                    for (i in roomToPos(roomIndex) downTo 0) {
                        if(isBanned(i))
                            continue
                        if (!moveFromRoomToHallway(i, nextStates, roomIndex, 1))
                            break
                        if (++counter >= limit)
                            break
                    }
                counter = 0
                //}else {
                    for (i in roomToPos(roomIndex) until 11) {
                        if(isBanned(i))
                            continue
                        if (!moveFromRoomToHallway(i, nextStates, roomIndex, 1))
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