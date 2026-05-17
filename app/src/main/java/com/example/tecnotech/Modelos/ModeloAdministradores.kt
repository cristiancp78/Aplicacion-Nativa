package com.example.tecnotech.Modelos

class ModeloAdministradores {
    var uid: String =""
    var nombres: String =""
    var correo: String =""
    var tipoUsuario: String =""

    constructor()
    constructor(id: String, nombre: String, correo: String, direccion: String, tipo: String){
        this.uid = id
        this.nombres = nombre
        this.correo = correo
        this.tipoUsuario = tipo
    }

}