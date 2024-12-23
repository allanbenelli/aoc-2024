package day22

import println
import readInput

private const val day = "day22"

fun main() {
    
    fun prune(secret: Long): Long {
        return secret % 16777216
    }
    
    fun generateNextSecret(secret: Long): Long {
        val mixedAndPruned1 = prune(secret xor (secret * 64))
        val mixedAndPruned2 = prune(mixedAndPruned1 xor (mixedAndPruned1 / 32))
        return prune(mixedAndPruned2 xor (mixedAndPruned2 * 2048))
    }
   
    
    fun simulateBuyerSecrets(initialSecret: Long, iterations: Int): List<Long> {
        val secrets = mutableListOf(initialSecret)
        var secret = initialSecret
        repeat(iterations) {
            secret = generateNextSecret(secret)
            secrets.add(secret)
        }
        return secrets
    }
    
    
    fun findBestSequence(input: List<String>, sequenceLength: Int = 4): Pair<List<Int>, Int> {
        val buyers = input.map { it.toLong() }
        val sequenceOccurrences = mutableMapOf<List<Int>, Int>()
        
        buyers.forEach { initialSecret ->
            val prices = simulateBuyerSecrets(initialSecret, 2000).map { (it % 10).toInt() }
            val changes = prices.zipWithNext { a, b -> b - a }
            
            val seenSequences = mutableSetOf<List<Int>>()
            for (i in 0..changes.size - sequenceLength) {
                val sequence = changes.subList(i, i + sequenceLength)
                if (sequence !in seenSequences) {
                    seenSequences.add(sequence)
                    sequenceOccurrences[sequence] = sequenceOccurrences.getOrDefault(sequence, 0) + prices[i + sequenceLength]
                }
            }
        }
        
        return sequenceOccurrences.maxByOrNull { it.value }!!.toPair()
    }
    
    
    fun part1(input: List<String>): Long {
        return input.map { it.toLong() }.sumOf { simulateBuyerSecrets(it, 2000).last() }
    }
    
    fun part2(input: List<String>): Int {
        val (bestSequence, maxBananas) = findBestSequence(input)
        println("Best sequence: $bestSequence")
        println("Max Bananas: $maxBananas")
        return maxBananas
    }
    
    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)
    
    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    part1(testInput).println()
    
    check(part1(testInput) == 37327623L)
    val testInput2: List<String> = listOf("1", "2", "3", "2024")
    check(part2(testInput2) == 23)
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
