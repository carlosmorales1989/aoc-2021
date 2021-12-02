import java.io.File

fun main() {
    val lines = File("data/inputDay2.txt").readLines()

    var depth = 0
    var x = 0
    var aim = 0
    for (line in lines)
    {
        val info = line.split(" ")
        if (info[0].equals("down")){
            aim+=info[1].toInt()
        } else if (info[0].equals("up")){
            aim-=info[1].toInt()
        } else if (info[0].equals("forward")){
            x+=info[1].toInt()
            depth+= (aim * info[1].toInt())
        }

    }
    println(depth)
    println(x)
    println(depth*x)



}