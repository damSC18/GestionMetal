package Clientes


import javafx.collections.FXCollections

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.Pane

import javafx.stage.Stage

import javafx.event.Event

import javafx.scene.SceneAntialiasing
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent

import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.Connection

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern
import javax.swing.filechooser.FileNameExtensionFilter


public class ControladorClientes {

    // Buscar en el cursos actula
    @FXML
    internal var listacamposBuscar: ComboBox<String> = ComboBox()

    @FXML
    internal var PalabraBuscador: TextField = TextField()
    // El ResultSetMetaData nos devuelve la estrura de la tabla en la base de datos
    // Nombre de campos, tipo, estado
    private var lc: ResultSetMetaData? = null
    @FXML
    private var PanelBusqueda :Pane=Pane()

    var escenaActual:Scene?=null


    var nuevo: Boolean = true
    var posicion: Int = 0;
    // MutableList
    // var listaArticulos: ArrayList<objetoArticuloFabricado> = arrayListOf<objetoArticuloFabricado>()
    var listaArticulos: MutableList<objetoCliente> = mutableListOf<objetoCliente>()

    // Clase en la  que se programan las operaciones contra la BD.

    var bd : operacionesBd =  operacionesBd()
    //////////////////////////////////////////////////////////////////////////////
    var  cliente: objetoCliente = objetoCliente(0,"","" ,"","","","","","","","","","")



    @FXML
    var idCliente: TextField = TextField()
    @FXML
    var nombre: TextField = TextField()
    @FXML
    var direccion: TextField = TextField()
    @FXML
    var poblacion: TextField = TextField()
    @FXML
    var provincia: TextField = TextField()
    @FXML
    var codigoPostal: TextField = TextField()
    @FXML
    var cif_nif: TextField = TextField()
    @FXML
    var telefono1: TextField = TextField()
    @FXML
    var telefono2: TextField = TextField()
    @FXML
    var email: TextField = TextField()
    @FXML
    var web: TextField = TextField()
    @FXML
    var personaContacto: TextField = TextField()
    @FXML
    var sectorComercial: TextField = TextField()




    // Trabajo Escandaloos por articulo actual se visualiza en el ListView
    @FXML
    var ListaEscandallos: ListView<String> = ListView()

    fun initialize(){

        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        cliente= bd.fleerPrimerRegistro()
        visualizaRegistro()
        // Lista de campos para métod de búsqueda
        rellenarListaCamposBuscar()


        registerListener(nombre,50,1)
        registerListener(direccion,60,2)
        registerListener(poblacion,50,3)
        registerListener(provincia,30,4)
        registerListener(cif_nif,10,5)
        registerListener(codigoPostal,5,6)
        registerListener(telefono1,10,7)
        registerListener(telefono2,10,8)
        registerListener(email,50,9)
        registerListener(web,50,10)
        registerListener(personaContacto,60,11)
        registerListener(sectorComercial,80,12)
    }

    @FXML
    fun NuevoRegistro() {
        nuevo = true
        var posicion: Int = 0
        try {



        } catch (ex: Exception) {
            posicion = 0
        }
        idCliente.text = posicion.toString()
        nombre.text = ""
        direccion.text = ""
        poblacion.text = ""
        provincia.text = ""
        codigoPostal.text = ""
        cif_nif.text = ""
        telefono1.text = ""
        telefono2.text = ""
        email.text = ""
        web.text = ""
        personaContacto.text = ""
        sectorComercial.text = ""
        nombre.requestFocus();



    }

    @FXML
    fun GrabaroRegistro() {
        try {

            val dato_idCliente = idCliente.text.toInt()
            val dato_nombre = nombre.text
            val dato_direccion= direccion.text
            val dato_poblacion = poblacion.text
            val dato_provincia = provincia.text
            val dato_codigoPostal = codigoPostal.text
            val dato_cif_nif = cif_nif.text
            val dato_telefono1  =  telefono1.text
            val dato_telefono2  =  telefono2.text

            val dato_email  =  email.text
            val dato_web  =  web.text
            val dato_personaContacto  =  personaContacto.text
            val dato_sectorComercial  =  sectorComercial.text





           var cliente=objetoCliente(dato_idCliente,dato_nombre,dato_direccion,dato_poblacion,dato_provincia,dato_codigoPostal,
                   dato_cif_nif,dato_telefono1,dato_telefono2,dato_email,dato_web,dato_personaContacto,dato_sectorComercial)

            if(nuevo){
            bd.ffgrabaRegistro(cliente)
            }
            else{
                bd.ffmodificaRegistro(cliente)
            }

           // idCliente.text.toInt()
        } catch (ex: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Materias Fabricadas"
            alert.contentText = "Error al grabar artículo"
            alert.showAndWait()
        }
    }


