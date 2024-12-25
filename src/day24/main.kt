package day24

import println
import readInput

private const val day = "day24"
lateinit var gateLines: List<String>
lateinit var state: MutableMap<String, Int>
fun main() {
    
    fun parseInputAndSetState(input: List<String>) {
        val blankIndex = input.indexOf("")
        state = mutableMapOf()
        input.subList(0, blankIndex).forEach {
            val line = it.split(": ")
            state[line[0].trim()] = line[1].toInt()
        }
        gateLines = input.subList(blankIndex + 1, input.size)
        var loop = true
        while (loop) {
            var shouldLoopAgain = false
            for (gate in gateLines) {
                // gate: "x0 AND y0 -> z0"
                val regex = Regex("^(.*) (AND|OR|XOR) (.*) -> (.*)$")
                val match = regex.matchEntire(gate) ?: continue
                
                val (_, left, operator, right, output) = match.groupValues
                
                if (state[left] == null || state[right] == null) {
                    shouldLoopAgain = true
                    continue
                }
                
                val lVal = state[left]!!
                val rVal = state[right]!!
                val result = when (operator) {
                    "AND" -> lVal and rVal
                    "OR"  -> lVal or rVal
                    "XOR" -> lVal xor rVal
                    else  -> error("Unknown operator: $operator")
                }
                state[output] = result
            }
            loop = shouldLoopAgain
        }
    }
    

    fun part1(input: List<String>): Long {
        parseInputAndSetState(input)
        return state.filter { it.key.startsWith("z") }
            .toSortedMap()
            .values
            .reversed()
            .joinToString(separator = "")
            .toLong(2)
    }
    
    
    fun findGate(a: String, b: String, op: String): String? {
        val pattern1 = "$a $op $b -> "
        val pattern2 = "$b $op $a -> "
        return gateLines.firstOrNull { line ->
            line.startsWith(pattern1) || line.startsWith(pattern2)
        }?.substringAfter(" -> ")
    }
    fun swap(): MutableList<String> {
        val swapped = mutableListOf<String>()
        
        var c0: String? = null
        
        for (i in 0 until 45) { // 45 registers
            val n = i.toString().padStart(2, '0')
            
            // Find the XOR and AND gates for xN, yN
            var m1: String? = findGate("x$n", "y$n", "XOR")
            var n1: String? = findGate("x$n", "y$n", "AND")
            
            var r1: String? = null
            var z1: String? = null
            var c1: String? = null
            
            if (c0 != null) {
                // r1 = c0 AND m1
                r1 = findGate(c0, m1.orEmpty(), "AND")
                if (r1 == null) {
                  
                    n1 = m1.also { m1 = n1 } // swap n1 and m1 kotlin style
                    swapped.addAll(listOfNotNull(m1, n1))
                    
                    // then look up the gate again
                    r1 = findGate(c0, m1.orEmpty(), "AND")
                }
                
                // z1 = c0 XOR m1
                z1 = findGate(c0, m1.orEmpty(), "XOR")
                
                // If we see something that starts with 'z', swap it
                // in each of m1, n1, r1
                if (m1?.startsWith("z") == true) {
                    m1 = z1.also { z1 = m1 }
                    swapped.addAll(listOfNotNull(m1, z1))
                }
                
                if (n1?.startsWith("z") == true) {
                    n1 = z1.also { z1 = n1 }
                    swapped.addAll(listOfNotNull(n1, z1))
                }
                
                if (r1?.startsWith("z") == true) {
                    r1 = z1.also { z1 = r1 }
                    swapped.addAll(listOfNotNull(r1, z1))
                }
                
                // c1 = r1 OR n1
                if (r1 != null && n1 != null) {
                    c1 = findGate(r1, n1, "OR")
                }
            }
            
            // If c1 is a z-wire but not "z45", swap
            if (c1?.startsWith("z") == true && c1 != "z45") {
                c1 = z1.also { z1 = c1 }
                swapped.addAll(listOfNotNull(c1, z1))
            }
            
            // update c0 for the next iteration
            c0 = if (c0 != null) c1 else n1
        }
        return swapped
    }
    
    
    fun part2(input: List<String>): String { // ignore input, as already initialized
        
        val swapped = swap()
        return swapped.sorted().joinToString(",")
    }

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    //check(part1(testInput) == 2024L)
    part1(testInput).println()
    part2(testInput).println()
    
    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
