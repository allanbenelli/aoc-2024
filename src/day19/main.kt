package day19

import println
import readInput

private const val day = "day19"

fun main() {
    
    fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
        val blankIndex = input.indexOf("")
        val patterns = input[0].split(", ").map { it.trim() }
        val designs = input.subList(blankIndex + 1, input.size).map { it.trim() }
        return patterns to designs
    }
    fun canConstructDesign(design: String, patterns: List<String>, memo: MutableMap<String, Boolean>): Boolean {
        if (design.isEmpty()) return true
        if (design in memo) return memo[design]!!
        
        for (pattern in patterns) {
            if (design.startsWith(pattern)) {
                if (canConstructDesign(design.removePrefix(pattern), patterns, memo)) {
                    memo[design] = true
                    return true
                }
            }
        }
        memo[design] = false
        return false
    }
    
    fun countWaysToConstruct(design: String, patterns: List<String>, memo: MutableMap<String, Long>): Long {
        if (design.isEmpty()) return 1L
        if (design in memo) return memo[design]!!
        
        var count = 0L
        for (pattern in patterns) {
            if (design.startsWith(pattern)) {
                count += countWaysToConstruct(design.removePrefix(pattern), patterns, memo)
            }
        }
        memo[design] = count
        return count
    }
    
    fun part1(input: List<String>): Int {
        val (towels, designs) = parseInput(input)
        return designs.count { design -> canConstructDesign(design, towels, mutableMapOf()) }
    }
    
    fun part2(input: List<String>): Long {
        val (towels, designs) = parseInput(input)
        return designs.sumOf { design -> countWaysToConstruct(design, towels, mutableMapOf()) }
    }

    // Test if implementation meets criteria from the description, like:

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 6)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
