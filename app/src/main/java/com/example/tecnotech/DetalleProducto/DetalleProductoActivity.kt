package com.example.tecnotech.DetalleProducto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityDetalleProductoBinding

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)


        cargarInfoProducto()
    }

    private fun cargarInfoProducto() {
        val nombre = intent.getStringExtra("nombre")
        val precio = intent.getStringExtra("precio")
        val imagen = intent.getIntExtra("imagen", R.drawable.icoproduc)

        binding.nombrePD.text = nombre
        binding.precioPD.text = precio
        binding.imagenVP.setImageResource(imagen)

        val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        binding.descripcionPD.text = loremIpsum
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}