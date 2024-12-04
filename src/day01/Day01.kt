package day01

import println
import readInput
import kotlin.math.abs

private val day = "day01"
fun main() {
    fun part1(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        input.forEach { str ->
            val (i1, i2) = str.split("   ").map { it.toInt() }
            leftList.add(i1)
            rightList.add(i2)
            
        }
        leftList.sort()
        rightList.sort()
        var count = 0
        leftList.forEachIndexed { index, element ->
            val ab = abs(element - rightList[index])
            count += ab
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightMap = mutableMapOf<Int,Int>()
        input.forEach { str ->
            val (i1, i2) = str.split("   ").map { it.toInt() }
            leftList.add(i1)
            rightMap[i2] = rightMap.getOrDefault(i2, 0) + 1
            
        }
        
        var count = 0
        leftList.forEach { value ->
            count += value * rightMap.getOrDefault(value, 0)
        }
        return count
        
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(readInput("Day01_test")) == 11)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part2(testInput) == 31)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
