package day10

import normalDirections
import println
import readInput

private const val day = "day10"

fun main() {
    fun dfs(
        inp: List<List<Int>>,
        x: Int,
        y: Int,
        targets: MutableSet<Pair<Int, Int>> = mutableSetOf(),
    
    ): Int {
        val rows = inp.size
        val cols = inp[0].size
        
        if (inp[x][y] == 9) {
            targets.add(x to y)
            return 1
        }
        var pathCount = 0
        
        for ((dx, dy) in normalDirections) {
            val nextX = x + dx
            val nextY = y + dy
            
            if (nextX in 0 until rows && nextY in 0 until cols) {
                val currentHeight = inp[x][y]
                val nextHeight = inp[nextX][nextY]
                
                if (nextHeight == currentHeight + 1) {
                    pathCount += dfs(inp, nextX, nextY, targets)
                }
            }
        }
        
        return pathCount
    }
    
    fun part1(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val inp = input.map { line -> line.map { it.digitToInt() } }
        
        var totalScore = 0
        
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (inp[i][j] == 0) {
                    val targets = mutableSetOf<Pair<Int, Int>>()
                    dfs(inp, i, j, targets)
                  
                    totalScore += targets.size
                }
            }
        }
        
        return totalScore
    }
    fun part2(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val inp = input.map { line -> line.map { it.digitToInt() } }
        
        var totalScore = 0
        
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (inp[i][j] == 0) {
                    totalScore += dfs(inp, i, j)
                }
            }
        }
        
        return totalScore
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 36)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    part1(testInput).println()
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
