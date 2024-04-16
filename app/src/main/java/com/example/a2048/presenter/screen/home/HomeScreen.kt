package com.example.a2048.presenter.screen.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.a2048.R
import com.example.a2048.data.MySharedPreferences
import com.example.a2048.databinding.ScreenHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen : Fragment() {
    private var _binding: ScreenHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupOnBackPressed()
        val navController = findNavController()

        binding.btnPlay.setOnClickListener { navController.navigate(R.id.action_homeScreen_to_gameScreen) }

        binding.icInfo.setOnClickListener { navController.navigate(R.id.action_homeScreen_to_screenInfo) }

        binding.icStatistics.setOnClickListener {
            val scores = MySharedPreferences.getBestScores()

            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_statistics)
            dialog.window!!.findViewById<TextView>(R.id.tv_best_first).text = scores[0].toString()
            dialog.window!!.findViewById<TextView>(R.id.tv_best_second).text = scores[1].toString()
            dialog.window!!.findViewById<TextView>(R.id.tv_best_third).text = scores[2].toString()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.CENTER)

            dialog.show()
        }
    }

    private fun setupOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            private var backPressedCount = 0

            override fun handleOnBackPressed() {
                backPressedCount++
                Toast.makeText(requireContext(), "Again for exit", Toast.LENGTH_SHORT).show()
                if (backPressedCount == 1) {
                    lifecycleScope.launch {
                        delay(2000) // Wait for 2 seconds
                        backPressedCount = 0 // Reset the count after 2 seconds
                    }
                } else if (backPressedCount == 2) {
                    requireActivity().finish() // Exit the application
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}