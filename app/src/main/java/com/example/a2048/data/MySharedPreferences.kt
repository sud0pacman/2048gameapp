package com.example.a2048.data

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences {

    companion object {
        private val PREFS_NAME = "2048Prefs"
        private val MATRIX_KEY = "matrixKey"
        private val SCORE_KEY = "scoreKey"
        private val BEST_SCORE_KEY = "bestScoreKey"

        private lateinit var pref: SharedPreferences

        fun init(context: Context) {
            if (!(Companion::pref.isInitialized)) pref =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

//            pref.edit().clear().apply()
        }


        fun saveGame(matrixString: String) {
            val editor = pref.edit()

            editor.putString(MATRIX_KEY, matrixString)
            editor.apply()
        }

        fun getState(): Array<Array<Int>> {
            val matrixString = pref.getString(MATRIX_KEY, null)
            var matrix = arrayOf(
                arrayOf(0, 0, 0, 0),
                arrayOf(0, 0, 0, 0),
                arrayOf(0, 0, 0, 0),
                arrayOf(0, 0, 0, 0)
            )

            if (matrixString != null) {
                val values = matrixString.split(",").map { it.toInt() }
                matrix = Array(4) { i -> Array(4) { j -> values[i * 4 + j] } }
            }

            return matrix
        }


        fun saveScore(score: Int) {
            pref.edit().putInt(SCORE_KEY, score).apply()
        }

        fun getScore(): Int = pref.getInt(SCORE_KEY, 0)

        fun clearScoreAndState() {
            pref.edit().remove(MATRIX_KEY).apply()
            pref.edit().remove(SCORE_KEY).apply()
        }

        fun saveBestScores(score: Int) {
            val scores = getBestScores().toMutableList()

            if (score >= scores[0]) {
                if (score == scores[0]) return
                scores[2] = scores[1]
                scores[1] = scores[0]
                scores[0] = score
            } else if (score >= scores[1]) {
                if (score == scores[1]) return
                scores[2] = scores[1]
                scores[1] = score
            } else if (score >= scores[2]) {
                if (score == scores[1]) return
                scores[2] = score
            }

            val editor = pref.edit()

            for (i in scores.indices) {
                editor.putInt("$BEST_SCORE_KEY$i", scores[i])
            }

            editor.apply()
        }

        fun getBestScores(): List<Int> {
            val bestScores = mutableListOf<Int>()

            for (i in 0 until 3) {
                val score = pref.getInt("$BEST_SCORE_KEY$i", 0)
                bestScores.add(score)
            }

            return bestScores
        }
    }
}