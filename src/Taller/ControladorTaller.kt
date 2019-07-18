package Taller


import Presupuestos.objetoIdiomas
import Presupuestos.objetoPresupuesto
import Presupuestos.objetosLP
import ProductosFabricados.objetoArticuloFabricado
import escandallos.objetoArticuloFabricacion
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.stage.Stage


import javafx.scene.control.*

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger
import javafx.scene.control.ComboBox
import javafx.scene.control.TitledPane
import javafx.scene.input.KeyEvent
import javafx.geometry.Insets
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import java.sql.ResultSet
import java.time.LocalDate
import java.util.HashMap


public class ControladorTaller
{

    var LineasPresupuesto = HashMap<String, objetosLP>()

    // BuscarCliente
    @FXML
    var idClinete: TextField = TextField()
    @FXML
    var nombreClinete: TextField = TextField()
    @FXML
    var clienteEncontrado: Label = Label()

    // Buscar en el cursos actula
    @FXML
    internal var listacamposBuscar: ComboBox<String> = ComboBox()

    @FXML
    internal var PalabraBuscador: TextField = TextField()
    private var lc: ResultSetMetaData? = null
    @FXML
    private var PanelBusqueda: Pane = Pane()

    var escenaActual: Scene? = null


    var nuevo: Boolean = true
    var posicion: Int = 0;

    var listaArticulos: MutableList<objetoIdiomas> = mutableListOf<objetoIdiomas>()

    // Clase en la  que se programan las operaciones contra la BD.

    var bd: TallerBd = TallerBd()
    //////////////////////////////////////////////////////////////////////////////
    var articulo: objetoIdiomas = objetoIdiomas(0, 0, "", 0, "", 0, "", 0)
    val fechaSql = java.sql.Date(0)

    // Campos FXML para crear presupuesto
    var articuloPresupuesto: objetoPresupuesto = objetoPresupuesto(0, 0, fechaSql, "", "")

    @FXML
    var idCliente: TextField = TextField()
    @FXML
    var nombreCliente: TextField = TextField()
    @FXML
    var idPresupuesto: Label = Label()
    @FXML
    var desdeFechaPresupuesto:DatePicker = DatePicker()
    @FXML
    var hastaFechaPresupuesto:DatePicker = DatePicker()
    @FXML
    var descripcionPresupuesto: TextArea = TextArea()
    @FXML
    var estadoPresupuesto:ComboBox<String> ?=null



    // Contenedor lineas Presupuesto creados en Ejecucion


    @FXML
    var ContenedorLineasPresupuesto: Accordion = Accordion()
    @FXML
    var descripcionLinea: TextField = TextField()

// Controles  Interfaz

    @FXML
    private var id_multimediaDMontaje: TextField = TextField()


    @FXML
    private var Texto_DMantenimiento: TextArea = TextArea()


    @FXML
    private var id_articuloFabricado: TextField = TextField()

    @FXML
    private var Texto_DTecnicos: TextArea = TextArea()


    @FXML
    private var SelecionIdiomas: ComboBox<String> = ComboBox()

    @FXML
    private var id_multimediaDMantenimiento: TextField = TextField()

    @FXML
    private var id_multimediaDTecnicos: TextField = TextField()

    @FXML
    private var etiquetaProductoFabricado: Label = Label()

    @FXML
    private var Texto_DMontaje: TextArea = TextArea()
    // Media View reproducir fichero Multimedia del registro
    @FXML
    var contenedorMultimedia: Pane = Pane()

    // Label visualiza articulo Fabricación buscado
    @FXML
    var presupuestoEncontradoParaAñadir: Label = Label()
    @FXML
    var idPresupuestoEncontradoParaAñadir: Label = Label()

     // Volumnas Tabla Visualizar presupuestos encontrados
    @FXML
    var rejillaRegistrosPresupuestor: TableView<objetoPresupuesto> = TableView()

    @FXML
    var cIdPresupuesto = TableColumn<objetoPresupuesto, Int>()
    @FXML
    var cCliente = TableColumn<objetoPresupuesto, Int>()

    @FXML
    var cFecha = TableColumn<objetoPresupuesto, java.sql.Date >()
    @FXML
    var cDescripcion = TableColumn<objetoPresupuesto, String >()
    @FXML
    var cEstado = TableColumn<objetoPresupuesto, String >()


    // VAriable para obtenel la sql de la consulta de presupuestos a fabricar i a imprimir
    var sqlPresupuestos: String = ""
    var wheresqlPresupuestos: String = ""
    // Contenedor de Lineas de Fabricacion creadas
    internal var contenedores = HashMap<String, VBox>()

