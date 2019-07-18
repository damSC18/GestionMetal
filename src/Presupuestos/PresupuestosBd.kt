package Presupuestos




import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData


class PresupuestosBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var articuloLeido: objetoIdiomas = objetoIdiomas(0,0,"",0,"",0,"",0)

     var articuloPresupuesto:objetoPresupuesto=objetoPresupuesto(0,0,java.sql.Date(0),"","")

    var stmt: Statement? = null
    var OrdenSql:Statement? = null

    var connection: Connection? = null
    var rs: ResultSet? = null
    var  rsArticulosFabricados:ResultSet?= null
    var  rsArticulosFabricadosBuscar:ResultSet?= null

    //  var metaData: ResultSetMetaData? = null
    var columnNames = arrayOf<String>()

    fun conexionBd() {
        // Lee los parámetros de conexión al gestor de Base de Datos
        // establecidos en un fichero de texto. C:/GestionMetal/conexion.txt
        var UrlficheroConexion = System.getProperty("user.dir") + "\\conexion.txt"
        val parametrosConexion = FileInputStream(UrlficheroConexion).bufferedReader().use { it.readText() }

        var datosconexion=parametrosConexion.split(",")

        // val connectionString = "jdbc:sqlserver://90d18277a66e\\sqlexpress;database=metal;user=sa;password=sa_sa_2018@2018sa";
        // val connectionString = "jdbc:sqlserver://localhost:1433;database=metal;user=sa;password=dam2";
        val connectionString = "jdbc:sqlserver://"+datosconexion[0]+":"+datosconexion[1]+";database=Metal;user="+datosconexion[2]+";password="+datosconexion[3];

        try {

            //  Class.forName("org.sqlite.JDBC")

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionString)


            val SQL = "SELECT * FROM presupuestos"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }

    fun listado(): MutableList<objetoIdiomas> {
        // Declare the JDBC objects.

        var listaArticulos: MutableList<objetoIdiomas> = mutableListOf<objetoIdiomas>()
        val SQL = "SELECT * FROM DatosPorIdioma"

        try {

                       stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.
            while (rs!!.next()) {
                println("Dato " + rs!!.getString(1) + " " + rs!!.getString(2))
                var registroLeiodo = objetoIdiomas(rs!!.getInt(1),rs!!.getInt(2), rs!!.getString(3), rs!!.getInt(4) ,rs!!.getString(5), rs!!.getInt(6), rs!!.getString(7), rs!!.getInt(8))
                listaArticulos.add(registroLeiodo)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } /*finally {
                if (connection != null) try {
                    connection!!.close()
                } catch (e: Exception) {
                }
            }*/

        return listaArticulos
    }

    // Actualiza el cursos en memoria con los datos que leemos de la BD
    fun actualizaCursor() {
        val SQL = "SELECT * FROM Presupuestos"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffgrabaRegistro(articulo:objetoPresupuesto) {
        val SQL = " insert into Presupuestos (id_Cliente, Fecha,Descripcion,Estado) values("+ articulo.id_Cliente+ ",'" + articulo.Fecha + "','"+articulo.Descripcion+  "','" + articulo.Estado + "')"
        println("¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿" + SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun ffmodificaRegistro(articulo: objetoPresupuesto){
        val SQL = " update   Presupuestos set id_Cliente="+articulo.id_Cliente+",Fecha='"+articulo.Fecha+"',Descripcion='"+articulo.Descripcion+"',Estado='"+articulo.Estado+"'  where id_Presupuesto="+ articulo.id_Presupuesto
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffborraRegistro( idPresupuesto: Int) {

        val SQL = "delete  from Presupuestos where  id_Presupuesto="+idPresupuesto

        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD


        } catch (e: Exception) {
            e.printStackTrace()
        }



    }

    fun fleerPrimerRegistro(): objetoPresupuesto {
        try {
            rs!!.first()
            articuloPresupuesto = crearRegistroPresupuestoVisualizag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloPresupuesto
    }

    fun fleerUltimoRegistro(): objetoPresupuesto {
        try {
            rs!!.last()
            articuloPresupuesto = crearRegistroPresupuestoVisualizag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloPresupuesto
    }

    fun fleerSiguienteRegistro(): objetoPresupuesto {
        try {
            var existe: Boolean = rs!!.next()

            if (!existe) {

                rs!!.last()
            }
            articuloPresupuesto = crearRegistroPresupuestoVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloPresupuesto
    }

    fun fleerAnteriorRegistro(): objetoPresupuesto {


        try {

            var existe: Boolean = rs!!.previous()

            if (!existe) {

                rs!!.first()
            }
            articuloPresupuesto = crearRegistroPresupuestoVisualizag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloPresupuesto
    }

    fun leerRegistroDatosIdiomaParaPresupuesto(codigoIDioma:Int, claveArticuloFabricado:Int) :objetoIdiomas{
        val SQL = "SELECT * FROM DatosPorIdioma where id_idioma=" + codigoIDioma +" and id_ArticuloFabricado= "+claveArticuloFabricado
println(SQL)


        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        var objetoConLosIdiomas:objetoIdiomas = objetoIdiomas(0,0,"",0,"",0,"",0)
        if(rs!!.first()){
          objetoConLosIdiomas=crearRegistroArticuloVisualizag( )
       }
        return objetoConLosIdiomas
    }

    fun crearRegistroArticuloVisualizag(): objetoIdiomas {

        var idIdiomaLeido: Int = rs!!.getInt(1)
        var idArticuloFabridado: Int = rs!!.getInt(2)
        var tecnicosLeido: String = rs!!.getString(3)
        var idMultimediaTecnicos: Int = rs!!.getInt(4)
        var mantenimientoLeido: String = rs!!.getString(5)
        var idMultimediaMantenimiento: Int = rs!!.getInt(6)
        var montajeLeido: String = rs!!.getString(7)
        var idMultimediaMontaje: Int = rs!!.getInt(8)




        articuloLeido=objetoIdiomas (idIdiomaLeido, idArticuloFabridado,tecnicosLeido, idMultimediaTecnicos,mantenimientoLeido,idMultimediaMantenimiento,montajeLeido,idMultimediaMontaje)

        return articuloLeido


    }


    fun crearRegistroPresupuestoVisualizag():objetoPresupuesto {

        var idPresupuesto: Int = rs!!.getInt(1)
        var idCliente: Int = rs!!.getInt(2)
        var Fecha: java.sql.Date = rs!!.getDate(3)
        var Descirocion: String = rs!!.getString(4)
        var Estado: String = rs!!.getString(5)

        articuloPresupuesto=objetoPresupuesto (idPresupuesto, idCliente,Fecha, Descirocion,Estado)

        return articuloPresupuesto

    }




    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): ResultSet {

        try {

            var en = false
            if (rsArticulosFabricadosBuscar!!.isAfterLast()) {
                rsArticulosFabricadosBuscar!!.last()
            }
            if (rsArticulosFabricadosBuscar!!.isBeforeFirst()) {
                rsArticulosFabricadosBuscar!!.first()
            }
            posicion = rsArticulosFabricadosBuscar!!.getRow()

            while (rsArticulosFabricadosBuscar!!.next()) {

                if (rsArticulosFabricadosBuscar!!.getObject(snc) != null) {
                    var cadena: String = rsArticulosFabricadosBuscar!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {


                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rsArticulosFabricadosBuscar!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {ex.printStackTrace()
        }
        return rsArticulosFabricadosBuscar!!

       // return rsArticulosFabricadosBuscar!!.getString(1)+"-"+rsArticulosFabricadosBuscar!!.getString(2)
    }

    fun fleerAnteriorBuuscar(datoAbuscar: String, snc: String): String {

        try {
            var en = false
            if (rsArticulosFabricadosBuscar!!.isAfterLast()) {
                rsArticulosFabricadosBuscar!!.last()
            }
            if (rsArticulosFabricadosBuscar!!.isBeforeFirst()) {
                rsArticulosFabricadosBuscar!!.first()
            }
            posicion = rsArticulosFabricadosBuscar!!.getRow()

            while (rsArticulosFabricadosBuscar!!.previous()) {
                if (rsArticulosFabricadosBuscar!!.getObject(snc) != null) {
                    var cadena: String = rsArticulosFabricadosBuscar!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {

                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rsArticulosFabricadosBuscar!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {
        }
         return rsArticulosFabricadosBuscar!!.getString(1)+"-"+rsArticulosFabricadosBuscar!!.getString(2)
    }
    // Obtener la descripcion del Registro Fabricado asociado al registro Idiomas
    fun leerproductoFabricado(codigo:Int ):String
    {
        val SQL = "SELECT * FROM  ArticulosFabricados where id_ArticuloFabricado="+codigo

        OrdenSql = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);


        rsArticulosFabricados = OrdenSql!!.executeQuery(SQL)
        var resultado:String="No existe Articulo Fabricado asociado a ese Id"
        if(rsArticulosFabricados!!.first()){
            resultado= rsArticulosFabricados!!.getString(2).toString()
        }
        return resultado
    }

    fun leerCamposTabla(): ResultSetMetaData {
        val SQL = "SELECT * FROM  ArticulosFabricados"

         OrdenSql = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);


        rsArticulosFabricadosBuscar = OrdenSql!!.executeQuery(SQL)
        lc = rsArticulosFabricadosBuscar!!.getMetaData()


        return lc!!
    }

    fun  leeCliente(sql:String ):ResultSet{
        println(sql)
        var OrdenSqlCliente:Statement? = null

        var rsCliente: ResultSet? = null

        OrdenSqlCliente = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rsCliente = OrdenSqlCliente.executeQuery(sql)

        return rsCliente
//////////////////////////////////////////////

    }
// Obtener el id autoincrement  del ultimo registro grabado en la tabla Presupuestos
    fun ffleerUltimoClaveGrabadaAutoincrement():Int
    { println("AUTO INCREMENT  ..: "+ "AQUI")
        val SQL="SELECT IDENT_CURRENT ('Presupuestos') AS Current_Identity;"
        var autoincrement: ResultSet = stmt!!.executeQuery(SQL)
        var correcto:Boolean=autoincrement.first()
        var id_NuevoArticuloFabricado:Int=0;
        if(correcto){
            id_NuevoArticuloFabricado=autoincrement.getInt(1)
        }

        return id_NuevoArticuloFabricado
    }

    // Grabar lineas Presupuesto
   fun grabarLineasPresupuesto(LineasPresupuesto:HashMap<String, objetosLP>,idPresupuesto:Int,nuevo:Boolean){
       // Si el presupusto existe le pasamos el idPresuesto leido
        var idUltimoPresupuesto:Int=idPresupuesto
        // Si es presupuesto Nuevo obtenemos el ide del ultimo grabado +1
        if(nuevo){
         idUltimoPresupuesto=ffleerUltimoClaveGrabadaAutoincrement()
        }
        println(idUltimoPresupuesto)
        var datosLineas=objetosLP("", Button(),TextField(), TextField(),Button(), TextArea(),TextArea(),TextArea())


        for ((key, value) in LineasPresupuesto) {
            datosLineas=value
            println(key +" Registro HasMap :" +datosLineas.descripcionLineaPresupuesto)
            println("________________________________"+datosLineas)
            var estadoLinea:String=datosLineas.estadoLinea.text.toString()
            var SQL="insert into LineasPresupuestos (" +
                    "id_Presupuesto,DescripcionLinea,id_Articulo,Precio,Cantidad,DatosTecnicos,DatosMantenimiento,DatosMontaje,Estado)"+
                    " values("+idUltimoPresupuesto+",'"+datosLineas.descripcionLineaPresupuesto +"',"+datosLineas.codigoArticuloCreado.text.toInt()+","+
            datosLineas.precioArticulos.text.toFloat()+","+datosLineas.cantidadArticulos.text.toInt()+
            ",'"+datosLineas.TATecnicos.text+"','"+datosLineas.TAMantenimiento.text+"','"+datosLineas.TAMontaje.text+"','"+estadoLinea+"')"


             println(SQL)
          //  javax.swing.JOptionPane.showMessageDialog(null,SQL)

           try{
            stmt!!.execute(SQL)}
           catch(ex:Exception){ex.printStackTrace()}
        }
    }
    // Borrar  lineas Presupuesto a modificar par despues insertar las nuevas
    fun borrarLineasPresupuesto(idPresupuesto:Int){

        var idUltimoPresupuesto:Int=ffleerUltimoClaveGrabadaAutoincrement()

            var SQL="delete  LineasPresupuestos where id_Presupuesto=" +idPresupuesto

            try{
                stmt!!.execute(SQL)}
            catch(ex:Exception){ex.printStackTrace()}

    }

    fun leerLineasPresupuesto(idPresupuesto:String):ResultSet
    {

        val SQL = "SELECT * FROM LineasPresupuestos where  id_Presupuesto ="+idPresupuesto +" order by Descripcionlinea"



        var stmt:Statement = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       var  rs:ResultSet = stmt.executeQuery(SQL)
        return rs
    }

    fun leerArticuloFabricado(Codigo:String):ResultSet
    {
        val SQL = "SELECT * FROM  ArticulosFabricados where id_ArticuloFabricado="+Codigo

        OrdenSql = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        rsArticulosFabricados = OrdenSql!!.executeQuery(SQL)
        return rsArticulosFabricados!!
    }
}