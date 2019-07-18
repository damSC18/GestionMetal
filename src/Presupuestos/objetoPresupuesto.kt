package Presupuestos

import java.sql.Date


data class objetoPresupuesto(var id_Presupuesto:Int, var id_Cliente:Int, var Fecha: java.sql.Date , var Descripcion: String, var Estado: String)