    fun initialize() {
       // desdeFechaPresupuesto=DatePicker(LocalDate.now())
      //  hastaFechaPresupuesto=DatePicker(LocalDate.now())

        estadoPresupuesto!!.items.add("*")
        estadoPresupuesto!!.items.add("Diseño")
        estadoPresupuesto!!.items.add("Presentado")
        estadoPresupuesto!!.items.add("Admitido")
        estadoPresupuesto!!.items.add("Taller")
        estadoPresupuesto!!.items.add("Entregado")
        estadoPresupuesto!!.value="*"

        bd.conexionBd()


        rellenarListaCamposBuscar()


    }


    @FXML
    fun buscarPresupuestos() {

        // Configurar columnas de la TableView

        //      data class objetoPresupuesto(var id_Presupuesto:Int, var id_Cliente:Int, var Fecha: java.sql.Date , var Descripcion: String, var Estado: String)
        cIdPresupuesto.setCellValueFactory(PropertyValueFactory<objetoPresupuesto, Int>("id_Presupuesto"))
        // Borrar Presupuesto de la rejilla
        cIdPresupuesto.setOnEditStart({ t: TableColumn.CellEditEvent<objetoPresupuesto, Int> ->
            try {
                //  javax.swing.JOptionPane.showMessageDialog(null, t.rowValue.toString() + "--" + t.tableColumn);
                //  javax.swing.JOptionPane.showMessageDialog(null,  t.tablePosition.row)
                rejillaRegistrosPresupuestor.items.remove(t.rowValue)
            } catch (e: Exception) {
            }
        })
        cCliente.setCellValueFactory(PropertyValueFactory<objetoPresupuesto, Int>("id_Cliente"))
        cFecha.setCellValueFactory(PropertyValueFactory<objetoPresupuesto, java.sql.Date>("Fecha"))
        cDescripcion.setCellValueFactory(PropertyValueFactory<objetoPresupuesto, String>("Descripcion"))
        cEstado.setCellValueFactory(PropertyValueFactory<objetoPresupuesto, String>("Estado"))


        // Select todos los registros de presupuestor.
         sqlPresupuestos = "select * from Presupuestos "
        var codicionAnterior:Boolean=false
// Consulta por cliente
        var dIdCliente: String = idCliente.text
        if (dIdCliente.trim().length > 0) {
            sqlPresupuestos += " where id_Cliente=" + dIdCliente


            wheresqlPresupuestos+= " where \"Presupuestos\".\"id_Cliente\"=" + dIdCliente
            codicionAnterior=true
        }
// Consulta por fechas
        //SELECT * FROM [Metal].[dbo].[Presupuestos] where Fecha BETWEEN '1970-02-01' AND '1970-02-01'
        var f1:String=desdeFechaPresupuesto.editor.text.toString()
        var f2:String=hastaFechaPresupuesto.editor.text.toString()

        if (f1.trim().length>0 && f2.trim().length>0 ) {
            if(codicionAnterior) {
                sqlPresupuestos += " and " + "Fecha BETWEEN '" + desdeFechaPresupuesto.value + "' AND '" + hastaFechaPresupuesto.value + "'"
                wheresqlPresupuestos += " and " + " \"Presupuestos\".\"Fecha\" BETWEEN '" + desdeFechaPresupuesto.value + "' AND '" + hastaFechaPresupuesto.value + "'"

            }
            else
            {
                sqlPresupuestos += " where " + "Fecha BETWEEN '" + desdeFechaPresupuesto.value + "' AND '" + hastaFechaPresupuesto.value + "'"
                wheresqlPresupuestos += " where " + " \"Presupuestos\".\"Fecha\" BETWEEN '" + desdeFechaPresupuesto.value + "' AND '" + hastaFechaPresupuesto.value + "'"
            }
            codicionAnterior=true
        }

        // Consulta por estado presupuesto (estadoPresupuesto)

        if(!estadoPresupuesto!!.value.toString().equals("*")){
            if(codicionAnterior) {
                sqlPresupuestos += " and " + "Estado ='" +estadoPresupuesto!!.value.toString()+ "'"
                wheresqlPresupuestos += " and " + " \"Presupuestos\".\"Estado\" ='" +estadoPresupuesto!!.value.toString()+ "'"
            }
            else
            {
                sqlPresupuestos += " where " +  "Estado ='" +estadoPresupuesto!!.value.toString()+ "'"
                wheresqlPresupuestos += " where " +  " \"Presupuestos\".\"Estado\" ='" +estadoPresupuesto!!.value.toString()+ "'"
            }
        }

        var consulta:ResultSet?=null

        println(sqlPresupuestos)
        println(   wheresqlPresupuestos)
        consulta=bd.ejecutaConsulta(sqlPresupuestos)
        while (consulta!!.next()) {
            println("Dato " + consulta!!.getString(1) + " " + consulta!!.getString(2))

            var presupuestoLeido:objetoPresupuesto=objetoPresupuesto(consulta!!.getInt(1),consulta!!.getInt(2),consulta!!.getDate(3),consulta!!.getString(4),consulta!!.getString(5))

            rejillaRegistrosPresupuestor.items.add(presupuestoLeido)



        }
    }







