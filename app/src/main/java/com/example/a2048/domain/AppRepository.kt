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
//            arrayOf(512, 1024, 2048, 4096),
//            arrayOf(16, 32, 64, 0)
//        )

//        matrix = arrayOf(
//            arrayOf(512, 0, 0, 0),
//            arrayOf(512, 256, 256, 512),
//            arrayOf(0, 0, 0, 0),
//            arrayOf(0, 0, 0, 0)
//        )

        score = MySharedPreferences.getScore()
        bestScore = MySharedPreferences.getBestScores()[0]

        lastStepMatrix = matrix.deepCopyCopyMatrix()
    }


    private var addElement = mutableListOf(2, 2, 2, 2, 2)

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
        matrix[findPairByRandomIndex.first][findPairByRandomIndex.second] = addElement[Random.nextInt(0, addElement.size)]
    }


    fun moveToLeft() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var isMatrixChanged = false

        for (i in matrix.indices) {
            var index = 0

            for (j in 1 until matrix[i].size) {
                if (matrix[i][j] != 0) {
                    if (matrix[i][index] == 0) {
                        matrix[i][index] = matrix[i][j]
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    } else if (matrix[i][index] == matrix[i][j]) {
                        matrix[i][index] *= 2
                        score += matrix[i][index]
                        matrix[i][j] = 0
                        ++index
                        isMatrixChanged = true
                    } else {
                        ++index
                        if (index != j) {
                            matrix[i][index] = matrix[i][j]
                            matrix[i][j] = 0
                            isMatrixChanged = true
                        }
                    }
                }
            }
        }

        if (isMatrixChanged) {
            addNewElement()
        }
    }

    fun moveToRight() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var isMatrixChanged = false

        for (i in matrix.indices) {
            var index = matrix[i].size - 1

            for (j in matrix[i].size - 2 downTo 0) {
                if (matrix[i][j] != 0) {
                    if (matrix[i][index] == 0) {
                        matrix[i][index] = matrix[i][j]
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    } else if (matrix[i][index] == matrix[i][j]) {
                        matrix[i][index] *= 2
                        score += matrix[i][index]
                        matrix[i][j] = 0
                        --index
                        isMatrixChanged = true
                    } else {
                        --index
                        if (index != j) {
                            matrix[i][index] = matrix[i][j]
                            matrix[i][j] = 0
                            isMatrixChanged = true
                        }
                    }
                }
            }
        }

        if (isMatrixChanged) {
            addNewElement()
        }
    }

    fun moveToUp() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var isMatrixChanged = false

        for (j in matrix[0].indices) {
            var index = 0

            for (i in 1 until matrix.size) {
                if (matrix[i][j] != 0) {
                    if (matrix[index][j] == 0) {
                        matrix[index][j] = matrix[i][j]
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    } else if (matrix[index][j] == matrix[i][j]) {
                        matrix[index][j] *= 2
                        score += matrix[index][j]
                        matrix[i][j] = 0
                        ++index
                        isMatrixChanged = true
                    } else {
                        ++index
                        if (index != i) {
                            matrix[index][j] = matrix[i][j]
                            matrix[i][j] = 0
                            isMatrixChanged = true
                        }
                    }
                }
            }
        }

        if (isMatrixChanged) {
            addNewElement()
        }
    }

    fun moveToDown() {
        lastStepMatrix = matrix.deepCopyCopyMatrix()
        lastStepScore = score

        var isMatrixChanged = false

        for (j in matrix[0].indices) {
            var index = matrix.size - 1

            for (i in matrix.size - 2 downTo 0) {
                if (matrix[i][j] != 0) {
                    if (matrix[index][j] == 0) {
                        matrix[index][j] = matrix[i][j]
                        matrix[i][j] = 0
                        isMatrixChanged = true
                    } else if (matrix[index][j] == matrix[i][j]) {
                        matrix[index][j] *= 2
                        score += matrix[index][j]
                        matrix[i][j] = 0
                        --index
                        isMatrixChanged = true
                    } else {
                        --index
                        if (index != i) {
                            matrix[index][j] = matrix[i][j]
                            matrix[i][j] = 0
                            isMatrixChanged = true
                        }
                    }
                }
            }
        }

        if (isMatrixChanged) {
            addNewElement()
        }
    }


    fun getScore(): Int {
        if (score >= 1024 && !addElement.contains(4)) {
            addElement.add(4)
            addElement.add(4)
        }
        return score
    }

    fun getLastStep() {
        matrix =  lastStepMatrix.deepCopyCopyMatrix()

        if (bestScore == score) bestScore = lastStepScore

        score = lastStepScore
        lastStepScore = score
    }

    fun resetGame() {
        MySharedPreferences.saveBestScores(bestScore)
        MySharedPreferences.clearScoreAndState()
        score = 0
        lastStepScore = 0

        for (index in lastStepMatrix.indices) {
            for (j in lastStepMatrix[index].indices) {
                lastStepMatrix[index][j] = 0
            }
        }

        addElement = mutableListOf(2, 2, 2, 2, 2)

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




























