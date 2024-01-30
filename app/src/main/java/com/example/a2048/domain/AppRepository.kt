package com.example.a2048.domain

import android.annotation.SuppressLint
import android.content.Context
import com.example.a2048.data.MySharedPreferences
import com.example.a2048.utils.deepCopyCopyMatrix
import kotlin.random.Random

class AppRepository(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: AppRepository

        fun getInstance(context: Context): AppRepository {
            if (!(Companion::instance.isInitialized)) {
                instance = AppRepository(context)
                instance.loadGameData()  // Call loadGameData after initializing
            }
            return instance
        }

        fun getRepository(): AppRepository = instance
    }

    private var score = 0
    private var bestScore: Int = 0
    private var lastStepScore: Int = 0

    private var matrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )

    private var lastStepMatrix = matrix.copyOf()


    fun saveGameData() {
        val matrixString = matrix.flatten().joinToString(",")
        MySharedPreferences.saveGame(matrixString)
        MySharedPreferences.saveScore(score)
        MySharedPreferences.saveBestScores(bestScore)
    }


    private fun loadGameData() {
        matrix = MySharedPreferences.getState()

        if (matrix.sumOf { row -> row.count { it == 0 } } == 16) {
            addNewElement()
            addNewElement()
        }

//        matrix = arrayOf(
//            arrayOf(2, 4, 8, 16),
//            arrayOf(32, 64, 128, 256),
//            arrayOf(512, 1024, 2048, 4),
//            arrayOf(16, 32, 64, 4)
//        )

        score = MySharedPreferences.getScore()
        bestScore = MySharedPreferences.getBestScores()[0]

        lastStepMatrix = matrix.deepCopyCopyMatrix()
    }


    private var addElement = 2

    fun getMatrix(): Array<Array<Int>> = matrix


    private fun addNewElement() {
        val empty = ArrayList<Pair<Int, Int>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) empty.add(
                    Pair(
                        i,
                        j
                    )
                )   // bo'sh koordinatalarni saqlab qoladi
            }
        }

        if (empty.isEmpty()) return
        val randomIndex = Random.nextInt(0, empty.size)
        val findPairByRandomIndex = empty[randomIndex]
        matrix[findPairByRandomIndex.first][findPairByRandomIndex.second] = addElement
    }


    fun moveToLeft() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var index: Int
        var isCanAddNewElement = false
        var isMatrixChanged = false // Flag to track if any changes were made to the matrix

        for (i in matrix.indices) {
            index = 0

            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0 || j == 0) {
                    continue
                }

                if (matrix[i][index] == matrix[i][j]) {
                    matrix[i][index] = matrix[i][j] * 2

                    score += matrix[i][index]

                    matrix[i][j] = 0
                    ++index
                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else if (matrix[i][index] == 0) {
                    matrix[i][index] = matrix[i][j]
                    matrix[i][j] = 0
                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else {
                    matrix[i][++index] = matrix[i][j]
                    if (index != j) {
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    }
                }
            }
        }

        // Only add a new element if the matrix has changed
        if (isMatrixChanged && isCanAddNewElement) {
            addNewElement()
        }
    }

    fun moveToRight() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var index: Int
        var isCanAddNewElement = false
        var isMatrixChanged = false


        for (i in matrix.indices) {
            index = 3

            for (j in matrix[i].size - 1 downTo 0) {

                if (matrix[i][j] == 0 || j == 3) {
                    continue
                }

                if (matrix[i][index] == matrix[i][j]) {
                    matrix[i][index] = matrix[i][j] * 2

                    score += matrix[i][index]

                    matrix[i][j] = 0
                    --index

                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else if (matrix[i][index] == 0) {
                    matrix[i][index] = matrix[i][j]

                    matrix[i][j] = 0

                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else {
                    matrix[i][--index] = matrix[i][j]

                    if (index != j) {
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    }
                }
            }
        }


        if (isCanAddNewElement && isMatrixChanged) addNewElement()
    }

    fun moveToUp() {
        lastStepScore = score
        lastStepMatrix = matrix.deepCopyCopyMatrix()

        var index: Int
        var isCanAddNewElement = false
        var isMatrixChanged = false // Flag to track if any changes were made to the matrix

        for (j in matrix[0].indices) { // Iterate over columns
            index = 0 // Start from the top row

            for (i in matrix.indices) { // Iterate over rows
                if (matrix[i][j] == 0 || i == 0) {
                    continue
                }

                if (matrix[index][j] == matrix[i][j]) {
                    matrix[index][j] = matrix[i][j] * 2
                    score += matrix[index][j]
                    matrix[i][j] = 0
                    ++index
                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else if (matrix[index][j] == 0) {
                    matrix[index][j] = matrix[i][j]
                    matrix[i][j] = 0
                    isCanAddNewElement = true
                    isMatrixChanged = true
                } else {
                    matrix[++index][j] = matrix[i][j]
                    if (index != i) {
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    }
                }
            }
        }

        // Only add a new element if the matrix has changed
        if (isMatrixChanged && isCanAddNewElement) {
            addNewElement()
        }
    }

    fun moveToDown() {
        lastStepScore = score
        lastStepMatrix = matrix.deepCopyCopyMatrix()

        var index: Int
        var isCanAddNewElement = false
        var isMatrixChanged = false // Flag to track if any changes were made to the matrix

        for (j in matrix[0].indices) { // Iterate over columns
            index = matrix.size - 1 // Start from the bottom row

            for (i in matrix.indices.reversed()) { // Iterate over rows in reverse order
                if (matrix[i][j] == 0 || i == matrix.size - 1) {
                    continue
                }

                if (matrix[index][j] == matrix[i][j]) {
                    matrix[index][j] = matrix[i][j] * 2
                    score += matrix[index][j]
                    matrix[i][j] = 0
                    --index
                    isCanAddNewElement = true
                    isMatrixChanged = true

                } else if (matrix[index][j] == 0) {
                    matrix[index][j] = matrix[i][j]
                    matrix[i][j] = 0
                    isCanAddNewElement = true
                    isMatrixChanged = true

                } else {
                    matrix[--index][j] = matrix[i][j]
                    if (index != i) {
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    }
                }
            }
        }

        // Only add a new element if the matrix has changed
        if (isMatrixChanged && isCanAddNewElement) {
            addNewElement()
        }
    }


    fun getScore(): Int = score

    fun getLastStep() {
        matrix =  lastStepMatrix.deepCopyCopyMatrix()

        if (bestScore == score) bestScore = lastStepScore

        score = lastStepScore
        lastStepScore = score
    }

    fun resetGame() {
        MySharedPreferences.saveBestScores(bestScore)
        MySharedPreferences.clearScoreAndState()

        for (index in lastStepMatrix.indices) {
            for (j in lastStepMatrix[index].indices) {
                lastStepMatrix[index][j] = 0
            }
        }

        loadGameData()
    }

    fun getBestScore(): Int {
        if (bestScore < score) bestScore = score
        return bestScore
    }

    fun isGameOver(): Boolean {
        // Check if there are any empty cells
        for (row in matrix) {
            if (0 in row) {
                return false
            }
        }

        // Check if there are any adjacent cells with the same value
        for (i in matrix.indices) {
            for (j in 0 until matrix[i].size - 1) {
                if (matrix[i][j] == matrix[i][j + 1] || matrix[j][i] == matrix[j + 1][i]) {
                    return false
                }
            }
        }

        return true
    }

}




























