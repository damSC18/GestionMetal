package ProductosFabricados


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
import javax.swing.filechooser.FileNameExtensionFilter


public class ControladorProductosFabricados {

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
    var listaArticulos: MutableList<objetoArticuloFabricado> = mutableListOf<objetoArticuloFabricado>()

    // Clase en la  que se programan las operaciones contra la BD.

    var bd : operacionesBd =  operacionesBd()
    //////////////////////////////////////////////////////////////////////////////
    var  articulo: objetoArticuloFabricado = objetoArticuloFabricado(0,"",0,0f,0f,0,0,"")
 //   var articulo: objetoArticuloFabricado = objetoArticuloFabricado(1, "", 0, 0f, 0f, 0f, 0f)

    @FXML
    var rejillaDatosPF: TableView<objetoArticuloFabricado> = TableView()
    @FXML
    var columnaId = TableColumn<objetoArticuloFabricado, Int>()
    @FXML
    var columnaDescripcion = TableColumn<objetoArticuloFabricado, String>()
    @FXML
    var columnaHoras = TableColumn<objetoArticuloFabricado, Float>()
    @FXML
    var columnaCoste = TableColumn<objetoArticuloFabricado, Float>()
    @FXML
    var columnaVentas = TableColumn<objetoArticuloFabricado, Float>()
    @FXML
    var columnaStock = TableColumn<objetoArticuloFabricado, Float>()
    @FXML
    var columnaMinimo = TableColumn<objetoArticuloFabricado, Float>()

    @FXML
    var tblParticipants = TableView<objetoArticuloFabricado>()

    @FXML
    var idProducto: TextField = TextField()
    @FXML
    var descripcion: TextField = TextField()
    @FXML
    var idFamilia: TextField = TextField()
    @FXML
    var precioCoste: TextField = TextField()
    @FXML
    var precioVenta: TextField = TextField()
    @FXML
    var stock: TextField = TextField()
    @FXML
    var stockMinimo: TextField = TextField()
    @FXML
    var ficheroImagen: TextField = TextField()



    // Trabajo Escandaloos por articulo actual se visualiza en el ListView
    @FXML
    var ListaEscandallos: ListView<String> = ListView()

    fun initialize(){

        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        articulo= bd.fleerPrimerRegistro()
        visualizaRegistro()
        // Lista de campos para métod de búsqueda
        rellenarListaCamposBuscar()
    }

    @FXML
    fun NuevoRegistro() {
        nuevo = true
        var posicion: Int = 0
        try {
        //    articulo = listaArticulos.last()
         //   posicion = articulo.id + 1


        } catch (ex: Exception) {
            posicion = 0
        }
        idProducto.text = posicion.toString()
        descripcion.text = ""
        idFamilia.text = "0.0"
        precioCoste.text = "0.0"
        precioVenta.text = "0.0"
        stock.text = "0.0"
        stockMinimo.text = "0.0"
        descripcion.requestFocus();
    }

