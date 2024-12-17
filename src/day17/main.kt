package day17

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import println
import readInput
import java.util.concurrent.atomic.AtomicLong

private const val day = "day17"

fun main() {
        val powersOfTwo = LongArray(64) { 1L shl it }
        
        fun runProgram(aInit: Long, bInit: Long, cInit: Long, program: List<Long>): List<Long> {
            var instructionPointer = 0
            var regA = aInit
            var regB = bInit
            var regC = cInit
            val output = mutableListOf<Long>()
            
            // Resolve combo operand to Long
            fun resolveCombo(operand: Long): Long {
                return when (operand) {
                    in 0..3 -> operand.toLong()
                    4L -> regA
                    5L -> regB
                    6L -> regC
                    else -> throw IllegalArgumentException("Invalid combo operand: $operand")
                }
            }
            
            while (instructionPointer < program.size) {
                val opcode = program[instructionPointer]
                val operand = program[instructionPointer + 1]
                
                when (opcode) {
                    0L -> regA /= powersOfTwo[resolveCombo(operand).toInt()] // Division using precomputed powers
                    1L -> regB = regB xor operand.toLong()
                    2L -> regB = resolveCombo(operand) % 8
                    3L -> {
                        if (regA != 0L) {
                            instructionPointer = operand.toInt()
                            continue
                        }
                    }
                    4L -> regB = regB xor regC
                    5L -> output.add((resolveCombo(operand) % 8))
                    6L -> regB = regA / powersOfTwo[resolveCombo(operand).toInt()]
                    7L -> regC = regA / powersOfTwo[resolveCombo(operand).toInt()]
                    else -> throw IllegalArgumentException("Unknown opcode: $opcode")
                }
                instructionPointer += 2
            }
            
            return output
        }
    fun part1(input: List<String>): String {
        val aInit = input[0].substringAfter("Register A: ").toLong()
        val bInit = input[1].substringAfter("Register B: ").toLong()
        val cInit = input[2].substringAfter("Register C: ").toLong()
        val programm = input[4].substringAfter("Program: ").split(",").map { it.toLong() }
        return runProgram(aInit,bInit,cInit,programm).joinToString(",")
    }
    

    
    fun part2Parallel(input: List<String>): Long {
        val program = input[4].substringAfter("Program: ").split(",").map { it.toLong() }
        val result = AtomicLong(-1)
        val chunkSize = 100_000_000L
        val rangeStart = 100_000_000_000_000L
        val rangeEnd = 1_000_000_000_000_000L
        
        runBlocking {
            val workers = Runtime.getRuntime().availableProcessors()
            val workChannel = Channel<LongRange>(capacity = workers)
            
            launch {
                for (start in rangeStart until rangeEnd step chunkSize) {
                    if (result.get() > 0) break
                    workChannel.send(start until (start + chunkSize).coerceAtMost(rangeEnd))
                }
                workChannel.close()
            }
            
            repeat(workers) {
                launch(Dispatchers.Default) {
                    for (range in workChannel) {
                        //println("Processing chunk: ${range.first} to ${range.last}")
                        for (candidateA in range) {
                            if (result.get() > 0) break
                            val output = runProgram(candidateA, 0, 0, program)
                            if (output == program) {
                                println("Result found: $candidateA")
                                result.compareAndSet(-1, candidateA)
                                return@launch
                            }
                        }
                    }
                }
            }
        }
        
        return result.get()
    }
    
    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    part1(testInput).println()
    //check(part2(testInput) == 117440L)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2Parallel(input).println()
}
