package com.example.tecnotech.Administrador

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentOrdenesA
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentPanelA
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentPerfilA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentAdministradoresA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentProductosA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentUsuariosA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentVendedoresA
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityMainAdministradorBinding
import com.google.android.material.navigation.NavigationView

class MainActivityAdministrador : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainAdministradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(Toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            Toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        binding.appBarMain.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.panel_a ->{
                    replaceFragment(FragmentPanelA())
                }
                R.id.ordenes_a ->{
                    replaceFragment(FragmentOrdenesA())
                }
                R.id.perfil_a ->{
                    replaceFragment(FragmentPerfilA())
                }
            }
            true
        }

        replaceFragment(FragmentPanelA())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.productos_a -> {
                replaceFragment(FragmentProductosA())
            }
            R.id.usuarios_a -> {
                replaceFragment(FragmentUsuariosA())
            }
            R.id.vendedores_a -> {
                replaceFragment(FragmentVendedoresA())
            }
            R.id.administradores_a -> {
                replaceFragment(FragmentAdministradoresA())
            }
            R.id.cerrar_sesion_A -> {
                Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
            }
            R.id.panel_a -> {
                replaceFragment(FragmentPanelA())
            }
            R.id.ordenes_a -> {
                replaceFragment(FragmentOrdenesA())
            }
            R.id.perfil_a->{
                replaceFragment(FragmentPerfilA())
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }

}