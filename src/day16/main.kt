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
    
    fun findShortestPathO(start: Pair<Int, Int>, end: Pair<Int, Int>, matrix: List<CharArray>): Int {
        val queue = PriorityQueue<State>(compareBy { it.cost })
        val visited = mutableSetOf<Triple<Int, Int, Int>>()
        val dir = normalDirections.withIndex().find { it.value == directionRight }!!
        queue.add(State(start.first, start.second, dir.index, 0)) // Start facing East (index 1)
        
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            
            //check if end
            if (current.x == end.first && current.y == end.second) {
                return current.cost
            }
            // check if already visited
            val stateKey = Triple(current.x,current.y,current.direction)
            if (stateKey in visited) continue
            visited.add(stateKey)
            
            // next step
            
            val (dx, dy) = normalDirections[current.direction]
            val nx = current.x + dx
            val ny = current.y + dy
            if (nx in matrix.indices && ny in matrix[0].indices && matrix[nx][ny] != '#') {
                queue.add(State(nx, ny, current.direction, current.cost+1))
            }
            // Rotate clockwise
            val clockwiseDir = (current.direction + 1) % 4
            queue.add(State(current.x, current.y, clockwiseDir, current.cost + 1000))
            
            // Rotate counterclockwise
            val counterclockwiseDir = (current.direction + 3) % 4
            queue.add(State(current.x, current.y, counterclockwiseDir, current.cost + 1000))
            
            
        }
        throw IllegalStateException("No path found from S to E, but should never happen...")
    }
    
    fun reverseTraversal(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        matrix: List<CharArray>,
        costMap: Map<Pair<Int, Int>, Int>
    ): Set<Pair<Int, Int>> {
        val bestPathTiles = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(end)
        bestPathTiles.add(end)
        
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == start) break
            // Explore neighbors for forward movements
            for ((dx, dy) in normalDirections) {
                val nx = current.first - dx
                val ny = current.second - dy
                
                if (nx in matrix.indices && ny in matrix[0].indices && matrix[nx][ny] != '#') {
                    val neighbor = nx to ny
                    if (costMap[neighbor] == costMap[current]!! - 1 || costMap[neighbor] == costMap[current]!! - 1001) {
                        if (neighbor !in bestPathTiles) {
                            bestPathTiles.add(neighbor)
                            queue.add(neighbor)
                        }
                    }
                }
            }
            
        }
        return bestPathTiles
    }
    
    fun part1(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }
        val (start, end) = findStartAndEnd(matrix)
        val (result, _) = findShortestPathCost(start, end, matrix)
        return result
    }
    
    fun part2(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }
        val (start, end) = findStartAndEnd(matrix)
        val (_, costMap) = findShortestPathCost(start, end, matrix)
        val tilesInBestPaths = reverseTraversal(start, end, matrix, costMap)
        return tilesInBestPaths.size
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
    part2(input).println()
}
