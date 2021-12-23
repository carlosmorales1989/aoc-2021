import java.io.File
import java.lang.Math.ceil
import java.util.*
import kotlin.math.max

fun main() {
    //val lines = File("data/inputDay18.txt").readLines()
    //val lines = File("data/testDay18_2.txt").readLines()
    val lines = File("data/day18.txt").readLines()

    val numbers = lines.map{readNumber(LinkedList(it.toList()))}

    val result = numbers.reduce { acc, snailfishNumber -> addNumbers(acc,snailfishNumber) }
    println("Final result $result")
    println("Magnitude: ${result.magnitude()}")

    var maxMagnitude = 0

    for(i in numbers.indices){
        for(j in numbers.indices){
            if(i==j){
                continue
            }
            val a = readNumber(LinkedList(lines[i].toList()))
            val b = readNumber(LinkedList(lines[j].toList()))
            maxMagnitude = max(addNumbers(a,b).magnitude(), maxMagnitude)
        }
    }
    println("Max magnitude: $maxMagnitude")

}




fun findNextAction(number: SnailfishNumber): SnailfishAction?{
    number.populateLevels()
    var action: SnailfishAction? = number.findNextExplodeAction()
    if(action != null)
        return action
    action = number.findNextSplitAction()
    return action
}

fun addNumbers(a: SnailfishNumber,b: SnailfishNumber): SnailfishNumber{
    val result = CompositeSnailfishNumber(a,b)
    var action = findNextAction(result)
    //println(result)
    while(action != null){
        action.executeAction()
        //println(action)
        //println(result)
        action = findNextAction(result)
    }
    /*println("A: $a")
    println("B: $b")
    println("Result: $result")
    println("Mag:${result.magnitude()}")*/
    return result
}

fun readNumber(charQueue: Queue<Char>): SnailfishNumber{
    if(charQueue.peek() == '['){
        // Composite
        charQueue.poll() // We remove the open bracket
        val left = readNumber(charQueue)
        charQueue.poll() // We remove the comma
        val right = readNumber(charQueue)
        charQueue.poll() // We remove the closing bracket

        return CompositeSnailfishNumber(left, right)
    }else{
        // Regular
        return RegularSnailfishNumber(charQueue.poll() - '0')
    }
}

abstract class SnailfishAction(val number:SnailfishNumber)
{
    abstract fun executeAction()
}

class SnailfishExplodeAction(number:SnailfishNumber): SnailfishAction(number){
    override fun executeAction(){
        number.explode()
    }

    override fun toString(): String {
        return "Explode {$number}"
    }
}
class SnailfishSplitAction(number:SnailfishNumber): SnailfishAction(number){
    override fun executeAction(){
        number.split()
    }

    override fun toString(): String {
        return "Split {$number}"
    }
}

abstract class SnailfishNumber(){
    var parent: SnailfishNumber? = null

    var position: SnailnumberPosition = SnailnumberPosition.NONE

    var level: Int = 0

    var deleted = false

    abstract fun identifyActions(): MutableList<SnailfishAction>

    abstract fun findNextExplodeAction(): SnailfishExplodeAction?

    abstract fun findNextSplitAction(): SnailfishSplitAction?

    open fun populateLevels(){
        level = (parent?.level ?: -1) + 1
    }

    open fun explode() {
        throw Exception("I shouldnt be here:$this")
    }

    open fun split() {
        throw Exception("I shouldnt be here:$this")
    }

    open fun findLeftmost(origin: SnailnumberPosition): RegularSnailfishNumber? {
        throw Exception("I shouldnt be here:$this")
    }
    open fun findRightmost(origin: SnailnumberPosition): RegularSnailfishNumber? {
        throw Exception("I shouldnt be here:$this")
    }

    fun findParentForExplode(position: SnailnumberPosition): SnailfishNumber?{
        if(this.position != position){
            return parent
        }else{
            return parent?.findParentForExplode(position)
        }
    }

    abstract fun magnitude(): Int

}

class RegularSnailfishNumber(var value: Int): SnailfishNumber(){
    override fun identifyActions(): MutableList<SnailfishAction> {
        if(value >= 10){
            return mutableListOf(SnailfishSplitAction(this))
        }
        return mutableListOf()
    }

