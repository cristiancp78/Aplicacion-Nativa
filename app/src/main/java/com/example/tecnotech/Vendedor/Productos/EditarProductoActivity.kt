package com.example.tecnotech.Vendedor.Productos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarProductoBinding

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarProductoBinding.inflate(layoutInflater)
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