package day02

import println
import readInput
import kotlin.math.abs

private val day = "day02"
fun main() {
    fun part1(input: List<String>): Int {
        return input.count { line ->
            val values = line.split(" ").map { it.toInt() }
            checkList(values)
        }
    }
    
    fun part2(input: List<String>): Int {
        return input.count { line ->
            val values = line.split(" ").map { it.toInt() }
            if (checkList(values)) {
                true
            } else {
                // Try removing each level and check if the modified report is safe
                values.indices.any { i ->
                    val modifiedValues = values.toMutableList().apply { removeAt(i) }
                    checkList(modifiedValues)
                }
            }
        }
    }
    

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}

fun checkList(list: List<Int>): Boolean {
    if (list.size < 2) return true // Trivially safe if too short
    val increasing = list[1] > list[0] // Determine initial trend
    for (i in 1 until list.size) {
        val diff = abs(list[i] - list[i - 1])
        if (diff !in 1..3 || (list[i] > list[i - 1]) != increasing) {
            return false
        }
    }
    return true
}