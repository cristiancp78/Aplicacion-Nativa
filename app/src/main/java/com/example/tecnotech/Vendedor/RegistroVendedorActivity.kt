package com.example.tecnotech.Vendedor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityRegistroVendedorBinding

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cuenta.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}