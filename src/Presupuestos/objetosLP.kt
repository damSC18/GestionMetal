package Presupuestos

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField


data class objetosLP(var descripcionLineaPresupuesto: String ,var codigoArticuloCreado: Button=Button()   ,var precioArticulos: TextField = TextField(),var cantidadArticulos: TextField = TextField(),var estadoLinea: Button = Button(),var TAMontaje: TextArea = TextArea(),var TAMantenimiento: TextArea = TextArea(),var TATecnicos: TextArea = TextArea())
