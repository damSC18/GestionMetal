package MateriasPrimas


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
import java.io.FileNotFoundException

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


public class ControladorMateriasPrimas {


    //DECLARACION DE VARIABLES
    private var lc: ResultSetMetaData? = null
    var escenaActual:Scene?=null
    var nuevo: Boolean = true
    var listaArticulos: MutableList<objetoMateriasPrimas> = mutableListOf<objetoMateriasPrimas>()
    var bd : operacionesMP =  operacionesMP()
    var  articulo: objetoMateriasPrimas = objetoMateriasPrimas(0,"",0,0f,"")

    @FXML
    internal var listacamposBuscar: ComboBox<String> = ComboBox()
    @FXML
    internal var PalabraBuscador: TextField = TextField()
    @FXML
    private var PanelBusqueda :Pane=Pane()
    @FXML
    var idMateriaPrima: TextField = TextField()
    @FXML
    var descripcion: TextField = TextField()
    @FXML
    var idFamilia: TextField = TextField()
    @FXML
    var precioCoste: TextField = TextField()
    @FXML
    var ficheroImagen: TextField = TextField()



    // Trabajo Escandaloos por articulo actual se visualiza en el ListView
    @FXML
    var ListaEscandallos: ListView<String> = ListView()

    fun initialize(){
        //LLAMAMOS A CONEXIONBD() QUE SE ENCUENTRA EN LA CLASE OPERACIONESMP "var bd : operacionesMP =  operacionesMP()" PARA CONECTARNOS A LA BASE DE DATOS
        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        articulo= bd.PrimerRegistroMP()
        visualizaRegistro()

        rellenarListaCamposBuscar()
    }

    @FXML
    //FUNCION PARA CREAR UN NUEVO REGISTRO
    fun NuevoRegistro() {
        nuevo = true
        var posicion: Int = 0
        try {
            //DEFINIMOS LA POSICION QUE OCUPARA EL NUEVO ARTICULO
            articulo = listaArticulos.last()
            posicion = articulo.id + 1


        } catch (ex: Exception) {
            posicion = 0
        }
        //DEFINIMOS LOS VALORES QUE SE COLOCARAN POR DEFECTO AP PINCHAR EN EL BOTON NUEVO Y EL CURSOR LE PONDREMOS EN EL CAMPO DESCRIPCION
        idMateriaPrima.text = posicion.toString()
        descripcion.text = ""
        idFamilia.text = "0"
        precioCoste.text = "0.0"
        descripcion.requestFocus()
    }

    @FXML
    //FUNCION PARA GUARDAR UN NUEVO REGISTRO
    fun GrabarRegistro() {
        try {
           //COGEMOS LOS VALORES DE LAS CAJAS DE TEXTO
            val dato_idMateriaPrima= idMateriaPrima.text.toInt()
            val dato_descripcion = descripcion.text
            val dato_familia = idFamilia.text.toInt()
            val dato_precioCoste = precioCoste.text.toFloat()
            val dato_Imagen = ficheroImagen.text
//INSERTAMOS EN ARTICULO  QUE ES UN OBJETO MATERIAS PRIMAS
           var articulo=objetoMateriasPrimas(dato_idMateriaPrima,dato_descripcion,dato_familia,dato_precioCoste,dato_Imagen)
//SI NUEVO ES TRUE LLAMAMOS A LA FUNCION PARA GRABAR UN NUEVO REGISTRO
            if(nuevo){
            bd.grabaRegistroMP(articulo)

            }
            //SI NUEVO ES false LLAMAMOS A LA FUNCION PARA MODIFICAR UN REGISTRO
            else{
                bd.modificaRegistroMP(articulo)

            }
            idMateriaPrima.text.toInt()
        } catch (ex: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Materias Fabricadas"
            alert.contentText = "Error al grabar artículo"
            alert.showAndWait()
        }
    }


    @FXML
    //FUNCION PARA BORRAR UN REGISTRO
    fun BorraRegistro() {

      //LLAMAMOS A LA FUNCION BORRAREGISTRO DE OPERACIONESMP Y LE PASAMOS EL VALOR DEL CAMPO IDMATERIAPRIMA
        bd.borraRegistroMP(idMateriaPrima.text.toInt())
        PrimerRegistro()

    }



