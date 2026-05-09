package com.example.tecnotech.Administrador.Administrar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarProductoAdminBinding

class ActivityEditarProductoAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityEditarProductoAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarProductoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)


        binding.etNombreP.setText(intent.getStringExtra("nombre"))
        binding.etPrecioP.setText(intent.getStringExtra("precio"))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
