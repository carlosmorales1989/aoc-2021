import java.io.File

fun main() {
    val lines = File("data/inputDay2.txt").readLines()

    var depth = 0
    var x = 0

    for (line in lines)
    {
        val info = line.split(" ")
        if (info[0].equals("down")){
            depth+=info[1].toInt()
        } else if (info[0].equals("up")){
            depth-=info[1].toInt()
        } else if (info[0].equals("forward")){
            x+=info[1].toInt()
        }

    }
    println(depth)
    println(x)
    println(depth*x)



}