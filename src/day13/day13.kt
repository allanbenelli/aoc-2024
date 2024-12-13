package day13

import println
import readInput
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.ConcurrentHashMap

private const val day = "day13"

fun main() {
    data class Point(val x: Long, val y: Long)
    
    data class Machine(val a: Point, val b: Point, val prize: Point)
    

    
    fun parseButton(s: String): Point {
        val (x, y) = s.split(", ")
        return Point(
            x = x.removePrefix("X+").toLong(),
            y = y.removePrefix("Y+").toLong()
        )
    }
    
    fun parsePrize(s: String, offset: Long): Point {
        val (x, y) = s.split(", ")
        return Point(
            x = x.removePrefix("X=").toLong() + offset,
            y = y.removePrefix("Y=").toLong() + offset
        )
    }
    
    fun solveMachines(machines: List<Machine>): Long {
        var tokens = 0L
        
        for (machine in machines) {
            val numA = machine.prize.y * machine.b.x - machine.prize.x * machine.b.y
            val denomA = machine.a.y * machine.b.x - machine.a.x * machine.b.y
            if (numA % denomA != 0L) continue
            
            val numB = machine.prize.y * machine.a.x - machine.prize.x * machine.a.y
            val denomB = -denomA
            if (numB % denomB != 0L) continue
            
            tokens += (numA / denomA) * 3 + (numB / denomB)
        }
        
        return tokens
    }
    
    fun parseInput(input: List<String>, part2: Boolean): List<Machine> {
        val machines = mutableListOf<Machine>()
        val offset = if (part2) 10_000_000_000_000L else 0L
        
        var index = 0
        while (index < input.size) {
            if (input[index].isBlank()) {
                index++
                continue
            }
            
            val buttonA = input[index++].removePrefix("Button A: ")
            val buttonB = input[index++].removePrefix("Button B: ")
            val prize = input[index++].removePrefix("Prize: ")
            
            machines.add(
                Machine(
                    a = parseButton(buttonA),
                    b = parseButton(buttonB),
                    prize = parsePrize(prize, offset)
                )
            )
        }
        
        return machines
    }
    
    fun part1(input: List<String>): Long {
        val machines = parseInput(input, part2 = false)
        return solveMachines(machines)
    }
    
    fun part2(input: List<String>): Long {
        val machines = parseInput(input, part2 = true)
        return solveMachines(machines)
    }
    
    
    val testInput = readInput("$day/test")
    println(part1(testInput))
    check(part1(testInput) == 480L)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("${day}/input")
    part1(input).println()
    part2(input).println()
}
