package day07

import println
import readInput

private const val day = "day07"

fun main() {
    
    fun generateOperatorCombinations(size: Int, operators: List<Char>): List<List<Char>> {
        if (size == 0) return listOf(emptyList())
        val subCombinations = generateOperatorCombinations(size - 1, operators)
        return subCombinations.flatMap { combination ->
            operators.map { combination + it }
        }
    }

    fun concatenateNumbers(num1: Long, num2: Long): Long {
        return "$num1$num2".toLong()
    }
    
    fun evaluateEquation(numbers: List<Long>, operators: List<Char>): Long {
        var result = numbers[0]
        for (i in operators.indices) {
            result = when (operators[i]) {
                '+' -> result + numbers[i + 1]
                '*' -> result * numbers[i + 1]
                '|' -> concatenateNumbers(result, numbers[i + 1])
                else -> throw IllegalArgumentException("Unsupported operator: ${operators[i]}")
            }
        }
        return result
    }
    
    fun part1(input: List<String>): Long {
        val operators = listOf('+', '*')
        return input.sumOf { line ->
            val (targetValue, numbers) = line.split(":").let { it[0].toLong() to it[1].trim().split(" ").map(String::toLong) }
            val valid = generateOperatorCombinations(numbers.size - 1, operators).any { ops ->
                evaluateEquation(numbers, ops) == targetValue
            }
            if (valid) targetValue else 0L
        }
    }
    
    fun part2(input: List<String>): Long {
        val operators = listOf('+', '*', '|')
        return input.sumOf { line ->
            val (targetValue, numbers) = line.split(":").let { it[0].toLong() to it[1].trim().split(" ").map(String::toLong) }
            val valid = generateOperatorCombinations(numbers.size - 1, operators).any { ops ->
                evaluateEquation(numbers, ops) == targetValue
            }
            if (valid) targetValue else 0L
        }
    }
    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
