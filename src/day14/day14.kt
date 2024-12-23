package day14


import Point
import println
import readInput

private const val GRID_X = 101
private const val GRID_Y = 103
private const val MAX_X = 100
private const val MAX_Y = 102
private const val day = "day14"


data class Robot(var position: Point, val velocity: Point)
data class Res(val time: Int, val safetyFactor: Int)
data class Quad(val a: Int, val b: Int, val c: Int, val d: Int)

fun main() {
    
    fun getNewRobotPosition(robot: Robot): Point {
        val (x, y) = robot.position
        val (vX, vY) = robot.velocity
        var newX = (x + vX) % GRID_X
        var newY = (y + vY) % GRID_Y
        
        if (newX < 0) newX = MAX_X - (Math.abs(newX) - 1)
        if (newY < 0) newY = MAX_Y - (Math.abs(newY) - 1)
        
        return Point(newX, newY)
    }
    
    fun countRobotsInQuadrants(robots: List<Robot>): Quad {
        val midX = GRID_X / 2
        val midY = GRID_Y / 2
        
        var a = 0
        var b = 0
        var c = 0
        var d = 0
        
        robots.forEach { robot ->
            val (x, y) = robot.position
            when {
                x < midX && y < midY -> a++
                x > midX && y < midY -> b++
                x < midX && y > midY -> c++
                x > midX && y > midY -> d++
            }
        }
        return Quad(a, b, c, d)
    }
    
    fun parseRobots(input: List<String>): MutableList<Robot> {
        val regex = Regex("p=(-?\\d+,-?\\d+)\\s+v=(-?\\d+,-?\\d+)")
        return input.map { line ->
            val matchResult = regex.find(line) ?: error("Invalid robot data: $line")
            val (position, velocity) = matchResult.destructured
            val (posX, posY) = position.split(",").map { it.toInt() }
            val (velX, velY) = velocity.split(",").map { it.toInt() }
            Robot(Point(posX, posY), Point(velX, velY))
        }.toMutableList()
    }
    
    fun part1(input: List<String>) : Res {
        val robots = parseRobots(input)
        val seconds = 100
        
        repeat(seconds) {
            robots.forEachIndexed { i, robot ->
                robots[i] = robot.copy(position = getNewRobotPosition(robot))
            }
        }
        val (a, b, c, d) = countRobotsInQuadrants(robots)
        val safetyFactor = a * b * c * d
        return Res(seconds, safetyFactor)
    }
    
    fun part2(input: List<String>) : Res {
        val robots = parseRobots(input)
        var lowestSafetyFactor = Int.MAX_VALUE
        var time = 0
        
        repeat(10_000) { i ->
            robots.forEachIndexed { j, robot ->
                robots[j] = robot.copy(position = getNewRobotPosition(robot))
            }
            val (a, b, c, d) = countRobotsInQuadrants(robots)
            val safetyFactor = a * b * c * d
            if (safetyFactor != 0 && (safetyFactor < lowestSafetyFactor || lowestSafetyFactor == Int.MAX_VALUE)) {
                lowestSafetyFactor = safetyFactor
                time = i + 1
            }
        }
        return Res(time, lowestSafetyFactor)
    }

    val testInput = readInput("$day/test")
    //check(part1(testInput).safetyFactor == 12)
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}



