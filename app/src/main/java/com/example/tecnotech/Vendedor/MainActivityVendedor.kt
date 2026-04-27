package com.example.tecnotech.Vendedor

import android.app.Fragment
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentAgregarProductosV
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductosV
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentOrdenesV
import com.example.tecnotech.Vendedor.Nav_Fragments_Vendedor.FragmentInicioV
import com.example.tecnotech.Vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaV
import com.example.tecnotech.databinding.ActivityMainVendedorBinding
import com.google.android.material.navigation.NavigationView

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(
            this,
            binding.drawerlayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.inicio_v)
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio_v -> {
                replaceFragment(FragmentInicioV())
            }
            R.id.mi_tienda_v -> {
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.cerrar_sesion_v -> {
                Toast.makeText(applicationContext, "Saliste de la aplicaicon", Toast.LENGTH_SHORT).show()
            }
            R.id.mis_productos_v -> {
                replaceFragment(FragmentMisProductosV())
            }
            R.id.agregar_producto_v -> {
                replaceFragment(FragmentAgregarProductosV())
            }
            R.id.mis_ordenes_v -> {
                replaceFragment(FragmentOrdenesV())
            }
        }
        binding.drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }
}