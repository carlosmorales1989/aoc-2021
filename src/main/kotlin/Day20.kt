import java.io.File

fun main() {
    //val lines = File("data/inputDay20.txt").readLines()
    val lines = File("data/day20.txt").readLines()

    val imageEnhancingAlgorithm = lines[0]

    var inputImage = mutableMapOf<Pair<Int,Int>,Char>()
    var minRow = 0
    var minCol = 0
    var maxRow = lines.size-2
    var maxCol = lines[2].length
    for(i in 2  until lines.size){
        val row = i-2
        lines[i].forEachIndexed() {index,char ->
            inputImage[Pair(row,index)]= char
        }
    }
    println(inputImage)


    for(iteration in 1..50){
        val newImage = mutableMapOf<Pair<Int,Int>,Char>()
        val default = if(iteration % 2 == 0) imageEnhancingAlgorithm[0] else '.'
        for(row in minRow-2 until maxRow+2){
            for(col in minCol-2 until maxCol+2){
                val newChar = imageEnhancingAlgorithm[getImageEnhancementInput(Pair(row,col), inputImage, default)]
                newImage[Pair(row,col)] = newChar
            }
        }
        minRow-=2
        maxRow+=2
        minCol-=2
        maxCol+=2
        println("After iteration $iteration there are ${newImage.count { entry-> entry.value == '#' }}")
        inputImage = newImage
    }



}

fun getImageEnhancementInput(center: Pair<Int,Int>, image: Map<Pair<Int,Int>,Char>, default: Char): Int{
    val result = mutableListOf<Char>()
    for(row in center.first-1..center.first+1){
        for(col in center.second-1..center.second+1){
            result.add(image.getOrDefault(Pair(row,col),default))
        }
    }

    return result.joinToString("") { if (it == '#') "1" else "0" }.toInt(2)

}