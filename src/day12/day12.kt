package day12

import normalDirections
import println
import readInput

private const val day = "day12"

fun main() {
    data class Region(val area: Int, val perimeter: Int, val sides: Int) {}
    
    
    fun dfs(
        matrix: Array<CharArray>,
        x: Int,
        y: Int,
        visited: Array<BooleanArray>
    ): Region {
        val rows = matrix.size
        val cols = matrix.first().size
        val plant = matrix[x][y]
        
        val stack = mutableListOf(Pair(x, y))
        visited[x][y] = true
        
        var area = 0
        var perimeter = 0
        val perimMap = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
        
        while (stack.isNotEmpty()) {
            val (cx, cy) = stack.removeLast()
            area++
            for ((dx, dy) in normalDirections) {
                val nx = cx + dx
                val ny = cy + dy
                
                if (nx in 0 until rows && ny in 0 until cols) {
                    if (matrix[nx][ny] == plant) {
                        if (!visited[nx][ny]) {
                            visited[nx][ny] = true
                            stack.add(Pair(nx, ny))
                        }
                    } else {
                        perimeter++
                        perimMap.getOrPut(Pair(dx, dy)) { mutableSetOf() }.add(Pair(cx, cy))
                    }
                } else {
                    perimeter++
                    perimMap.getOrPut(Pair(dx, dy)) { mutableSetOf() }.add(Pair(cx, cy))                }
            }
        }
        
        var sides = 0
        for ((_, points) in perimMap) {
            val seenPerim = mutableSetOf<Pair<Int, Int>>()
            for (point in points) {
                if (point in seenPerim) continue
                sides++
                val perimQueue = ArrayDeque<Pair<Int, Int>>()
                perimQueue.add(point)
                while (perimQueue.isNotEmpty()) {
                    val (curR, curC) = perimQueue.removeFirst()
                    if (Pair(curR, curC) in seenPerim) continue
                    seenPerim.add(Pair(curR, curC))
                    for ((dr, dc) in normalDirections) {
                        val newR = curR + dr
                        val newC = curC + dc
                        if (Pair(newR, newC) in points) perimQueue.add(Pair(newR, newC))
                    }
                }
            }
        }
        
        return Region(area, perimeter, sides)
    }
    
    fun calculateResult(
        input: List<String>,
        compute: (Region) -> Int
    ): Int {
        val matrix = input.map { it.toCharArray() }.toTypedArray()
        val visited = Array(matrix.size) { BooleanArray(matrix.first().size) }
        var result = 0
        
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (!visited[i][j]) {
                    val region = dfs(matrix, i, j, visited)
                    result += compute(region)
                }
            }
        }
        return result
    }
    
    fun part1(input: List<String>): Int {
        return calculateResult(input) { region -> region.area * region.perimeter }
    }
    
    fun part2(input: List<String>): Int {
        return calculateResult(input) { region -> region.area * region.sides }
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)
    
    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("${day}/test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("${day}/input")
    part1(input).println()
    part2(input).println()
}
