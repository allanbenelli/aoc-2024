package day18

import directions
import normalDirections
import println
import readInput

private const val day = "day18"

fun main() {
    data class Point(val x: Int, val y: Int)
    fun solve(points: List<Point>, size: Int) : Int{
        val matrix = Array(size) { CharArray(size) { '.' } }
        
        points.forEach {
            matrix[it.x][it.y] = '#'
        }
        val start = Point(0, 0)
        val end = Point(size - 1, size-1)
        
        val visited = Array(size) { BooleanArray(size) { false } }
        val queue: ArrayDeque<Pair<Point, Int>> = ArrayDeque()
        queue.add(Pair(start, 0))
        visited[start.x][start.y] = true
        
        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()
            if (current == end) return distance
            
            for (dir in normalDirections) {
                val next = Point(current.x + dir.first, current.y + dir.second)
                if (next.x in 0 until size && next.y in 0 until size &&
                    !visited[next.x][next.y] && matrix[next.x][next.y] != '#'
                ) {
                    queue.add(Pair(next, distance + 1))
                    visited[next.x][next.y] = true
                }
            }
        }
        
        return -1
    }
    fun part1(input: List<String>, size: Int, bytes: Int): Int {
        val points = input.map { line ->
            val (x, y) = line.split(",").map { it.trim().toInt() }
            Point(y, x)
        }
        return solve(points.subList(0, bytes), size)
    }
    
    fun isPathBlocked(matrix: Array<CharArray>, size: Int): Boolean {
        val start = Point(0, 0)
        val end = Point(size - 1, size - 1)
        
        val visited = Array(size) { BooleanArray(size) { false } }
        val queue: ArrayDeque<Point> = ArrayDeque()
        queue.add(start)
        visited[start.x][start.y] = true
        
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == end) return false
            
            for (dir in normalDirections) {
                val next = Point(current.x + dir.first, current.y + dir.second)
                if (next.x in 0 until size && next.y in 0 until size &&
                    !visited[next.x][next.y] && matrix[next.x][next.y] != '#'
                ) {
                    queue.add(next)
                    visited[next.x][next.y] = true
                }
            }
        }
        return true
    }
    
    fun part2(input: List<String>, size: Int): String {
        val points = input.map { line ->
            val (x, y) = line.split(",").map { it.trim().toInt() }
            Point(y, x)
        }
        
        val matrix = Array(size) { CharArray(size) { '.' } }
        
        for ((index, point) in points.withIndex()) {
            matrix[point.x][point.y] = '#'
            if (isPathBlocked(matrix, size)) {
                return "${point.y},${point.x}"
            }
        }
        
        return "-1,-1" // Should not happen if there is a blocking byte
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    part1(testInput, 7, 12).println()
    check(part1(testInput, 7, 12) == 22)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input, 71,1024).println()
    part2(input, 71).println()
}
