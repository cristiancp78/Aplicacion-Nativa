package com.example.tecnotech.Administrador.Administrar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarUsuarioBinding

class ActivityEditarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)


        binding.etNombresC.setText(intent.getStringExtra("nombre"))
        binding.etEmail.setText(intent.getStringExtra("correo"))
        binding.etDireccion.setText(intent.getStringExtra("direccion"))

        binding.btnEditarCliente.setOnClickListener {
            Toast.makeText(this, "Cliente editado", Toast.LENGTH_SHORT).show()
        }

        binding.btnRestablecerContraseA.setOnClickListener {
            Toast.makeText(this, "Se ha enviado un correo de restablecimiento de contraseña al cliente", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}