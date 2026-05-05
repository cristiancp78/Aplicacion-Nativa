package com.example.tecnotech.Vendedor.Nav_Fragments_Vendedor

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
import com.example.tecnotech.databinding.FragmentCategoriasVBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class FragmentCategoriasV : Fragment() {

    private lateinit var binding: FragmentCategoriasVBinding

    private var imageUri : Uri? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        binding.imgCategorias.setOnClickListener {
            seleccionarImg()
        }

        return binding.root
    }

    private fun seleccionarImg() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
        if (resultado.resultCode == Activity.RESULT_OK){
            val data = resultado.data
            imageUri = data!!.data
            binding.imgCategorias.setImageURI(imageUri)
        }else{
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()

        }
    }
}