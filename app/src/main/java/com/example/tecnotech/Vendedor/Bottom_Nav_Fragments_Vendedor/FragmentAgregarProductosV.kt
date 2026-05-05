package com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.app.Activity
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
import com.example.tecnotech.Modelos.ModeloImagenSeleccionada


class FragmentAgregarProductosV : Fragment() {

    private lateinit var binding: FragmentAgregarProductosVBinding
    private var imagenUri : Uri? = null

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarProductosVBinding.inflate(inflater, container, false)

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)


        cargarImagenes()
        return binding.root
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