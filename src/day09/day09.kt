package day09

import println
import readInput

private const val day = "day09"

fun main() {
    
    fun splitListByIndices(input: List<String>): Pair<MutableList<Long>, MutableList<Long>> {
        val inp = input.first().map { it.digitToInt() }
        val evenIndices = inp.filterIndexed { index, _ -> index % 2 == 0 }.map { it.toLong() }.toMutableList()
        val oddIndices = inp.filterIndexed { index, _ -> index % 2 != 0 }.map { it.toLong() }.toMutableList()
        return Pair(evenIndices, oddIndices)
    }
    fun calculateChecksum(disk: List<Int>): Long {
        return disk.withIndex()
            .filter { it.value != -1 }
            .sumOf { (index, value) -> index.toLong() * value }
    }
    
    fun part1(input: List<String>): Long {
        val (memory, freeSpace) = splitListByIndices(input)
        val totalMemory = memory.sumOf { it }
        val mutableList = mutableListOf<Int>()
        var lastIndex = memory.lastIndex
        memory.forEachIndexed { index, blocks ->
            repeat (blocks.toInt()) {
                mutableList.add(index)
            }
            if (index in freeSpace.indices) {
                repeat (freeSpace[index].toInt()) {
                    while (memory[lastIndex] == 0L) {
                        lastIndex--
                    }
                    mutableList.add(lastIndex)
                    memory[lastIndex]--
                }
            }
        }
        return calculateChecksum(mutableList.take(totalMemory.toInt()))
    }
    
    
    fun findFreeSpans(disk: List<Int>): List<Pair<Int, Int>> =
        disk.foldIndexed(mutableListOf()) { index, spans, value ->
            if (value == -1) {
                if (spans.isEmpty() || spans.last().second + spans.last().first != index) {
                    spans.add(index to 1)
                } else {
                    spans[spans.lastIndex] = spans.last().first to (spans.last().second + 1)
                }
            }
            spans
        }
    
    fun part2(input: List<String>): Long {
        val (files, freeSpace) = splitListByIndices(input)
        
        val disk = mutableListOf<Int>().apply {
            files.forEachIndexed { fileId, size ->
                repeat(size.toInt()) { add(fileId) }
                if (fileId < freeSpace.size) repeat(freeSpace[fileId].toInt()) { add(-1) }
            }
        }
        
        files.indices.reversed().forEach { fileId ->
            val fileSize = files[fileId].toInt()
            val freeSpans = findFreeSpans(disk)
            
            val targetSpan = freeSpans.firstOrNull { (_, size) -> size >= fileSize }
            val currentPositions = disk.withIndex().filter { it.value == fileId }.map { it.index }
            
            if (targetSpan != null && currentPositions.first() > targetSpan.first) {
                currentPositions.forEach { disk[it] = -1 }
                
                repeat(fileSize) { disk[targetSpan.first + it] = fileId }
            }
        }
        
        
        return calculateChecksum(disk)
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
