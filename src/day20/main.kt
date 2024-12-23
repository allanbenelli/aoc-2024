package day20

import Point
import println
import readInput
import java.util.*

private const val day = "day20"


fun main() {
    fun parseInput(input: List<String>): Pair<MutableMap<Point, Char>, Pair<Point, Point>> {
        val points = mutableMapOf<Point, Char>()
        val grid = input.map { it.toCharArray() }.toTypedArray()
        var start = Point(0, 0)
        var end = Point(0, 0)
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] != '#') {
                    points[Point(x, y)] = grid[y][x]
                }
                when (grid[y][x]) {
                    'S' -> start = Point(x, y)
                    'E' -> end = Point(x, y)
                }
            }
        }
        return points to (start to end)
    }
    
    fun bfs(grid: MutableMap<Point, Char>, start: Point, end: Point): MutableMap<Point, Int> {
        val dist = mutableMapOf<Point, Int>()
        val todo = ArrayDeque<Point>()
        dist[start] = 0
        todo.add(start)
        
        val directions = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))
        
        while (todo.isNotEmpty()) {
            val current = todo.removeFirst()
            val currentDist = dist[current]!!
            
            for (dir in directions) {
                val neighbor = current + dir
                if (neighbor in grid && neighbor !in dist) {
                    dist[neighbor] = currentDist + 1
                    todo.add(neighbor)
                }
            }
        }
        return dist
    }
    
    fun preprocessing(input: List<String>): MutableMap<Point, Int> {
        val (grid, startEnd) = parseInput(input)
        val (start, end) = startEnd
        val dist = bfs(grid, start, end)
        return dist
    }
    fun part1(dist: MutableMap<Point, Int>): Int {
        
        var cnt = 0
        for (p in dist.keys) {
            for (q in dist.keys) {
                val d = p.manhattanDistance(q)
                val timeSaved = dist[p]!! - dist[q]!! - d
                
                if (d == 2 && timeSaved >= 100) cnt++
            }
        }
        return cnt
    }
    
    fun part2(dist: MutableMap<Point, Int>): Int {
        var cnt = 0
        for (p in dist.keys) {
            for (q in dist.keys) {
                val d = p.manhattanDistance(q)
                val timeSaved = dist[p]!! - dist[q]!! - d
                
                if (d < 21 && timeSaved >= 100) cnt++
            }
        }
        return cnt    }
    
    val testInput = readInput("$day/test")
    //check(part1(testInput) == 6)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    val preprocessing = preprocessing(input)
    part1(preprocessing).println()
    part2(preprocessing).println()
}
