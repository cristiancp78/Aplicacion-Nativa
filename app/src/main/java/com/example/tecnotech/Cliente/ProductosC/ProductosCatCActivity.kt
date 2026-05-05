package com.example.tecnotech.Cliente.ProductosC

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Adaptadores.AdaptadorProductoC
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityProductosCatCactivityBinding

class ProductosCatCActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductosCatCactivityBinding
    private lateinit var productosArrayList: ArrayList<ModeloProductoC>
    private lateinit var adaptadorProductoC: AdaptadorProductoC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoriaRecibida = intent.getStringExtra("categoria")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listarProductos(categoriaRecibida)

        binding.etBuscarProducto.addTextChangedListener (object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(filtro: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    val consulta = filtro.toString()
                    adaptadorProductoC.filter.filter(consulta)

                }catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.IbLimpiarCampo.setOnClickListener {
            val consulta = binding.etBuscarProducto.text.toString().trim()
            if (consulta.isNotEmpty()) {
                binding.etBuscarProducto.setText("")
                Toast.makeText(this, "Campo limpio", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "No se ha ingresado una consulta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listarProductos(categoriaRecibida: String?) {
        productosArrayList = ArrayList()

        val listaCompleta = arrayListOf(
            ModeloProductoC("1", "Mouse Gamer", "80.000",  R.drawable.icoproduc, "Accesorios"),
            ModeloProductoC("2", "Teclado RGB", "150.000",  R.drawable.icoproduc, "Accesorios"),
            ModeloProductoC("3", "iPhone 15", "4.500.000",  R.drawable.icoproduc, "Celulares"),
            ModeloProductoC("4", "Xiaomi RedMi", "1.200.000",  R.drawable.icoproduc, "Celulares"),
            ModeloProductoC("5", "MacBook Pro", "8.000.000",  R.drawable.icoproduc, "Laptops"),
            ModeloProductoC("6", "Audífonos Sony", "900.000",  R.drawable.icoproduc, "Audio"),
            ModeloProductoC("7", "Samsung 24\"", "850.000",  R.drawable.icoproduc, "Pantallas")


        )

        for (producto in listaCompleta) {
            if (producto.categoria == categoriaRecibida) {
                productosArrayList.add(producto)
            }
        }

        adaptadorProductoC = AdaptadorProductoC(this, productosArrayList)
        binding.productosRV.adapter = adaptadorProductoC
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}