    @FXML
    fun GrabaroRegistro() {
        try {

            val dato_idProducto = idProducto.text.toInt()
            val dato_descripcion = descripcion.text
            val dato_familia = idFamilia.text.toInt()
            val dato_precioCoste = precioCoste.text.toFloat()
            val dato_precioVenta = precioVenta.text.toFloat()
            val dato_stock = stock.text.toInt()
            val dato_stockMinimo = stockMinimo.text.toInt()

            val dato_Imagen = ficheroImagen.text

           var articulo=objetoArticuloFabricado(dato_idProducto,dato_descripcion,dato_familia,dato_precioCoste,dato_precioVenta,dato_stock,dato_stockMinimo,dato_Imagen)

            if(nuevo){
            bd.ffgrabaRegistro(articulo)
            }
            else{
                bd.ffmodificaRegistro(articulo)
            }

            idProducto.text.toInt()
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
        bd.ffborraRegistro(idProducto.text.toInt())
        PrimerRegistro()

    }



    @FXML
    fun anteriorRegistro() {



        articulo= bd.fleerAnteriorRegistro()

        visualizaRegistro()

    }

    @FXML
    fun SiguienteRegistro() {


        articulo= bd.fleerSiguienteRegistro()

        visualizaRegistro()


    }

    @FXML
    fun PrimerRegistro() {

        articulo= bd.fleerPrimerRegistro()




        visualizaRegistro()
    }

    @FXML
    fun UltimoRegistro() {


        articulo= bd.fleerUltimoRegistro()



        visualizaRegistro()
    }

    @FXML
    fun BuscarRegistro() {
        PanelBusqueda.setVisible(true)

    }

    @FXML
    fun SigBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo= bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    fun AntBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo= bd.fleerAnteriorBuuscar(PalabraBuscador.text, snc)

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
        idProducto.text = articulo.id.toString()
        descripcion.text = articulo.descripcion
        idFamilia.text = articulo.familia.toString()
        precioCoste.text = articulo.precioCoste.toString()
        precioVenta.text = articulo.precioVenta.toString()
        stock.text = articulo.stock.toString()
        stockMinimo.text = articulo.stockMinimo.toString()
        ficheroImagen.text = articulo.imagen

        // Llena ListView con las materia primas del articulo fabricado
        ListaEscandallos.items.clear()
        var data = ArrayList<String>()
        data=   bd.escandalloProductoFabricado(articulo.id)
        var observableListEscandallos = FXCollections.observableList<String>(data)
        ListaEscandallos.items=observableListEscandallos
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
    fun VisualizaRejilla(event: ActionEvent) {


        var ColumnaId = TableColumn<objetoArticuloFabricado, Int>()
        ColumnaId.text = "Id"
        ColumnaId.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Int>("id"))

        var ColumnaDescripcion = TableColumn<objetoArticuloFabricado, String>()
        ColumnaDescripcion.text = "Descripcion"
        ColumnaDescripcion.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, String>("descripcion"))

        var ColumnaHorasFabricacion = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaHorasFabricacion.text = "Familia"
        ColumnaHorasFabricacion.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("Familia"))

        var ColumnaPrecioCoste = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaPrecioCoste.text = "Precio Coste"
        ColumnaPrecioCoste.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("precioCoste"))

        var ColumnaPrecioVenta = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaPrecioVenta.text = "Precio Venta"
        ColumnaPrecioVenta.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("precioVenta"))

        var ColumnaStock = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaStock.text = "Stock"
        ColumnaStock.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("stock"))

        var ColumnaStockMinimo = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaStockMinimo.text = "Stock Minimo"
        ColumnaStockMinimo.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("stockMinimo"))

        var rejillaDatosCodigo: TableView<objetoArticuloFabricado> = TableView()

        rejillaDatosCodigo.setPrefSize(780.0,380.0)

        rejillaDatosCodigo.getColumns().addAll(ColumnaId, ColumnaDescripcion, ColumnaHorasFabricacion, ColumnaPrecioCoste, ColumnaPrecioVenta, ColumnaStock, ColumnaStockMinimo)

        // bobtener Datos del ResultSet

        try {
            listaArticulos = bd.listado()


            for (art in listaArticulos) {

                var objetoAFabricado:objetoArticuloFabricado=objetoArticuloFabricado(art.id, art.descripcion, art.familia, art.precioCoste, art.precioVenta, art.stock, art.stockMinimo, art.imagen)

                rejillaDatosCodigo.items.add(objetoAFabricado)

            }

        }catch (e: Exception) {
            e.printStackTrace()}



        val stage = (event.getSource() as Node).getScene().getWindow() as Stage

        stage.setAlwaysOnTop(true);
