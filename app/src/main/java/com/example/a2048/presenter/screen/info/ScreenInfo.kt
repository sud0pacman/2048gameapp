package com.example.a2048.presenter.screen.info

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a2048.R

class ScreenInfo : Fragment(R.layout.screen_info) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageView>(R.id.ic_info_back_btn).setOnClickListener { findNavController().popBackStack() }
    }
}