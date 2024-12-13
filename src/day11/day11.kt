package day11

import println
import readInput
import java.io.File

private const val day = "day11"

fun main() {
    val memo = mutableMapOf<Pair<Long, Int>, Long>()
    
    fun blinkSingleElement(stone: Long, times: Int): Long {
        val key = stone to times
        
        if (key in memo) return memo[key]!!
        
        if (times == 0) {
            memo[key] = 1
            return 1
        }
        
        val newList = mutableListOf<Long>()
        when {
            stone == 0L -> newList.add(1)
            stone.toString().length % 2 == 0 -> {
                val len = stone.toString().length / 2
                val first = stone.toString().substring(0, len).toLong()
                val second = stone.toString().substring(len).toLong()
                newList.add(first)
                newList.add(second)
            }
            else -> newList.add(stone * 2024)
        }
        
        val result = newList.sumOf { blinkSingleElement(it, times - 1) }
        
        memo[key] = result
        
        return result
    }
    
    fun part1(input: List<String>): Long {
        val intInput = input.first().split(" ").map { it.toLong() }
        var count = 0L
        
        intInput.forEach { count += blinkSingleElement(it, 25) }
        return count
    }
    
    fun part2(input: List<String>): Long {
        val intInput = input.first().split(" ").map { it.toLong() }
        var count = 0L
        
        intInput.forEach { count += blinkSingleElement(it, 75) }
        return count
    }
    


    // Test if implementation meets criteria from the description, like:
    //heck(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 55312L)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
