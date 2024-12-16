package day16

import directionRight
import normalDirections
import println
import readInput
import java.util.*

private const val day = "day16"

fun main() {
    fun findStartAndEnd(matrix: List<CharArray>): Pair<Pair<Int,Int>, Pair<Int,Int>> {
        var start = Pair(0, 0)
        var end = Pair(0, 0)
        
        for (x in matrix.indices) {
            for (y in matrix[x].indices) {
                if (matrix[x][y] == 'S') {
                    start = Pair(x, y)
                } else if (matrix[x][y] == 'E') {
                    end = Pair(x, y)
                }
            }
        }
        return start to end
        
    }
    data class State(val x: Int, val y: Int, val direction: Int, val cost: Int)
    
    fun findShortestPathCost(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        matrix: List<CharArray>
    ): Pair<Int, Map<Pair<Int, Int>, Int>> {
        val queue = PriorityQueue<State>(compareBy { it.cost })
        val costMap = mutableMapOf<Pair<Int, Int>, Int>()
        val visited = mutableSetOf<Triple<Int,Int,Int>>()
        val dir = normalDirections.withIndex().find { it.value == directionRight }!!
        queue.add(State(start.first, start.second, dir.index, 0)) // Start facing East (index 1)
        
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (Triple(current.x, current.y, current.direction) in visited) continue
            
            val currentPos = current.x to current.y
            
            if (costMap.getOrDefault(currentPos, Int.MAX_VALUE) > current.cost) {
                costMap[currentPos] = current.cost
            }
            visited.add(Triple(current.x, current.y, current.direction) )
            
            
            // Stop if we reach the end
            if (current.x == end.first && current.y == end.second) {
                continue
            }
            
            
            // Next step
            val (dx, dy) = normalDirections[current.direction]
            val nx = current.x + dx
            val ny = current.y + dy
            if (nx in matrix.indices && ny in matrix[0].indices && matrix[nx][ny] != '#') {
                queue.add(State(nx, ny, current.direction, current.cost + 1))
            }
            
            // Rotate clockwise
            val clockwiseDir = (current.direction + 1) % 4
            queue.add(State(current.x, current.y, clockwiseDir, current.cost + 1000))
            
            // Rotate counterclockwise
            val counterclockwiseDir = (current.direction + 3) % 4
            queue.add(State(current.x, current.y, counterclockwiseDir, current.cost + 1000))
        }
        
        return costMap[end]!! to costMap
    }
    
    fun reverseTraversalWithDirection(
        end: Pair<Int, Int>,
        matrix: List<CharArray>,
        costMap: Map<Pair<Int, Int>, Int>
    ): MutableSet<Pair<Int, Int>> {
        data class InternalState(val position: Pair<Int, Int>, val direction: Int)
        
        val path = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<InternalState>()
        val visited = mutableSetOf<Pair<Int,Int>>()
        
        queue.add(InternalState(end, -1)) // Direction is irrelevant at the end
        path.add(end)
        
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            
            if (current.position in visited) continue
            visited.add(current.position)
            
            val (cx, cy) = current.position
            if (current.position == 7 to 15) {
                println("ge") }
            for ((dirIndex, dir) in normalDirections.withIndex()) {
                val nx = cx - dir.first
                val ny = cy - dir.second
                val neighbor = nx to ny
                
                if (nx in matrix.indices && ny in matrix[0].indices && matrix[nx][ny] != '#') {
                    val currentCost = costMap[current.position] ?: continue
                    val neighborCost = costMap[neighbor] ?: continue
                    
                    // Check conditions:
                    // 1. Forward movement with cost difference = 1
                    // 2. Forward movement without rotation with cost difference = 999 (same direction)
                    // 3. Rotational movement with cost difference = 1001
                    if (
                        neighborCost == currentCost - 1 || // Straight movement
                        (neighborCost == currentCost + 999 && dirIndex == current.direction) || // No rotation
                        neighborCost == currentCost - 1001 // Rotation
                    ) {
                        queue.add(InternalState(neighbor, dirIndex))
                        path.add(neighbor)
                    }
                }
            }
        }
        
        return path
    }
    

    fun part1(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }
        val (start, end) = findStartAndEnd(matrix)
        val (result, _) = findShortestPathCost(start, end, matrix)
        return result
    }
    
    fun printPath(matrix: List<CharArray>, path: Set<Pair<Int, Int>>) {
        val updatedMatrix = matrix.map { it.copyOf() } // Create a mutable copy of the maze
        path.forEach { (x, y) ->
            if (updatedMatrix[x][y] == '.') {
                updatedMatrix[x][y] = 'O' // Mark the path with 'O'
            }
        }
        updatedMatrix.forEach { println(it.concatToString()) }
    }
    
    fun printCostMap(matrix: List<CharArray>, costMap: Map<Pair<Int, Int>, Int>) {
        val costGrid = matrix.mapIndexed { x, row ->
            row.mapIndexed { y, cell ->
                when {
                    cell == '#' -> "#####" // Wall
                    (x to y) in costMap -> costMap[x to y].toString().padStart(5, ' ')
                    else -> "   ." // Unvisited tile
                }
            }.joinToString(" ")
        }
        costGrid.forEach { println(it) }
    }
    
    
    fun part2(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }
        val (start, end) = findStartAndEnd(matrix)
        val (_, costMap) = findShortestPathCost(start, end, matrix)
        printCostMap(matrix, costMap)
        val path = reverseTraversalWithDirection(end, matrix, costMap)
        printPath(matrix, path) // Print the maze with the path
        return path.size
    }


    // Test if implementation meets criteria from the description, like:
    // check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 11048)
    part2(testInput).println()
    check(part2(testInput) == 64)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println() // lower than 481 bigger than 442
}
