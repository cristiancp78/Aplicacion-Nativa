package com.example.tecnotech.Modelos

class ModeloVendedores {
    var uid: String =""
    var nombres: String =""
    var correo: String =""
    var imagen: String =""
    var cedula: String =""
    var direccion: String =""
    var tienda: String =""

    constructor()
    constructor(
        uid: String,
        nombres: String,
        correo: String,
        imagen: String,
        direccion: String,
        cedula: String,
        tienda: String
    ) {
        this.uid = uid
        this.nombres = nombres
        this.correo = correo
        this.imagen = imagen
        this.direccion = direccion
        this.cedula = cedula
        this.tienda = tienda
    }

}