//        stage.initStyle(StageStyle.TRANSPARENT)


        var scene:Scene=stage.getScene()
        escenaActual=scene

        var pantallaRejilla: VBox = VBox()
        pantallaRejilla.setPrefSize(780.0,380.0)

        var volver: Button = Button("Volver")

        volver.setOnAction { actionEvent ->

            try {

                //     val root = FXMLLoader.load<Parent>(javaClass.getResource("ProductosFabricados.fxml"))

                stage.scene =escenaActual// scene
                stage.toFront()
                stage.show()

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message
                )
            }
        }


        // Detecta el evento del botón close(X) y lo desacriva
        stage.setOnCloseRequest { e: Event ->

            e.consume()
            //  println("Cerr<r2)")
            volver.fire()
        }


        pantallaRejilla.children.addAll(rejillaDatosCodigo,volver)


        pantallaRejilla.setLayoutX(10.0)

        pantallaRejilla.setLayoutY(10.0)
        scene = Scene(pantallaRejilla , 800.0, 400.0,true , SceneAntialiasing.BALANCED )


        // var stage: Stage = Stage()


        stage.scene = scene
        stage.title="Gestión del Metal   -- Listado Artículos Fabricados"

        val ico = Image("Imagenes/icono.jpg")
        stage.icons.add(ico)
        stage.y=300.1
        stage.x=300.1
        stage.toFront()



        stage.show()




    }
  /*  @FXML
    fun VisualizaRejilla(event: ActionEvent) {


        var ColumnaId = TableColumn<oopf, Int>()
        ColumnaId.text = "Id"
        ColumnaId.setCellValueFactory(PropertyValueFactory<oopf, Int>("id"))

        var ColumnaDescripcion = TableColumn<oopf, String>()
        ColumnaDescripcion.text = "Descripcion"
        ColumnaDescripcion.setCellValueFactory(PropertyValueFactory<oopf, String>("descripcion"))

        var ColumnaHorasFabricacion = TableColumn<oopf, Float>()
        ColumnaHorasFabricacion.text = "H.Fabricacion"
        ColumnaHorasFabricacion.setCellValueFactory(PropertyValueFactory<oopf, Float>("horasFabricacion"))

        var ColumnaPrecioCoste = TableColumn<oopf, Float>()
        ColumnaPrecioCoste.text = "Precio Coste"
        ColumnaPrecioCoste.setCellValueFactory(PropertyValueFactory<oopf, Float>("precioCoste"))

        var ColumnaPrecioVenta = TableColumn<oopf, Float>()
        ColumnaPrecioVenta.text = "Precio Venta"
        ColumnaPrecioVenta.setCellValueFactory(PropertyValueFactory<oopf, Float>("precioVenta"))

        var ColumnaStock = TableColumn<oopf, Float>()
        ColumnaStock.text = "Stock"
        ColumnaStock.setCellValueFactory(PropertyValueFactory<oopf, Float>("stock"))

        var ColumnaStockMinimo = TableColumn<oopf, Float>()
        ColumnaStockMinimo.text = "Stock Minimo"
        ColumnaStockMinimo.setCellValueFactory(PropertyValueFactory<oopf, Float>("stockMinimo"))

        var rejillaDatosCodigo: TableView<oopf> = TableView()

        rejillaDatosCodigo.setPrefSize(780.0,380.0)

        rejillaDatosCodigo.getColumns().addAll(ColumnaId, ColumnaDescripcion, ColumnaHorasFabricacion, ColumnaPrecioCoste, ColumnaPrecioVenta, ColumnaStock, ColumnaStockMinimo)

        // bobtener Datos del ResultSet

        try {
            listaArticulos = bd.listado()


            for (art in listaArticulos) {

                val objetoPF: oopf = oopf()
                objetoPF.oopf(art.id, art.descripcion, art.familia, art.precioCoste, art.precioVenta, art.stock, art.stockMinimo, art.imagen)


                rejillaDatosCodigo.items.add(objetoPF)

            }

        }catch (e: Exception) {
            e.printStackTrace()}



        val stage = (event.getSource() as Node).getScene().getWindow() as Stage




        stage.setAlwaysOnTop(true);
//        stage.initStyle(StageStyle.TRANSPARENT)


        var scene:Scene=stage.getScene()
        escenaActual=scene

        var pantallaRejilla: VBox = VBox()
        pantallaRejilla.setPrefSize(780.0,380.0)

        var volver: Button = Button("Volver")

        volver.setOnAction { actionEvent ->


            try {

           //     val root = FXMLLoader.load<Parent>(javaClass.getResource("ProductosFabricados.fxml"))

                stage.scene =escenaActual// scene
                stage.toFront()
                stage.show()

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message
            )
            }
        }


        // Detecta el evento del botón close(X) y lo desacriva
        stage.setOnCloseRequest { e: Event ->

             e.consume()
          //  println("Cerr<r2)")
            volver.fire()


        }


        pantallaRejilla.children.addAll(rejillaDatosCodigo,volver)


        pantallaRejilla.setLayoutX(10.0)

        pantallaRejilla.setLayoutY(10.0)
        scene = Scene(pantallaRejilla , 800.0, 400.0,true , SceneAntialiasing.BALANCED )


       // var stage: Stage = Stage()


        stage.scene = scene
        stage.title="Gestión del Metal"

        val ico = Image("Imagenes/icono.jpg")
        stage.icons.add(ico)
        stage.y=300.1
        stage.x=300.1
        stage.toFront()



        stage.show()

    }

*/
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
    fun SeleccionarImagen()
    {
        val fileChooser: FileChooser = FileChooser()
        fileChooser.getExtensionFilters().addAll(
                FileChooser.ExtensionFilter("Ficheros de Imagen", "*.png", "*.jpg", "*.gif"),
                FileChooser.ExtensionFilter("Ficheros de Texto", "*.txt"),
                FileChooser.ExtensionFilter("Ficheros Audio", "*.wav", "*.mp3", "*.aac"),
                FileChooser.ExtensionFilter("Todos los formatos", "*.*"));
        val selectedFile = fileChooser.showOpenDialog(null)
        if (selectedFile != null) {
            try {

                    ficheroImagen.text = selectedFile.absolutePath

            } catch (ex: FileNotFoundException) {
                println(ex.message)
            }
        }
    }
    @FXML
    fun VisualizaRejillaversion2() {
        println("VFinalizargggggggggggggggggggggggggggggggggggggggggggggggggg")
        val root = FXMLLoader.load<Parent>(javaClass.getResource("rejillaProductos.fxml"))



        var ColumnaId = TableColumn<objetoArticuloFabricado, Int>()
        ColumnaId.text = "Id"
        ColumnaId.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Int>("id"))

        var ColumnaDescripcion = TableColumn<objetoArticuloFabricado, String>()
        ColumnaDescripcion.text = "Descripcion"

        ColumnaDescripcion.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, String>("descripcion"))

        var ColumnaHorasFabricacion = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaHorasFabricacion.text = "H.Fabricacion"
        ColumnaHorasFabricacion.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("horasFabricacion"))

        var ColumnaPrecioCoste = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaPrecioCoste.text = "Precio Coste"
        ColumnaPrecioCoste.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("precioCoste"))

        var ColumnaPrecioVenta = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaPrecioVenta.text = "Precio Venta"
        ColumnaPrecioVenta.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("precioVenta"))

        var ColumnaStock = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaStock.text = "Stock"
        ColumnaStock.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("stock"))

        var ColumnaStockMinimo = TableColumn<objetoArticuloFabricado, Float>()
        ColumnaStockMinimo.text = "Stock Minimo"
        ColumnaStockMinimo.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricado, Float>("stockMinimo"))

        var rejillaDatosCodigo: TableView<objetoArticuloFabricado> = TableView()

        rejillaDatosCodigo.getColumns().addAll(ColumnaId, ColumnaDescripcion, ColumnaHorasFabricacion, ColumnaPrecioCoste, ColumnaPrecioVenta, ColumnaStock, ColumnaStockMinimo)

      //  rejillaDatosCodigo.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (registro in listaArticulos) {

            val objetoPF: objetoArticuloFabricado =  objetoArticuloFabricado(0,"",0,0f,0f,0,0,"")

            println(objetoPF)
            rejillaDatosCodigo.items.add(objetoPF)

        }
        //println(rejillaDatosCodigo.items)
        var pantallaRejilla: Pane = Pane()
        pantallaRejilla.children.add(rejillaDatosCodigo)
        val scene = Scene(pantallaRejilla)

        var stage: Stage = Stage()
        //val stage = (event.getSource() as Node).getScene().getWindow() as Stage
        //    var stage:Stage=Stage()
        stage.scene = scene
        stage.setOnCloseRequest { event ->
            println("removing from windows stack")
            val root = FXMLLoader.load<Parent>(javaClass.getResource("ProductosFabricados.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
            appStage.scene = scene
            appStage.toFront()
            appStage.show()


        }
        // stage.close()

        stage.toFront()
        stage.show()
    }

    // Combo Box con los campos del resultSet Actual
    private fun rellenarListaCamposBuscar() {

      lc= bd.leerCamposTabla()
        try {
         //   listacamposBuscar.items.clear()
            //  listacamposBuscar.removeAllItems();

            for (i in 1..lc!!.getColumnCount()) {
            //   println("TIpo de campo en la table Producto " + lc!!.getColumnType(i) + lc!!.getColumnTypeName(i))
                if (lc!!.getColumnType(i) == 12 || lc!!.getColumnType(i) == 4) {

                    listacamposBuscar.items.add(lc!!.getColumnName(i).toString())

                }
            }
        } catch (ex: SQLException) {
            Logger.getLogger(ControladorProductosFabricados::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }
}

