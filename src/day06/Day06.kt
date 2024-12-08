package day06

import normalDirections
import println
import readInput

private const val day = "day06"

fun main() {
    
    fun Char.toDirection(): Int = when(this) {
        '^' -> 0
        '>' -> 1
        'v' -> 2
        '<' -> 3
        else -> 0
    }
    
    fun parseInput(input: List<String>): Triple<Array<CharArray>, Pair<Int, Int>, Int> {
        val matrix = input.map { it.toCharArray() }.toTypedArray()
        var guardX = 0
        var guardY = 0
        var direction = 0 // defualt up
        
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] in listOf('^', '>', 'v', '<')) {
                    guardX = i
                    guardY = j
                    direction = matrix[i][j].toDirection()
                    matrix[i][j] = '.'
                }
            }
        }
        return Triple(matrix, Pair(guardX, guardY), direction)
    }
    
    fun simulateWithLoopDetection(
        matrix: Array<CharArray>,
        startPosition: Pair<Int, Int>,
        startDirection: Int,
        obstruction: Pair<Int, Int>? = null
    ): Boolean {
        val rows = matrix.size
        val cols = matrix[0].size
        val visitedStates = mutableSetOf<Triple<Int, Int, Int>>()
        
        var (guardX, guardY) = startPosition
        var direction = startDirection
        
        val modifiedMatrix = matrix.map { it.copyOf() }.toTypedArray()
        obstruction?.let { (ox, oy) -> modifiedMatrix[ox][oy] = '#' }
        
        while (true) {
            val state = Triple(guardX, guardY, direction)
            if (obstruction != null && state in visitedStates) return true // Loop detected
            visitedStates.add(state)
            
            val (dx, dy) = normalDirections[direction]
            val nextX = guardX + dx
            val nextY = guardY + dy
            
            if (nextX !in 0 until rows || nextY !in 0 until cols) break
            
            if (modifiedMatrix[nextX][nextY] == '#') {
                direction = (direction + 1) % 4 // Turn right
            } else {
                guardX = nextX
                guardY = nextY
            }
        }
        
        return false
    }
    
    
    
    fun simulate(
        matrix: Array<CharArray>,
        startPosition: Pair<Int, Int>,
        startDirection: Int
    ): Set<Pair<Int, Int>> {
        val rows = matrix.size
        val cols = matrix[0].size
        val visited = mutableSetOf<Pair<Int, Int>>()
        
        var (guardX, guardY) = startPosition
        var direction = startDirection
        
        val modifiedMatrix = matrix.map { it.copyOf() }.toTypedArray()
        
        visited.add(Pair(guardX, guardY))
        
        while (true) {
            val (dx, dy) = normalDirections[direction]
            val nextX = guardX + dx
            val nextY = guardY + dy
            
            if (nextX !in 0 until rows || nextY !in 0 until cols) break
            
            if (modifiedMatrix[nextX][nextY] == '#') {
                direction = (direction + 1) % 4 // Turn right
            } else {
                guardX = nextX
                guardY = nextY
                val position = Pair(guardX, guardY)
                visited.add(position)
            }
        }
        
        return visited
    }
    
    
    fun part1(input: List<String>): Int {
        val (matrix, startPosition, startDirection) = parseInput(input)
        return simulate(matrix, startPosition, startDirection).size
    }
    
    fun part2(input: List<String>): Int {
        val (matrix, startPosition, startDirection) = parseInput(input)
        
        val potentialObstructions = mutableListOf<Pair<Int, Int>>()
        val visited = simulate(matrix, startPosition, startDirection)
        visited.forEach {
            if (matrix[it.first][it.second] == '.' && it != startPosition) {
                if (simulateWithLoopDetection(matrix, startPosition, startDirection, it)) {
                    potentialObstructions.add(it)
                }
                
            }
        }
        return potentialObstructions.size
    }


    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
    
}
