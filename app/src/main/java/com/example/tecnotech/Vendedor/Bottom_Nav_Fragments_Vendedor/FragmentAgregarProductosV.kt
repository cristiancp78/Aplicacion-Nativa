package com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentAgregarProductosVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.core.view.WindowCompat
import com.example.tecnotech.Adaptadores.AdaptadorImagenSeleccionada
import com.example.tecnotech.Modelos.ModeloCategoriaV
import com.example.tecnotech.Modelos.ModeloImagenSeleccionada
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentAgregarProductosV : Fragment() {

    private lateinit var binding: FragmentAgregarProductosVBinding
    private var imagenUri : Uri? = null

    private lateinit var categoriaArrayList : ArrayList<ModeloCategoriaV>

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarProductosVBinding.inflate(inflater, container, false)

        cargarCategorias()
        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)

        binding.Categoria.setOnClickListener {
            selecCategorias()
        }

        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()
        return binding.root
    }

    private var nombreP = ""
    private var descripcionP = ""
    private var precioP = ""
    private var precioConDescuentoP = ""
    private var notaDescuentoP = ""
    private fun validarInfo() {

    }

    private fun cargarCategorias() {
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoriaV::class.java)
                    categoriaArrayList.add(modelo!!)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private var idcat = ""
    private var tituloCat = ""
    private fun selecCategorias(){
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriaArrayList.indices){
            categoriasArray[i] = categoriaArrayList[i].categoria
        }
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccione una Categoria")
            .setItems(categoriasArray){dialog, wich ->
                idcat = categoriaArrayList[wich].id
                tituloCat = categoriaArrayList[wich].categoria
                binding.Categoria.text = tituloCat
            }
            .show()

    }

    private fun cargarImagenes() {
        adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(requireContext(), imagenSelecArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSeleccionada
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }

    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
        if (resultado.resultCode == Activity.RESULT_OK){
            val data = resultado.data
            imagenUri = data!!.data
            val tiempo = System.currentTimeMillis()
            val tiempoString = "$tiempo"

            val modeloImgSel = ModeloImagenSeleccionada(tiempoString,imagenUri,null,false)
            imagenSelecArrayList.add(modeloImgSel)
            cargarImagenes()
        }else{
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()
        }

    }


}