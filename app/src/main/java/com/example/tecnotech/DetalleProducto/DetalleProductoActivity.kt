package com.example.tecnotech.DetalleProducto

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Adaptadores.AdaptadorImgSlider
import com.example.tecnotech.Modelos.ModeloImgSlider
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityDetalleProductoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var idProducto = ""

    private lateinit var imagenSlider : ArrayList<ModeloImgSlider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        firebaseAuth = FirebaseAuth.getInstance()

        idProducto = intent.getStringExtra("idProducto").toString()

        cargarImagenesProd()
        cargarInfoProducto()
    }

    private fun cargarInfoProducto() {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val modeloProducto = snapshot.getValue(ModeloProductoC::class.java)

                    val nombre = modeloProducto?.nombre
                    val descripcion = modeloProducto?.descripcion
                    val precio = modeloProducto?.precio
                    val precioDesc = modeloProducto?.precioDesc
                    val notaDesc = modeloProducto?.notaDesc

                    binding.nombrePD.text = nombre
                    binding.descripcionPD.text = descripcion
                    binding.precioPD.text = precio.plus(" COP")

                    if(!precioDesc.equals("") && !notaDesc.equals("")){
                        binding.precioDescPD.text = precioDesc.plus(" COP")
                        binding.notaDescPD.text = notaDesc

                        binding.precioPD.paintFlags = binding.precioPD.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        binding.precioDescPD.visibility = View.GONE
                        binding.notaDescPD.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun cargarImagenesProd() {
        imagenSlider = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    imagenSlider.clear()
                    for (ds in snapshot.children) {
                        try {
                            val modeloImgSlider = ds.getValue(ModeloImgSlider::class.java)
                            imagenSlider.add(modeloImgSlider!!)
                        }catch (e : Exception){

                        }
                    }
                    val adaptadorImgSlider = AdaptadorImgSlider(this@DetalleProductoActivity, imagenSlider)
                    binding.imagenVP.adapter = adaptadorImgSlider
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