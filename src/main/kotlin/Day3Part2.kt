import java.io.File

fun main() {
    val lines = File("data/inputDay3.txt").readLines()

    val lifeSupport = findRating(lines.toList(), 0, true)
    val co2Scrubber = findRating(lines.toList(), 0, false)

    println(lifeSupport)
    println(co2Scrubber)
    print(lifeSupport*co2Scrubber)

}

fun findRating(list: List<String>, index: Int, mostCommon: Boolean): Int
{
    if (index > list[0].length){
        return -1
    }
    val onesList: MutableList<String> = mutableListOf<String>()
    val zerosList: MutableList<String> = mutableListOf<String>()

    println("List: " + list)

    for (value in list){
        if (value[index] == '1'){
            onesList.add(value)
        } else {
            zerosList.add(value)
        }
    }

    println("0List: " + zerosList)
    println("1List: " + onesList)

    val mostCommonList: MutableList<String>
    val leastCommonList: MutableList<String>
    println(onesList.size.toString() + " vs " + (list.size/2).toString())
    if (onesList.size >= list.size/2.0) {
        mostCommonList = onesList
        leastCommonList = zerosList
    } else {
        leastCommonList = onesList
        mostCommonList = zerosList
    }

    println("MCList: " + mostCommonList)
    println("LCList: " + leastCommonList)

    val chosenList: MutableList<String>

    if (mostCommon) {
        chosenList = mostCommonList
    } else {
        chosenList = leastCommonList
    }

    println("ChosenList: " + chosenList)

    if (chosenList.size == 1){
        return chosenList[0].toInt(2)
    } else {
        return findRating(chosenList, index + 1, mostCommon)
    }

}