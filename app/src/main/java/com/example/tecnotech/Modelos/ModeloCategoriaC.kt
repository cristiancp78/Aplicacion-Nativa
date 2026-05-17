package com.example.tecnotech.Modelos

class ModeloCategoriaC {

    var id: String=""
    var categoria: String=""
    var imagen: String=""

    constructor()

    constructor(id: String, categoria: String, imagen: String) {
        this.id = id
        this.categoria = categoria
        this.imagen = imagen
    }
}