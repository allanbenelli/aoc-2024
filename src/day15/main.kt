package day15

import Point
import println
import readInput

private const val day = "day15"

fun main() {
    fun findRobot(grid: List<CharArray>): Point {
        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (grid[x][y] == '@') {
                    return Point(x, y)
                }
            }
        }
        throw IllegalArgumentException("Robot not found in grid")
    }
    
    fun scaleWarehouse(grid: List<String>): List<String> {
        return grid.map { row ->
            row.map { char ->
                when (char) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> throw IllegalArgumentException("Unknown character: $char")
                }
            }.joinToString("")
        }
    }
    
    fun updateRobotPosition(warehouse: MutableList<CharArray>, newRobot: Point, robot: Point): Point {
        var intermediate = robot
        warehouse[newRobot.x][newRobot.y] = '@'
        warehouse[intermediate.x][intermediate.y] = '.'
        intermediate = newRobot
        return intermediate
    }
    fun Char.toDirection(): Point = when(this) {
        '^' -> Point(-1, 0) // Up
        'v' -> Point(1, 0)  // Down
        '<' -> Point(0, -1) // Left
        '>' -> Point(0, 1)  // Right
        else -> Point(0, 0)
    }
    
    fun simulateWarehouse(grid: List<String>, moves: String): List<String> {
        val warehouse = grid.map { it.toCharArray() }.toMutableList()
        var robot = findRobot(warehouse)
        
        moves.forEach { move ->
            val direction = move.toDirection()
            
            val newRobot = Point(robot.x + direction.x, robot.y + direction.y)
            val newPositionVal = warehouse[newRobot.x][newRobot.y]
            
            if (newPositionVal == '#') {
                // do nothing
            } else if (newPositionVal == '.') {
                // Empty space: Move robot
                robot = updateRobotPosition(warehouse, newRobot, robot)
            } else if (newPositionVal== 'O') {
                var currentPos = newRobot
                while (true) {
                    val nextPos = Point(currentPos.x + direction.x, currentPos.y + direction.y)
                    val nextPosVal = warehouse[nextPos.x][nextPos.y]
                    
                    if (nextPos.x !in warehouse.indices || nextPos.y !in warehouse[0].indices || nextPosVal == '#') {
                        break
                    } else if (nextPosVal == 'O') {
                        currentPos = nextPos
                    } else if (nextPosVal == '.') {
                        warehouse[nextPos.x][nextPos.y] = 'O'
                        robot = updateRobotPosition(warehouse, newRobot, robot)
                        break
                    }
                    
                }
            }

        }
        
        return warehouse.map { it.joinToString("") }
    }

    fun checkRecusive(pos: Point, direction: Point, transitions: MutableList<Triple<Char, Point, Point>>, warehouse: MutableList<CharArray>): Boolean {
        
        val curPosVal = warehouse[pos.x][pos.y]
        val nextPos = Point(pos.x + direction.x, pos.y + direction.y)
        if (nextPos.x !in warehouse.indices || nextPos.y !in warehouse[0].indices) return false
        
        val newPositionVal = warehouse[nextPos.x][nextPos.y]
        if (newPositionVal == '#') {
            return false
        } else {
            transitions.add(Triple(curPosVal,pos, nextPos))
            if (newPositionVal == '.') return true
            else {
                val otherPos = if (newPositionVal == '[') {
                    Point(nextPos.x, nextPos.y+1)
                } else {
                    Point(nextPos.x, nextPos.y-1)
                }
                return checkRecusive(nextPos, direction, transitions, warehouse) && checkRecusive(otherPos, direction, transitions, warehouse)
            }
        }
        
    }
    
    fun MutableList<CharArray>.applyTransitions(transitions: MutableList<Triple<Char, Point, Point>>) {
        transitions.reversed().forEach { (char, from, to) ->
            this[to.x][to.y] = char
            //this[from.x][from.y] = '.'
        }
        transitions.forEach { (char, from, to) ->
            if (transitions.none { it.third.x == from.x && it.third.y == from.y }) {
                this[from.x][from.y] = '.'
            }
        }
    }

    
    fun simulateWarehouse2(grid: List<String>, moves: String): List<String> {
        val warehouse = grid.map { it.toCharArray() }.toMutableList()
        var robot = findRobot(warehouse)
        
        moves.forEach { move ->
            val direction = move.toDirection()
            
            val newRobot = Point(robot.x + direction.x, robot.y + direction.y)
            val newPositionVal = warehouse[newRobot.x][newRobot.y]
            
            if (newPositionVal == '#') {
                // do nothing
            } else if (newPositionVal == '.') {
                // Empty space: Move robot
                robot = updateRobotPosition(warehouse, newRobot, robot)
            } else {
                val transitions = mutableListOf<Triple<Char, Point, Point>>()
                var currentPos = newRobot

                if (move in setOf('<', '>')) {
                    
                    while (true) {
                        val nextPos = Point(currentPos.x + direction.x, currentPos.y + direction.y)
                        val nextPosVal = warehouse[nextPos.x][nextPos.y]
                        val curPosVal = warehouse[currentPos.x][currentPos.y]
                        
                        if (nextPos.x !in warehouse.indices || nextPos.y !in warehouse[0].indices || nextPosVal == '#') {
                            break
                        } else if (nextPosVal == '[' || nextPosVal == ']') {
                            transitions.add(Triple(curPosVal,currentPos, nextPos))
                            currentPos = nextPos
                        } else if (nextPosVal == '.') {
                            transitions.add(Triple(curPosVal,currentPos, nextPos))
                            warehouse.applyTransitions(transitions)
                            robot = updateRobotPosition(warehouse, newRobot, robot)
                            break
                        }
                    }
                } else {
                    
                    val otherPos = if (newPositionVal == '[') {
                        Point(currentPos.x, currentPos.y+1)
                    } else {
                        Point(currentPos.x, newRobot.y-1)
                    }
                    
                    val canMove = checkRecusive(currentPos, direction, transitions, warehouse) && checkRecusive(otherPos, direction, transitions, warehouse)
                    if (canMove) {
                        warehouse.applyTransitions(transitions)
                        robot = updateRobotPosition(warehouse, currentPos, robot)
                    }
                }

            }
//            println()
//            println("move $move")
//            println(warehouse.map { it.joinToString ( "" ) }.joinToString  ("\n" ))
        }
        
        return warehouse.map { it.joinToString("") }
    }
    
    
    fun part1(input: List<String>): Int {
        val emptyIndex = input.withIndex().first { it.value == "" }
        val initialGrid = input.subList(0, emptyIndex.index)
        val moves = input.subList(emptyIndex.index+1, input.size).joinToString(separator = "")
        
        val finalGrid = simulateWarehouse(initialGrid, moves)
        println(finalGrid.joinToString("\n"))
        
        return finalGrid.sumUp('O')
    }

    fun part2(input: List<String>): Int {
        val emptyIndex = input.withIndex().first { it.value == "" }
        val initialGrid = input.subList(0, emptyIndex.index)
        val moves = input.subList(emptyIndex.index + 1, input.size).joinToString(separator = "")
        
        // Scale the warehouse
        val scaledWarehouse =  scaleWarehouse(initialGrid)
        val finalGrid = simulateWarehouse2(scaledWarehouse, moves)
        println(finalGrid.joinToString("\n"))
        return finalGrid.sumUp('[')

    }


    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}

fun List<String>.sumUp(char: Char) : Int {
    var cnt = 0
    this.forEachIndexed { x, row ->
        row.forEachIndexed { y, value ->
            if (value == char) {
                cnt += x * 100 + y
            }
        }
    }
    return cnt
}
