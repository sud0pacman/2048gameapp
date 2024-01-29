package com.example.a2048.presenter.screen.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a2048.domain.AppRepository


class GameViewModel : ViewModel() {
    private val repository = AppRepository.getRepository()

    private val _matrixLiveData = MutableLiveData<Array<Array<Int>>>()
    val matrixLiveData: LiveData<Array<Array<Int>>> get() = _matrixLiveData

    private val _scoreLiveData = MutableLiveData<Int>()
    val scoreLiveData: LiveData<Int> get() = _scoreLiveData

    private val _bestScoreLiveData = MutableLiveData<Int>()
    val bestSCoreLiveData: LiveData<Int> get() = _bestScoreLiveData

    private val _isGameOverLiveData = MutableLiveData<Boolean>()
    val isGameOverLiveData: LiveData<Boolean> get() = _isGameOverLiveData

    fun moveToDown() {
        repository.moveToDown()
        loadData()
    }

    fun moveToRight()  {
        repository.moveToRight()
        loadData()
    }

    fun moveToUp() {
        repository.moveToUp()
        loadData()
    }

    fun moveToLeft() {
        repository.moveToLeft()
        loadData()
    }

    fun getLastStep() {
        repository.getLastStep()
        loadData()
    }

    fun saveData() = repository.saveGameData()

    fun loadData() {

        if (repository.isGameOver()) {
            _isGameOverLiveData.value = repository.isGameOver()
        }

        _matrixLiveData.value = repository.getMatrix()
        _scoreLiveData.value = repository.getScore()
        _bestScoreLiveData.value = repository.getBestScore()
    }

    fun restartGame() {
        repository.resetGame()
        loadData()
    }
}