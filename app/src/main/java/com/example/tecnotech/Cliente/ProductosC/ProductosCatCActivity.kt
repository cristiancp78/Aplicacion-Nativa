package com.example.tecnotech.Cliente.ProductosC

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Adaptadores.AdaptadorProductoC
import com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente.FragmentFavoritosC
import com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.example.tecnotech.Cliente.MainActivityCliente
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityProductosCatCactivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosCatCActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductosCatCactivityBinding
    private lateinit var productosArrayList: ArrayList<ModeloProductoC>
    private lateinit var adaptadorProductoC: AdaptadorProductoC
    private var nombreCat =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreCat = intent.getStringExtra("categoria").toString()

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        listarProductos(nombreCat)

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


    private fun listarProductos(nombreCat: String) {
        productosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()
                    for (ds in snapshot.children){
                        val modelo = ds.getValue(ModeloProductoC::class.java)
                        productosArrayList.add(modelo!!)
                    }

                    adaptadorProductoC = AdaptadorProductoC(this@ProductosCatCActivity, productosArrayList)
                    binding.productosRV.adapter = adaptadorProductoC


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}