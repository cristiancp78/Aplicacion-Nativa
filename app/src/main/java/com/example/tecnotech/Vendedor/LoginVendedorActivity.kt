package com.example.tecnotech.Vendedor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Cliente.MainActivityCliente
import com.example.tecnotech.Cliente.RegistroClienteActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityLoginVendedorBinding

class LoginVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginV.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivityVendedor::class.java))
        }

        binding.tvRegistrarV.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroVendedorActivity::class.java))
        }
    }
}