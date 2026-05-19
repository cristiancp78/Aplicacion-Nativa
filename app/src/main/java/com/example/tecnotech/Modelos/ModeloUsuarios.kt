package com.example.tecnotech.Modelos

class ModeloUsuarios {
    var uid: String =""
    var nombres: String =""
    var cedula: String =""
    var correo: String =""
    var imagen: String =""
    var direccion: String =""
    var tipoUsuario: String =""

    constructor()
    constructor(uid: String, nombre: String, cedula: String, correo: String,imagen: String, direccion: String, tipo: String) {
        this.uid = uid
        this.nombres = nombre
        this.correo = correo
        this.imagen = imagen
        this.cedula = cedula
        this.direccion = direccion
        this.tipoUsuario = tipo
    }


}