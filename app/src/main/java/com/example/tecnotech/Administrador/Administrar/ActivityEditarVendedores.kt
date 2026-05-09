package com.example.tecnotech.Administrador.Administrar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarVendedoresBinding

class ActivityEditarVendedores : AppCompatActivity() {

    private lateinit var binding: ActivityEditarVendedoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarVendedoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)


        binding.etNombresV.setText(intent.getStringExtra("nombre"))
        binding.etEmail.setText(intent.getStringExtra("correo"))
        binding.etNombreTienda.setText(intent.getStringExtra("nombreTienda"))

        binding.btnEditarVendedor.setOnClickListener {
            Toast.makeText(this, "Vendedor editado", Toast.LENGTH_SHORT).show()
        }

        binding.btnRestablecerContraseA.setOnClickListener {
            Toast.makeText(this, "Se ha enviado un correo de restablecimiento de contraseña al vendedor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}