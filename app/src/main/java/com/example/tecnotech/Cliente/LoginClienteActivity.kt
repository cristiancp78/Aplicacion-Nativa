package com.example.tecnotech.Cliente

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityLoginClienteBinding

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginC.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivityCliente::class.java))
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroClienteActivity::class.java))
        }
    }

}