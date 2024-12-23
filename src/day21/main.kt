import kotlin.math.abs

enum class ButtonPattern {
    LUA, ULA, LA, LDA, DLA, UA, DA, URA, RUA, RA, DRA, RDA
}
private const val day = "day21"

fun main() {
    val memo = mutableMapOf<Pair<ButtonPattern, Int>, Long>()
    
    fun recurseButtonPresses(pattern: ButtonPattern, depth: Int, memo: MutableMap<Pair<ButtonPattern, Int>, Long>): Long {
        if (depth == 0) {
            return when (pattern) {
                ButtonPattern.LUA, ButtonPattern.ULA, ButtonPattern.LDA, ButtonPattern.DLA, ButtonPattern.URA, ButtonPattern.RUA, ButtonPattern.DRA, ButtonPattern.RDA -> 3
                ButtonPattern.LA, ButtonPattern.UA, ButtonPattern.DA, ButtonPattern.RA -> 2
            }
        }
        
        val cacheKey = pattern to depth
        if (cacheKey in memo) {
            return memo[cacheKey]!!
        }
        
        val result = when (pattern) {
            ButtonPattern.LUA -> recurseButtonPresses(ButtonPattern.DLA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RUA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RA, depth - 1, memo) + 1
            ButtonPattern.ULA -> recurseButtonPresses(ButtonPattern.LA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.DLA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RUA, depth - 1, memo) + 1
            ButtonPattern.LA -> recurseButtonPresses(ButtonPattern.DLA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RUA, depth - 1, memo) + 2
            ButtonPattern.LDA -> recurseButtonPresses(ButtonPattern.DLA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.URA, depth - 1, memo) + 1
            ButtonPattern.DLA -> recurseButtonPresses(ButtonPattern.LDA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.LA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RUA, depth - 1, memo) + 1
            ButtonPattern.UA -> recurseButtonPresses(ButtonPattern.LA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RA, depth - 1, memo)
            ButtonPattern.DA -> recurseButtonPresses(ButtonPattern.LDA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.URA, depth - 1, memo)
            ButtonPattern.URA -> recurseButtonPresses(ButtonPattern.LA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.DRA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.UA, depth - 1, memo)
            ButtonPattern.RUA -> recurseButtonPresses(ButtonPattern.DA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.LUA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RA, depth - 1, memo)
            ButtonPattern.RA -> recurseButtonPresses(ButtonPattern.DA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.UA, depth - 1, memo)
            ButtonPattern.DRA -> recurseButtonPresses(ButtonPattern.LDA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.RA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.UA, depth - 1, memo)
            ButtonPattern.RDA -> recurseButtonPresses(ButtonPattern.DA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.LA, depth - 1, memo) + recurseButtonPresses(ButtonPattern.URA, depth - 1, memo)
        }
        
        memo[cacheKey] = result
        return result
    }
    
    val numericKeypad = mapOf(
        Point(0, 0) to '7',
        Point(0, 1) to '8',
        Point(0, 2) to '9',
        Point(1, 0) to '4',
        Point(1, 1) to '5',
        Point(1, 2) to '6',
        Point(2, 0) to '1',
        Point(2, 1) to '2',
        Point(2, 2) to '3',
        Point(3, 1) to '0',
        Point(3, 2) to 'A'
    )
    
    val reverseNumericKeyPad: Map<Char, Point> = numericKeypad.map { entry -> entry.value to entry.key }.toMap()
    
    fun Point.toButtonPattern(from: Point): ButtonPattern {
        return when {
            this == from -> throw IllegalArgumentException("No movement needed")
            
            // Horizontal alignment
            this.x == from.x -> if (this.y > from.y) ButtonPattern.RA else ButtonPattern.LA
            
            // Vertical alignment
            this.y == from.y -> if (this.x > from.x) ButtonPattern.DA else ButtonPattern.UA
            
            this.y > from.y -> {
                if (this.x > from.x) {
                    if (this.x == 3 && from.y == 0) ButtonPattern.RDA else ButtonPattern.DRA
                } else {
                    ButtonPattern.URA
                }
            }
            
            // Moving diagonally to the left
            this.y < from.y -> {
                if (this.x > from.x) {
                    ButtonPattern.LDA
                } else {
                    if (this.y == 0 && from.x == 3) ButtonPattern.ULA else ButtonPattern.LUA
                }
            }

            
            else -> throw IllegalStateException("Unexpected movement pattern")
        }
    }
    
    fun calulateComplexity(input: List<String>, depth: Int): Long {
        var totalComplexity = 0L
        
        for (code in input) {
            var complexity = 0L
            var currentPosition = reverseNumericKeyPad['A']!!
            
            for (char in code) {
                val charPosition = reverseNumericKeyPad[char]!!
                val currentPattern = charPosition.toButtonPattern(currentPosition)
                
                val extra = maxOf(0, abs(charPosition.y - currentPosition.y) - 1) +
                        maxOf(0, abs(charPosition.x - currentPosition.x) - 1)
                
                currentPattern.let {
                    complexity += recurseButtonPresses(it, depth, memo) + extra
                }
                currentPosition = charPosition
            }
            val numericValue = code.filter { it.isDigit() }.toInt()

            totalComplexity += (complexity * numericValue)
        }
        
        return totalComplexity
    }
    
    fun part1(input: List<String>): Long {
        return calulateComplexity(input, 2)
    }
    
    fun part2(input: List<String>): Long {
        return calulateComplexity(input, 25)
    }
    
    val testInput = readInput("$day/test")
    check(part1(testInput) == 126384L)
    
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
