package com.example.tecnotech.Cliente

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.example.tecnotech.Cliente.Nav_Fragments_Cliente.FragmentInicioC
import com.example.tecnotech.Cliente.Nav_Fragments_Cliente.FragmentMiPerfilC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        replaceFragment(FragmentInicioC())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio_c -> {
                replaceFragment(FragmentInicioC())
            }
            R.id.mi_perfil_C -> {
                replaceFragment(FragmentMiPerfilC())
            }
            R.id.cerrar_sesion_c -> {
                Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
            }
            R.id.tienda_c -> {
                replaceFragment(FragmentTiendaC())
            }
            R.id.Ordenes_c -> {
                replaceFragment(FragmentMisOrdenesC())
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }
}