    @FXML
    fun BorraRegistro() {

      //  Borrar registro actul
        bd.ffborraRegistro(idCliente.text.toInt())
        PrimerRegistro()

    }



    @FXML
    fun anteriorRegistro() {



        cliente= bd.fleerAnteriorRegistro()

        visualizaRegistro()

    }

    @FXML
    fun SiguienteRegistro() {


        cliente= bd.fleerSiguienteRegistro()

        visualizaRegistro()


    }

    @FXML
    fun PrimerRegistro() {

        cliente= bd.fleerPrimerRegistro()




        visualizaRegistro()
    }

    @FXML
    fun UltimoRegistro() {


        cliente= bd.fleerUltimoRegistro()



        visualizaRegistro()
    }

    @FXML
    fun BuscarRegistro() {
        PanelBusqueda.setVisible(true)

    }

    @FXML
    fun SigBuscar() {
        val snc = listacamposBuscar.value.toString()
        cliente= bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    fun AntBuscar() {
        val snc = listacamposBuscar.value.toString()
        cliente= bd.fleerAnteriorBuuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    fun FinalizarBuscar() {
        PanelBusqueda.setVisible(false)
    }

    @FXML
    fun Finalizar() {

    }

    fun visualizaRegistro() {

        nuevo = false


        idCliente.text = cliente.id.toString()
        nombre.text =  cliente.nombre
        direccion.text =  cliente.direccion
        poblacion.text =cliente.poblacion
        provincia.text =  cliente.provincia
        codigoPostal.text =  cliente.codigoPostal
        cif_nif.text = cliente.cif_Nif
        telefono1.text =  cliente.telefono1
        telefono2.text =  cliente.telefono2
        email.text = cliente.email
        web.text =  cliente.web
        personaContacto.text = cliente.personaContacto
        sectorComercial.text =  cliente.sectorComercial
        nombre.requestFocus();



    }


    // Listado articulos con JasperReport
    @FXML
    fun ImprimeArticulosFabricados()
    {

        var listadoJr:reportes.listadoArticulosFabricados=reportes.listadoArticulosFabricados()

         listadoJr.imprimefichero()
    }
    // Rejilla datos

    @FXML
    fun Finalizar(event: ActionEvent) {

        try {


            val root = FXMLLoader.load<Parent>(javaClass.getResource("Metal.fxml"))

            // val root = FXMLLoader.load<Parent>(javaClass.getResource("..//Metal.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {e.printStackTrace()
        }
    }

@FXML
fun SalirCaja(event: MouseEvent){
   /* System.out.println(event.source.toString())
    var caja:TextField=event.getSource() as TextField
    System.out.println(caja.text)
    val p = Pattern.compile("[0-9]*.[0-9]*")
    val m = p.matcher(caja.text )
    val b = m.matches()
    println("Foco " +b)
    if(!b){caja.requestFocus();*/
}



    fun registerListener(cajaTexto: TextField ,ncaracteres:Int,nc:Int) {

        cajaTexto.textProperty().addListener { obs, oldText, newText ->
            if ( newText.length > ncaracteres) {
                cajaTexto.text=  oldText
            }

        }

        cajaTexto.focusedProperty().addListener { obs, oldText, newText ->

           // val p = Pattern.compile("[0-9]*.[0-9]*")
            var p = Pattern.compile("")
            var b:Boolean=true
            if(nc==9) {
                p = Pattern.compile("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}\$")
                val m = p.matcher(cajaTexto.text )
                b = m.find()
            }
            if(nc==6||nc==7 || nc==8 ) {
                println("NUMERO "+nc)
                p = Pattern.compile("[0-9]*")
                val m = p.matcher(cajaTexto.text )
                b = m.matches()
            }

            if(nc==5  ) {
                println("NUMERO "+nc)
                p = Pattern.compile("[0-9]{8}[A-Z]{1}")
                val m = p.matcher(cajaTexto.text )
                b = m.matches()
            }
         //   val b = m.find()
            println("Foco " +b)

            if(!b){
                cajaTexto.requestFocus()
               cajaTexto.style="-fx-text-fill:red"
            }

        }
    }





    // Combo Box con los campos del resultSet Actual
    private fun rellenarListaCamposBuscar() {

      lc= bd.leerCamposTabla()
        try {


            for (i in 1..lc!!.getColumnCount()) {
            //   println("TIpo de campo en la table Producto " + lc!!.getColumnType(i) + lc!!.getColumnTypeName(i))
                if (lc!!.getColumnType(i) == 12 || lc!!.getColumnType(i) == 4) {

                    listacamposBuscar.items.add(lc!!.getColumnName(i).toString())


                }
            }
        } catch (ex: SQLException) {
            Logger.getLogger(ControladorClientes::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }
}

