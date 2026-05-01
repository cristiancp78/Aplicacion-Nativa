package com.example.tecnotech.Administrador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityLoginAdministradorBinding

class LoginAdministradorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdministradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginA.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivityAdministrador::class.java))
        }

    }
}