    override fun findNextExplodeAction(): SnailfishExplodeAction? {
        return null
    }

    override fun findNextSplitAction(): SnailfishSplitAction? {
        if(value>=10){
            return SnailfishSplitAction(this)
        }
        return null
    }

    override fun toString(): String {
        return value.toString()
    }



    override fun findLeftmost(origin: SnailnumberPosition): RegularSnailfishNumber {
        return this
    }

    override fun findRightmost(origin: SnailnumberPosition): RegularSnailfishNumber {
        return this
    }

    override fun magnitude(): Int {
        return value
    }

    override fun split() {
        if(deleted) return
        val newNumber = CompositeSnailfishNumber(
            RegularSnailfishNumber(value/2),
            RegularSnailfishNumber(ceil(value/2.0).toInt()))
        if(position == SnailnumberPosition.LEFT){
            (parent as CompositeSnailfishNumber).left = newNumber
            newNumber.position = SnailnumberPosition.LEFT
        }else{
            (parent as CompositeSnailfishNumber).right = newNumber
            newNumber.position = SnailnumberPosition.RIGHT
        }
        newNumber.parent = parent
        parent!!.populateLevels()
        this.deleted = true
    }
}

class CompositeSnailfishNumber(var left: SnailfishNumber, var right: SnailfishNumber):
    SnailfishNumber(){

    init {
        left.parent = this
        left.position = SnailnumberPosition.LEFT
        right.parent = this
        right.position = SnailnumberPosition.RIGHT
    }

    override fun identifyActions(): MutableList<SnailfishAction> {
        populateLevels()
        if (level == 4){
            return mutableListOf(SnailfishExplodeAction(this))
        }
        val result = left.identifyActions()
        result.addAll(right.identifyActions())
        return result

    }

    override fun findNextExplodeAction(): SnailfishExplodeAction? {
        if(level == 4)
            return SnailfishExplodeAction(this)
        var action = left.findNextExplodeAction()
        if(action != null){
            return action
        }
        action = right.findNextExplodeAction()
        if(action != null){
            return action
        }
        return null
    }

    override fun findNextSplitAction(): SnailfishSplitAction? {
        var action = left.findNextSplitAction()
        if(action != null){
            return action
        }
        action = right.findNextSplitAction()
        if(action != null){
            return action
        }
        return null
    }

    override fun toString(): String {
        return "($left,$right)"
    }

    override fun populateLevels() {
        super.populateLevels()
        left.populateLevels()
        right.populateLevels()
    }

    override fun explode() {
        if(deleted) return

        val leftMostParent = findParentForExplode(SnailnumberPosition.LEFT)
        if (leftMostParent != null) {
            val leftMost = (leftMostParent as CompositeSnailfishNumber).left.findRightmost(position)
            if (leftMost != null) {
                leftMost.value += (left as RegularSnailfishNumber).value
            }
        }
        val rightMostParent = findParentForExplode(SnailnumberPosition.RIGHT)
        if (rightMostParent != null){
            val rightMost = (rightMostParent as CompositeSnailfishNumber).right.findLeftmost(position)
            if(rightMost != null){
                rightMost.value += (right as RegularSnailfishNumber).value
            }
        }
        val newNumber = RegularSnailfishNumber(0)
        if(position == SnailnumberPosition.LEFT){
            (parent as CompositeSnailfishNumber).left = newNumber
            newNumber.position = SnailnumberPosition.LEFT
        }else{
            (parent as CompositeSnailfishNumber).right = newNumber
            newNumber.position = SnailnumberPosition.RIGHT
        }
        this.deleted = true
        newNumber.parent = parent
    }

    override fun findLeftmost(origin: SnailnumberPosition): RegularSnailfishNumber? {
        return left.findLeftmost(origin)
    }

    override fun findRightmost(origin: SnailnumberPosition): RegularSnailfishNumber? {
        return right.findRightmost(origin)
    }

    override fun magnitude(): Int {
        return 3*left.magnitude() + 2* right.magnitude()
    }

}

enum class SnailnumberPosition{
    LEFT, RIGHT, NONE
}