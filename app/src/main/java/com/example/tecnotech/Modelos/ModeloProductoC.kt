package com.example.tecnotech.Modelos

class ModeloProductoC {
    var id: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var precio: String = ""
    var categoria: String = ""
    var precioDesc: String = ""
    var notaDesc: String = ""
    var favoritos = false

    constructor()
    constructor(
        id: String,
        nombre: String,
        descripcion: String,
        precio: String,
        categoria: String,
        precioDesc: String,
        notaDesc: String,
        favoritos: Boolean
    ){
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
        this.categoria = categoria
        this.precioDesc = precioDesc
        this.notaDesc = notaDesc
        this.favoritos = favoritos
    }

}