    @FXML
    //FUNCION PARA RETROCEDER UN REGISTRO
    fun anteriorRegistro() {
        articulo= bd.AnteriorRegistroMP()
        visualizaRegistro()

    }

    @FXML
    //FUNCION PARA AVANZAR UN REGISTRO
    fun SiguienteRegistro() {
        articulo= bd.SiguienteRegistroMP()
        visualizaRegistro()

    }

    @FXML
    //FUNCION PARA IR AL PRIMER REGISTRO
    fun PrimerRegistro() {
        articulo= bd.PrimerRegistroMP()
        visualizaRegistro()
    }

    @FXML
    //FUNCION PARA IR AL ULTIMO REGISTRO
    fun UltimoRegistro() {
        articulo= bd.UltimoRegistroMP()
        visualizaRegistro()
    }

    @FXML
    //FUNCION PARA BUSCAR UN REGISTRO
    fun BuscarRegistro() {
        PanelBusqueda.setVisible(true)

    }
    //FUNCION PARA BUSCAR SIGUIENTE REGISTRO
    @FXML
    fun SigBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo= bd.SiguienteBuscarMP(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    //FUNCION PARA BUSCAR ANTERIOR REGISTRO
    fun AntBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo= bd.AnteriorBuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    //FUNCION PARA FINALIZAR  BUSQUEDA
    fun FinalizarBuscar() {
        PanelBusqueda.setVisible(false)
    }


    fun visualizaRegistro() {

        nuevo = false
        idMateriaPrima.text = articulo.id.toString()
        descripcion.text = articulo.descripcion
        idFamilia.text = articulo.familia.toString()
        precioCoste.text = articulo.precioCoste.toString()
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

     // var listadoJr:reportes.informeMateriasPrimas=reportes.informeMateriasPrimas()

  //    listadoJr.imprimefichero()
    }
    //FUNCION PARA VISUALIZAR LA REJILLA DE DATOS
    @FXML
    fun VisualizaRejilla(event: ActionEvent) {

        //CREAMOS LA COLUMNA LE ASIGNAMOS SU TITULO Y LA IMCICAMOS QUE TOME EL VALOR ENTERO DEL CAMPO ID
        var ColumnaId = TableColumn<objetoMateriasPrimas, Int>()
        ColumnaId.text = "Id"
        ColumnaId.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Int>("id"))

        var ColumnaDescripcion = TableColumn<objetoMateriasPrimas, String>()
        ColumnaDescripcion.text = "Descripcion"
        ColumnaDescripcion.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, String>("descripcion"))