    var datosPresupuestoRecibido: ResultSet? = null
    @FXML
    fun SigBuscar() {
        try {
            val snc = listacamposBuscar.value.toString()
            datosPresupuestoRecibido = bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)


            presupuestoEncontradoParaAñadir.text = datosPresupuestoRecibido!!.getString("Descripcion")
            idPresupuesto.text= datosPresupuestoRecibido!!.getString(1)
            idPresupuestoEncontradoParaAñadir.text=datosPresupuestoRecibido!!.getString(1)
            idCliente.text= datosPresupuestoRecibido!!.getString(2)
            estadoPresupuesto!!.value=datosPresupuestoRecibido!!.getString(5)

            var fechaPresu:java.sql.Date=datosPresupuestoRecibido!!.getDate(3)

            desdeFechaPresupuesto.value = fechaPresu.toLocalDate()

            hastaFechaPresupuesto.value=fechaPresu.toLocalDate()



        } catch (ex: Exception) {
            ex.printStackTrace()
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Idiomas Fabricadas"
            alert.contentText = "Error .. :Elija el Campo de Búsqueda"
            alert.showAndWait()
        }


    }

    @FXML
    fun AntBuscar() {
        try {
        val snc = listacamposBuscar.value.toString()
            presupuestoEncontradoParaAñadir.text = datosPresupuestoRecibido!!.getString("Descripcion")
            idPresupuesto.text= datosPresupuestoRecibido!!.getString(1)
            idPresupuestoEncontradoParaAñadir.text=datosPresupuestoRecibido!!.getString(1)
            idCliente.text= datosPresupuestoRecibido!!.getString(2)
            estadoPresupuesto!!.value=datosPresupuestoRecibido!!.getString(5)

            var fechaPresu:java.sql.Date=datosPresupuestoRecibido!!.getDate(3)

            desdeFechaPresupuesto.value = fechaPresu.toLocalDate()

            hastaFechaPresupuesto.value=fechaPresu.toLocalDate()

    } catch (ex: Exception)    {
        ex.printStackTrace()
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Gestión Metal"
        alert.headerText = "Crud Idiomas Fabricadas"
        alert.contentText = "Error .. :Elija el Campo de Búsqueda"
        alert.showAndWait()
    }

    }

    @FXML
    fun inicializarBusqueda()
    {
        presupuestoEncontradoParaAñadir.text = "Descripción"
        idPresupuesto.text=""
        idPresupuestoEncontradoParaAñadir.text="Id Presupuesto"
        idCliente.text=""
        estadoPresupuesto!!.value="*"
        PalabraBuscador.text=""
        desdeFechaPresupuesto.editor.text=""
        hastaFechaPresupuesto.editor.text=""
    }
    @FXML
    fun FinalizarBuscar() {
        PanelBusqueda.setVisible(false)
    }




    // Listado articulos con JasperReport
    @FXML
    fun ImprimirParteTaller() {

        var listadoJr: Taller.ParteFabricacionTaller.parteTaller = Taller.ParteFabricacionTaller.parteTaller()

        listadoJr.imprimefichero(wheresqlPresupuestos)
        wheresqlPresupuestos=""
        sqlPresupuestos=""

    }


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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Combo Box con los campos del resultSet Actual
    private fun rellenarListaCamposBuscar() {


        lc = bd.leerCamposTabla()
        try {
            //   listacamposBuscar.items.clear()
            //  listacamposBuscar.removeAllItems();

            for (i in 1..lc!!.getColumnCount()) {
                //     println("TIpo de campo en la table Producto " + lc!!.getColumnType(i) + lc!!.getColumnTypeName(i))
                if (lc!!.getColumnType(i) == 12 || lc!!.getColumnType(i) == 4) {

                    listacamposBuscar.items.add(lc!!.getColumnName(i).toString())

                }
            }
        } catch (ex: SQLException) {
            Logger.getLogger(ControladorTaller::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }

    @FXML
    fun buscaClientes(event: KeyEvent) {

        var texto: TextField = event.source as TextField
        var Campo: String = texto.id.trim()
        var rsCliente: ResultSet? = null
        var SQL:String=""
        if (Campo == "nombreCliente") {
            SQL = "select * from clientes where Nombre like '" + texto.text + "%'"
        } else {
            SQL = "select * from clientes where id like " + texto.text + ""
        }
        try {
            rsCliente = bd.leeCliente(SQL)
            var existe: Boolean = rsCliente.first()
            if (existe) {
                clienteEncontrado.text = rsCliente.getString("Nombre")
                idCliente.text = rsCliente.getString("Id")
            }

        } catch (e: Exception) {
        }
    }



}