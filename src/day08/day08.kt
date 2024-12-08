package day05

import println
import readInput

private const val day = "day08"

fun main() {
    
    fun antinodesWithinBounds(point: Pair<Int, Int>, matrix: Array<CharArray>): Boolean {
        val (x, y) = point
        return x in matrix.indices && y in matrix.first().indices
    }
    
    fun getAntennas(matrix: Array<CharArray>): MutableMap<Char, MutableSet<Pair<Int, Int>>> {
        val antennas = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] != '.') {
                    antennas.computeIfAbsent(matrix[i][j]) { mutableSetOf() }.add(Pair(i, j))
                }
            }
        }
        return antennas
    }
    
    fun part1(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }.toTypedArray()
        val antennas = getAntennas(matrix)
        
        val antinodes = mutableSetOf<Pair<Int,Int>>()
        antennas.forEach { (_, setOfPoints) ->
            val pointsList = setOfPoints.toList()
            for (i in pointsList.indices) {
                for (j in i + 1 until pointsList.size) {
                    val (x1, y1) = pointsList[i]
                    val (x2, y2) = pointsList[j]
                    
                    val dx = x2 - x1
                    val dy = y2 - y1
                    
                    val antinode1 = Pair(x1 - dx, y1 - dy)
                    val antinode2 = Pair(x2 + dx, y2 + dy)
                    
                    if (antinodesWithinBounds(antinode1, matrix)) antinodes.add(antinode1)
                    if (antinodesWithinBounds(antinode2, matrix)) antinodes.add(antinode2)
                }
            }
        }
        
        return antinodes.size
    }
    
    

    fun part2(input: List<String>): Int {
        val matrix = input.map { it.toCharArray() }.toTypedArray()
        val antennas = getAntennas(matrix)
        
        val antinodes = mutableSetOf<Pair<Int,Int>>()
        antennas.forEach { (_, points) ->
            val pointsList = points.toList()
            for (i in pointsList.indices) {
                for (j in i + 1 until pointsList.size) {
                    val (x1, y1) = pointsList[i]
                    val (x2, y2) = pointsList[j]
                    
                    val dx = x2 - x1
                    val dy = y2 - y1
                    
                    var antinode1 = Pair(x1 - dx, y1 - dy)
                    var antinode2 = Pair(x2 + dx, y2 + dy)
                    
                    while (antinodesWithinBounds(antinode1, matrix)) {
                        antinodes.add(antinode1)
                        antinode1 = Pair(antinode1.first - dx, antinode1.second - dy)
                    }
                    
                    while (antinodesWithinBounds(antinode2, matrix)) {
                        antinodes.add(antinode2)
                        antinode2 = Pair(antinode2.first + dx, antinode2.second + dy)
                    }
                }
            }
        }
        antennas.forEach { (_, points) ->
            antinodes.addAll(points)
            
        }
        return antinodes.size
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