        var ColumnaHorasFabricacion = TableColumn<objetoMateriasPrimas, Float>()
        ColumnaHorasFabricacion.text = "H.Fabricacion"
        ColumnaHorasFabricacion.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Float>("horasFabricacion"))

        var ColumnaPrecioCoste = TableColumn<objetoMateriasPrimas, Float>()
        ColumnaPrecioCoste.text = "Precio Coste"
        ColumnaPrecioCoste.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Float>("precioCoste"))

        var ColumnaPrecioVenta = TableColumn<objetoMateriasPrimas, Float>()
        ColumnaPrecioVenta.text = "Precio Venta"
        ColumnaPrecioVenta.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Float>("precioVenta"))

        var ColumnaStock = TableColumn<objetoMateriasPrimas, Float>()
        ColumnaStock.text = "Stock"
        ColumnaStock.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Float>("stock"))

        var ColumnaStockMinimo = TableColumn<objetoMateriasPrimas, Float>()
        ColumnaStockMinimo.text = "Stock Minimo"
        ColumnaStockMinimo.setCellValueFactory(PropertyValueFactory<objetoMateriasPrimas, Float>("stockMinimo"))
        //DECLARAMOS LA TABLEVIEW REJILLA CODIGO, LA DAMOS UN TAMAÑO Y AÑADIMOS EN ELLA LAS COLUMNAS CREADAS ANTERIORMENTE
        var rejillaDatosCodigo: TableView<objetoMateriasPrimas> = TableView()

        rejillaDatosCodigo.setPrefSize(780.0,380.0)

        rejillaDatosCodigo.getColumns().addAll(ColumnaId, ColumnaDescripcion, ColumnaHorasFabricacion, ColumnaPrecioCoste, ColumnaPrecioVenta, ColumnaStock, ColumnaStockMinimo)

        // bobtener Datos del ResultSet

        try {
            //CARGAMOS EN LISTA ARTICULOS LOS DATOS CON LA FUNCION LISTADO DE LA CLASE OPERACIONES
            listaArticulos = bd.listado()

            //PARA CADA ELEMENTO EN LA LISTA ASIGNAMOS LOS VALORES O OBJETOPF Y LE AÑADIMOS A ALA REJILLA
            for (art in listaArticulos) {
                var objetoPF:objetoMateriasPrimas=objetoMateriasPrimas(art.id, art.descripcion, art.familia, art.precioCoste, art.imagen)

                rejillaDatosCodigo.items.add(objetoPF)

            }

        }catch (e: Exception) {
            e.printStackTrace()}

        val stage = (event.getSource() as Node).getScene().getWindow() as Stage
        stage.setAlwaysOnTop(true)

        var scene:Scene=stage.getScene()
        escenaActual=scene

        var pantallaRejilla: VBox = VBox()
        pantallaRejilla.setPrefSize(780.0,380.0)
        //ASIGNAMOS EL EVENTO AL BOTON VOLVER PARA PODER SALIR DE LA REJILLA
        var volver: Button = Button("Volver")

        volver.setOnAction { actionEvent ->

            try {

                stage.scene =escenaActual// scene
                stage.toFront()
                stage.show()

            } catch (e: Exception) {
                e.printStackTrace()
                println(e.message
                )
            }
        }


        // Detecta el evento del botón close(X) y lo desactiva
        stage.setOnCloseRequest { e: Event ->
            e.consume()
            //  println("Cerr<r2)")
            volver.fire()

        }
        pantallaRejilla.children.addAll(rejillaDatosCodigo,volver)
        pantallaRejilla.setLayoutX(10.0)
        pantallaRejilla.setLayoutY(10.0)
        scene = Scene(pantallaRejilla , 800.0, 400.0,true , SceneAntialiasing.BALANCED )
        stage.scene = scene
        stage.title="Gestión del Metal"

        val ico = Image("Imagenes/icono.jpg")
        stage.icons.add(ico)
        stage.y=300.1
        stage.x=300.1
        stage.toFront()
        stage.show()
    }

  @FXML
  //FUNCION PARA FINALIZAR QUE NOS CARGA DE NUEVO EL MENU INICIAL DE METAL.FXML
  fun Finalizar(event: ActionEvent) {

      try {
          val root = FXMLLoader.load<Parent>(javaClass.getResource("..//Metal.fxml"))

          val scene = Scene(root)
          val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
          scene.stylesheets.add("estilos.css")
          appStage.width = 1200.0
          appStage.height = 700.0
          appStage.scene = scene
          appStage.toFront()
          appStage.show()

      } catch (e: Exception) {e.printStackTrace()
      }
  }
  @FXML
  //FUNCION PARA SELECCIONAR IMAGENES
  fun SeleccionarImagen()
  {
      //CREAMOS UN FILECHOOSER
      val fileChooser: FileChooser = FileChooser()
      //AÑADIMOS FILTROS A NUESTRO FILECHOOSER PARA INDICAR QUE TIPO DE EXTENSIONES QUEREMOAS ADMITIR
      fileChooser.getExtensionFilters().addAll(
              FileChooser.ExtensionFilter("Text Files", "*.txt"),
              FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
              FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
              FileChooser.ExtensionFilter("All Files", "*.*"));
      val selectedFile = fileChooser.showOpenDialog(null)
      //SI LA SELECCION NO ES NULA TOMAMOS LA RUTA ABSOLUTA DEL ELEMENTO ESCOGIDO
      if (selectedFile != null) {
          try {
                  ficheroImagen.text = selectedFile.absolutePath

          } catch (ex: FileNotFoundException) {
              println(ex.message)
          }
      }
  }

  // Combo Box con los campos del resultSet Actual
  private fun rellenarListaCamposBuscar() {

    lc= bd.leerCamposTabla()
      try {
        listacamposBuscar.items.clear()
          for (i in 1..lc!!.getColumnCount()) {
              if (lc!!.getColumnType(i) == 12 || lc!!.getColumnType(i) == 4) {

                  listacamposBuscar.items.add(lc!!.getColumnName(i).toString())

              }
          }
      } catch (ex: SQLException) {
          Logger.getLogger(ControladorMateriasPrimas::class.java!!.getName()).log(Level.SEVERE, null, ex)
      }


  }
}

