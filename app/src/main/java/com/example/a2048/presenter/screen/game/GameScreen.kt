package com.example.a2048.presenter.screen.game

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.data.SideEnum
import com.example.a2048.databinding.ScreenGameBinding
import com.example.a2048.utils.MyBackgroundUtil
import com.example.a2048.utils.MyTouchListener
import jp.wasabeef.blurry.Blurry


class GameScreen : Fragment(R.layout.screen_game) {
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val views = ArrayList<AppCompatTextView>()
    private val viewModel = GameViewModel()
    private lateinit var matrix: Array<Array<Int>>
    private  var gameOver = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOnBackPressed()
        loadViews()
        attachTouchListener()


        viewModel.matrixLiveData.observe(viewLifecycleOwner) {
            loadDataToUI(it)
        }

        viewModel.scoreLiveData.observe(viewLifecycleOwner) {
            loadScore()
        }

        viewModel.bestSCoreLiveData.observe(viewLifecycleOwner) {
            loadBestScore()
        }

        viewModel.isGameOverLiveData.observe(viewLifecycleOwner) {
            gameOver = true
            openGameOverUI()
        }

        viewModel.loadData()
    }

    private fun loadDataToUI(matrix: Array<Array<Int>>) {
        this.matrix = matrix

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) views[i * 4 + j].text = ""
                else views[i * 4 + j].text = "${matrix[i][j]}"

                views[i * 4 + j].setBackgroundResource(MyBackgroundUtil.backgroundByAmount(matrix[i][j]))
            }
        }
    }

    private fun loadViews() {
        for (i in 0 until binding.mainView.childCount) {
            val line = binding.mainView[i] as LinearLayoutCompat

            for (j in 0 until line.childCount) {
                views.add(line[j] as AppCompatTextView)
            }
        }

        binding.tvScore.text = viewModel.scoreLiveData.value.toString()

        binding.icHome.setOnClickListener { findNavController().popBackStack() }

        binding.icBack.setOnClickListener {
            if (gameOver) {
                closeGameOverUI()
                gameOver = false
            }

            viewModel.getLastStep()
        }

        binding.restart.setOnClickListener {
            if (!gameOver) openRestartGameUI()
            else {
                closeGameOverUI()
                gameOver = false
                viewModel.restartGame()
            }
        }

        binding.tvYes.setOnClickListener {
            viewModel.restartGame()
            closeResetGameUI()
        }

        binding.tvNo.setOnClickListener { closeResetGameUI() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener() {
        val myTouchListener = MyTouchListener(requireContext())

        myTouchListener.setActionSideEnumListener {
            when (it) {
                SideEnum.DOWN -> viewModel.moveToDown()
                SideEnum.RIGHT -> viewModel.moveToRight()
                SideEnum.UP -> viewModel.moveToUp()
                SideEnum.LEFT -> viewModel.moveToLeft()
            }
        }

        binding.mainView.setOnTouchListener(myTouchListener)
    }

    private fun loadScore() {
        binding.tvScore.text = viewModel.scoreLiveData.value.toString()
    }

    private fun loadBestScore() {
        binding.tvHighScore.text = viewModel.bestSCoreLiveData.value.toString()
    }


    private fun setupOnBackPressed() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // create a dialog to ask yes no questions whether or not the user wants to exit
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun openRestartGameUI() {
        binding.cRestartGame.isVisible = true

        val animationOpen =
            AnimationUtils.loadAnimation(requireContext(), R.anim.game_over_open)
        animationOpen.duration = 500

        binding.cRestartGame.startAnimation(animationOpen)
    }



    private fun closeResetGameUI() {
        val animationOpen =
            AnimationUtils.loadAnimation(requireContext(), R.anim.game_over_close)
        animationOpen.duration = 500

        binding.cRestartGame.startAnimation(animationOpen)

        binding.cRestartGame.isVisible = false
    }

    private fun openGameOverUI() {
        binding.cGameOver.isVisible = true

        val animationOpen =
            AnimationUtils.loadAnimation(requireContext(), R.anim.game_over_open)
        animationOpen.duration = 600

        binding.cGameOver.startAnimation(animationOpen)
    }

    private fun closeGameOverUI() {
        val animationOpen =
            AnimationUtils.loadAnimation(requireContext(), R.anim.game_over_close)
        animationOpen.duration = 500

        binding.cGameOver.startAnimation(animationOpen)

        binding.cGameOver.isVisible = false
    }


    override fun onStop() {
        viewModel.saveData()
        super.onStop()
    }
}