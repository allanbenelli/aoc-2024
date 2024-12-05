package day05

import println
import readInput

private const val day = "day05"

fun main() {
    fun splitInput(input: List<String>): Pair<List<String>, List<String>> {
        val emptyRowIndex = input.indexOf("")
        val rules = input.take(emptyRowIndex)
        val updates = input.drop(emptyRowIndex + 1)
        return rules to updates
    }
    
    fun isValidUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
        val positions = update.withIndex().associate { it.value to it.index }
        for ((x, y) in rules) {
            if (x in positions && y in positions && positions[x]!! >= positions[y]!!) {
                return false // x should appear before y, but it doesn't
            }
        }
        return true
    }
    
    
    fun part1(input: List<String>): Int {
        val (rulesInput, updatesInput) = splitInput(input)
        
        val rules = rulesInput.map { it.split("|").map { page -> page.toInt() } }.map { it[0] to it[1] }
        val updates = updatesInput.map { it.split(",").map { page -> page.toInt() } }
        
        val middleSum = updates.filter { isValidUpdate(it, rules) }.sumOf { validUpdate ->
            validUpdate[validUpdate.size / 2]
        }
        
        return middleSum
    }
    
    
    fun reorderUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        val precedence = mutableMapOf<Int, MutableSet<Int>>()
        
        // precedence map
        for ((x, y) in rules) {
            precedence.computeIfAbsent(y) { mutableSetOf() }.add(x)
        }
        
        // compare and sort
        return update.sortedWith { a, b ->
            when {
                a == b -> 0
                a in precedence[b].orEmpty() -> -1 // a should come before b
                b in precedence[a].orEmpty() -> 1  // b should come before a
                else -> 0 // No specific order between a and b
            }
        }
    }
    
    fun part2(input: List<String>): Int {
        val (rulesInput, updatesInput) = splitInput(input)
        
        val rules = rulesInput.map { it.split("|").map { page -> page.toInt() } }.map { it[0] to it[1] }
        val updates = updatesInput.map { it.split(",").map { page -> page.toInt() } }
        
        val middleSum = updates.filterNot { isValidUpdate(it, rules) }.sumOf { invalidUpdate ->
            val reordered = reorderUpdate(invalidUpdate, rules)
            reordered[reordered.size / 2]
        }
        
        return middleSum
    }
    
    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)
    
    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
