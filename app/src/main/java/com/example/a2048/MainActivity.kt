package com.example.a2048

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.get
import androidx.core.view.indices
import com.example.a2048.data.SideEnum
import com.example.a2048.databinding.ActivityMainBinding
import com.example.a2048.domain.AppRepository
import com.example.a2048.presenter.screen.game.GameScreen
import com.example.a2048.utils.MyBackgroundUtil
import com.example.a2048.utils.MyTouchListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}