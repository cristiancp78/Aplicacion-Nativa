package com.example.tecnotech.Administrador.Administrar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarAdministradoresBinding

class ActivityEditarAdministradores : AppCompatActivity() {

    private lateinit var binding: ActivityEditarAdministradoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarAdministradoresBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)


        binding.etNombresA.setText(intent.getStringExtra("nombre"))
        binding.etEmail.setText(intent.getStringExtra("correo"))

        binding.btnEditarAdministrador.setOnClickListener {
            Toast.makeText(this, "Administrador editado", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}