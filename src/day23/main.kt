package day23

import println
import readInput

private const val day = "day23"

fun main() {
    
    fun setUpGraph(connections: List<String>): MutableMap<String, MutableSet<String>> {
        val graph = mutableMapOf<String, MutableSet<String>>()
        
        for (connection in connections) {
            val (a, b) = connection.split("-")
            graph.computeIfAbsent(a) { mutableSetOf() }.add(b)
            graph.computeIfAbsent(b) { mutableSetOf() }.add(a)
        }
        return graph
    }
    
    fun findTriangles(connections: List<String>): Int {
        val graph = setUpGraph(connections)
        
        val triangles = mutableSetOf<Set<String>>()
        
        for ((node, neighbors) in graph) {
            for (neighbor in neighbors) {
                val common = neighbors.intersect(graph[neighbor] ?: emptySet())
                for (shared in common) {
                    val triangle = setOf(node, neighbor, shared)
                    triangles.add(triangle)
                }
            }
        }
        
        val trianglesWithT = triangles.filter { triangle ->
            triangle.any { it.startsWith("t") }
        }
        
        return trianglesWithT.size
    }
    
    
    
    fun findCliques(
        graph: MutableMap<String, MutableSet<String>>,
        potential: Set<String>,
        candidates: Set<String>,
        excluded: Set<String>
    ): List<Set<String>> {
        if (candidates.isEmpty() && excluded.isEmpty()) {
            return listOf(potential)
        }
        
        val cliques = mutableListOf<Set<String>>()
        val pivot = (candidates + excluded).firstOrNull() // Choose a pivot arbitrarily
        val pivotNeighbors = graph[pivot] ?: emptySet()
        
        // Iterate over candidates excluding neighbors of the pivot
        for (node in candidates - pivotNeighbors) {
            val newPotential = potential + node
            val newCandidates = candidates.intersect(graph[node] ?: emptySet())
            val newExcluded = excluded.intersect(graph[node] ?: emptySet())
            cliques += findCliques(graph, newPotential, newCandidates, newExcluded)
        }
        return cliques
    }

    
    
    fun part1(input: List<String>): Int {
        return findTriangles(input)
    }

    fun part2(input: List<String>): String {
        val graph = setUpGraph(input)
        
        val allNodes = graph.keys
        val cliques = findCliques(graph, emptySet(), allNodes, emptySet())
        
        val largestClique = cliques.maxByOrNull { it.size } ?: emptySet()
        
        return largestClique.sorted().joinToString(",")
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/test.txt` file:
    val testInput = readInput("$day/test")
    check(part1(testInput) == 7)

    // Read the input from the `src/input.txt` file.
    val input = readInput("$day/input")
    part1(input).println()
    part2(input).println()
}
