package day04

 import diagonalDownLeft
import diagonalDownRight
import diagonalUpLeft
import diagonalUpRight
import directions
import println
import readInput

private val day = "day04"

fun main() {
    fun part1(input: List<String>): Int {
        val word = "XMAS"
        val numRows = input.size
        val numCols = input.first().length
        fun matches(row: Int, col: Int, dir: Pair<Int, Int>): Boolean {
            val result = buildString {
                for (i in word.indices) {
                    val newRow = row + i * dir.first
                    val newCol = col + i * dir.second
                    if (newRow !in 0 until numRows || newCol !in 0 until numCols) {
                        return false
                    }
                    append(input[newRow][newCol])
                }
            }
            return result == word
        }
        
        return (0 until numRows).sumOf { row ->
            (0 until numCols).sumOf { col ->
                directions.count { matches(row, col, it) }
            }
        }
    }
    
    fun part2(input: List<String>): Int {
        val word = "MAS"
        val wordReversed = word.reversed()
        val numRows = input.size
        val numCols = input.first().length
        var count = 0
        
        fun matchesDiagonal(row: Int, col: Int, diag: Pair<Int,Int>, diag2: Pair<Int,Int>): Boolean {
            val result = buildString {
                append(input[row+diag.first][col+diag.second])
                append(input[row][col])
                append(input[row+diag2.first][col+diag2.second])
            }
 
            return result == word || result == wordReversed
        }
        
        for (row in 1 until numRows -1) {
            for (col in 1 until numCols -1) {
                if (input[row][col] == 'A') {
                    if (matchesDiagonal(row, col, diagonalUpLeft, diagonalDownRight)
                        && matchesDiagonal(row, col, diagonalUpRight, diagonalDownLeft)
                        ) {
                        count++
                    }
                }
            }
        }
        
        return count
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 18)
    
    part2(testInput).println()

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
