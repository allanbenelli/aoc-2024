package day25

import println
import readInput

private const val day = "day25"

fun main() {
    
    
    fun isLock(block: List<String>): Boolean {
        return block[0].all { it == '#' } && block[6].all { it == '.' }
    }
    
    fun isKey(block: List<String>): Boolean {
        return block[0].all { it == '.' } && block[6].all { it == '#' }
    }
    
    fun parseLockHeights(block: List<String>): IntArray {
        val heights = IntArray(5) { 0 }
        for (col in 0 until 5) {
            var count = 0
            for (row in 1..5) {
                if (block[row][col] == '#') {
                    count++
                } else {
                    break
                }
            }
            heights[col] = count
        }
        return heights
    }
    
    fun parseKeyHeights(block: List<String>): IntArray {
        val heights = IntArray(5) { 0 }
        for (col in 0 until 5) {
            var count = 0
            for (row in 5 downTo 1) {
                if (block[row][col] == '#') {
                    count++
                } else {
                    break
                }
            }
            heights[col] = count
        }
        return heights
    }
    
    fun parseInput(input: List<String>): Pair<List<IntArray>, List<IntArray>> {
        val blocks = mutableListOf<List<String>>()
        var i = 0
        while (i < input.size) {
            val chunk = mutableListOf<String>()
            while (chunk.size < 7 && i < input.size) {
                val line = input[i].trimEnd()
                if (line.isNotBlank()) {
                    chunk.add(line)
                }
                i++
            }
            if (chunk.size == 7) {
                blocks.add(chunk)
            }
        }
        
        val locks = mutableListOf<IntArray>()
        val keys = mutableListOf<IntArray>()
        
        for (block in blocks) {
            when {
                isLock(block) -> locks.add(parseLockHeights(block))
                isKey(block)  -> keys.add(parseKeyHeights(block))
                else -> error("should not happen :)")
            }
        }
        
        return locks to keys
    }
    fun part1(input: List<String>): Int {
        val (locks, keys) = parseInput(input)
        var fitCount = 0
        for (lockPins in locks) {
            for (keyPins in keys) {
                val fits = (0 until 5).all { lockPins[it] + keyPins[it] <= 5 }
                if (fits) {
                    fitCount++
                }
            }
        }
        return fitCount
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
    
    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 3)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
