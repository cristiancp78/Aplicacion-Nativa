package com.example.tecnotech.Cliente

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityRegistroClienteBinding

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cuenta.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}