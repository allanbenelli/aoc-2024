package day03

import println
import readInput

private val day = "day03"
fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        var cnt = 0
        input.forEach { line -> regex.findAll(line).iterator().forEach { matchResult ->
            cnt += matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
        }}

        return cnt
    }


    fun part2(input: List<String>): Int {
        var cnt = 0
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        val doString = "do()"
        val dontString = "don't()"
        
        input.forEach {
            it.split(doString).iterator().forEach { doPart ->
                val effectiveDo = doPart.substringBefore(dontString)
                regex.findAll(effectiveDo).forEach { matchResult ->
                    cnt += matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
                }
            }
        
        }
        
        return cnt
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 161)
    
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
