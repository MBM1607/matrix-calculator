package processor

import java.util.Scanner
import kotlin.math.min
import kotlin.math.pow

val scanner = Scanner(System.`in`)

fun readMatrix(messageInfix: String = ""): Array<DoubleArray> {
    print("Enter size of ${messageInfix}matrix: ")
    val row = scanner.nextInt()
    val col = scanner.nextInt()
    val matrix = Array(row) { DoubleArray(col) }

    println("Enter ${messageInfix}matrix:")
    for (i in 0 until row) {
        for (j in 0 until col) {
            matrix[i][j] = scanner.nextDouble()
        }
    }
    return matrix
}

fun printMatrix(matrix: Array<DoubleArray>) {
    for (i in matrix.indices) {
        var line = ""
        for (j in matrix[0].indices) {
            line += "${matrix[i][j]} "
        }
        println(line)
    }
}

fun addMatrices(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>, log: Boolean = true): Array<DoubleArray> {
    if (log) println("The result is:")
    val sumMatrix = Array(matrix1[0].size) { DoubleArray(matrix2.size) }

    if (matrix1.size == matrix2.size && matrix1[0].size == matrix2[0].size) {
        for (i in matrix1.indices) {
            for (j in matrix1[0].indices)
                sumMatrix[i][j] = matrix1[i][j] + matrix2[i][j]
        }
    } else {
        if (log) println("The operation cannot be performed.")
    }
    return sumMatrix
}

fun multiplyMatrices(matrix1: Array<DoubleArray>,
                     matrix2: Array<DoubleArray>, log: Boolean = true): Array<DoubleArray> {
    if (log) println("The result is:")
    val productMatrix = Array(matrix1.size) { DoubleArray(matrix2[0].size) }

    if (matrix1[0].size == matrix2.size) {
        for (i in matrix1.indices) {
            for (j in matrix2[0].indices) {
                var sum = 0.0
                for (k in matrix1[0].indices) {
                    sum += matrix1[i][k] * matrix2[k][j]
                }
                productMatrix[i][j] = sum
            }
        }
    } else {
        if (log) println("The operation cannot be performed.")
    }
    return productMatrix
}

fun multiplyByConstant(constant: Double, matrix: Array<DoubleArray>, log: Boolean = true): Array<DoubleArray> {
    if (log) println("The result is:")
    val productMatrix = Array(matrix.size) { DoubleArray(matrix[0].size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            productMatrix[i][j] = constant * matrix[i][j]
        }
    }
    return productMatrix
}

fun transposeMatrix() {
    println()
    println("1. Main diagonal\n" +
            "2. Side diagonal\n" +
            "3. Vertical line\n" +
            "4. Horizontal line")
    print("Your choice: ")
    val choice = readLine()!!
    println("The result is:")
    when (choice) {
        "1" -> printMatrix(transposeMain(readMatrix()))
        "2" -> printMatrix(transposeSide(readMatrix()))
        "3" -> printMatrix(transposeVertical(readMatrix()))
        "4" -> printMatrix(transposeHorizontal(readMatrix()))
        else -> println("Invalid choice")
    }
}

fun transposeMain(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val transpose = Array(matrix[0].size) { DoubleArray(matrix.size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            transpose[j][i] = matrix[i][j]
        }
    }
    return transpose
}

fun transposeSide(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val transpose = Array(matrix[0].size) { DoubleArray(matrix.size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            transpose[matrix.lastIndex - j][matrix[0].lastIndex - i] = matrix[i][j]
        }
    }
    return transpose
}

fun transposeVertical(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val transpose = Array(matrix[0].size) { DoubleArray(matrix.size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            transpose[i][matrix[0].lastIndex - j] = matrix[i][j]
        }
    }
    return transpose
}

fun transposeHorizontal(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val transpose = Array(matrix[0].size) { DoubleArray(matrix.size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            transpose[matrix.lastIndex - i][j] = matrix[i][j]
        }
    }
    return transpose
}

// Get the minor matrix from the original matrix after excluding the given row and column
fun getCofactor(matrix: Array<DoubleArray>, row: Int, col: Int): Double {
    val minor = Array(matrix[0].size - 1) { DoubleArray(matrix.size - 1) }
    val temp = matrix.filterIndexed { i, _ -> i != row }.toTypedArray()
    for (i in temp.indices) {
        minor[i] = temp[i].filterIndexed { j, _ -> j != col }.toDoubleArray()
    }
    return determinant(minor) * (-1.0).pow(row + col)
}

// Get the determinant of a matrix
fun determinant(matrix: Array<DoubleArray>): Double {
    return if (matrix.size == 2) {
        matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
    } else {
        var result = 0.0
        for (j in matrix.indices)
            result += getCofactor(matrix, 0, j) * matrix[0][j]
        result
    }
}

fun calcDeterminant() {
    val matrix = readMatrix()
    println("The result is:")
    if (matrix.size == matrix[0].size) {
        println(determinant(matrix))
    } else {
        println("The operation cannot be performed.")
    }
}

fun getAdjoint(matrix: Array<DoubleArray>): Array<DoubleArray> {
    val adjoint = Array(matrix.size) { DoubleArray(matrix[0].size) }
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            adjoint[i][j] = getCofactor(matrix, i, j)
        }
    }
    return transposeMain(adjoint)
}

fun inverseMatrix(matrix: Array<DoubleArray>, log: Boolean = true): Array<DoubleArray> {
    if (log) println("The result is:")
    val det = determinant(matrix)
    return if (det != 0.0) {
        multiplyByConstant(1 / det, getAdjoint(matrix), false)
    } else {
        if (log) println("This matrix doesn't have an inverse.")
        Array(matrix.size) { DoubleArray(matrix[0].size) }
    }
}

fun main() {
    while (true) {
        println("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit")
        print("Your choice: ")
        when (readLine()) {
            "0" -> return
            "1" -> printMatrix(addMatrices(readMatrix("first "), readMatrix("second ")))
            "2" -> {
                val matrix = readMatrix()
                print("Enter constant:")
                printMatrix(multiplyByConstant(scanner.nextDouble(), matrix))
            }
            "3" -> printMatrix(multiplyMatrices(readMatrix("first "), readMatrix("second ")))
            "4" -> transposeMatrix()
            "5" -> calcDeterminant()
            "6" -> printMatrix(inverseMatrix(readMatrix()))
            else -> println("Invalid choice")
        }
        println()
    }
}