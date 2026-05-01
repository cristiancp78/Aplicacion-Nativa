package com.example.tecnotech

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Administrador.LoginAdministradorActivity
import com.example.tecnotech.Administrador.RegistroAdministradorActivity
import com.example.tecnotech.Cliente.LoginClienteActivity
import com.example.tecnotech.Vendedor.LoginVendedorActivity
import com.example.tecnotech.databinding.ActivitySeleccionarTipoBinding

class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionarTipoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipoAdministrador.setOnClickListener {
            startActivity(Intent(this, LoginAdministradorActivity::class.java))
        }
        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this, LoginClienteActivity::class.java))
        }
        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this, LoginVendedorActivity::class.java))
        }
    }
}