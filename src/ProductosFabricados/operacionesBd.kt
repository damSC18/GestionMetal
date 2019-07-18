package ProductosFabricados

//Use the JDBC driver


import ProductosFabricados.objetoArticuloFabricado

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.FileInputStream

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData
import java.util.ArrayList


class operacionesBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var articuloLeido =  objetoArticuloFabricado(0,"",0,0f,0f,0,0,"")
    var stmt: Statement? = null
    var connection: Connection? = null
    var rs: ResultSet? = null

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


            val SQL = "SELECT * FROM ArticulosFabricados"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }

    fun listado(): MutableList<objetoArticuloFabricado> {
        // Declare the JDBC objects.

        var listaArticulos: MutableList<objetoArticuloFabricado> = mutableListOf<objetoArticuloFabricado>()
        val SQL = "SELECT * FROM ArticulosFabricados"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)

            val SQL = "SELECT * FROM ArticulosFabricados"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.
            while (rs!!.next()) {
                println("Dato " + rs!!.getString(1) + " " + rs!!.getString(2))
                var registroLeiodo = objetoArticuloFabricado(rs!!.getInt(1), rs!!.getString(2), rs!!.getInt(3), rs!!.getFloat(4), rs!!.getFloat(5), rs!!.getInt(6), rs!!.getInt(7), rs!!.getString(8))
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
        val SQL = "SELECT * FROM ArticulosFabricados"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffgrabaRegistro(articulo:  objetoArticuloFabricado) {
        val SQL = " insert into ArticulosFabricados (Descripcion,id_Familia,PrecioCoste,PrecioVenta,Stock,StockMinimo,Imagen) values(" + "'" + articulo.descripcion + "'," + articulo.familia + "," + articulo.precioCoste + "," + articulo.precioVenta + "," + articulo.stock+ "," + articulo.stockMinimo+ ",'" + articulo.imagen+ "')"
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

    fun ffmodificaRegistro(articulo:  objetoArticuloFabricado){
        val SQL = " update   ArticulosFabricados set Descripcion='"+articulo.descripcion+"',id_Familia="+articulo.familia+",PrecioCoste="+articulo.precioCoste+",PrecioVenta="+articulo.precioVenta+",Stock="+articulo.stock+",StockMinimo="+articulo.stockMinimo+",Imagen='"+articulo.imagen+"' where id_ArticuloFabricado="+articulo.id
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

    fun ffborraRegistro(idProducto: Int) {

        val SQL = "delete  from  ArticulosFabricados where id_ArticuloFabricado=" + idProducto
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        actualizaCursor()
    }

    fun fleerPrimerRegistro():  objetoArticuloFabricado {


        try {

            rs!!.first()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }


    fun fleerUltimoRegistro():  objetoArticuloFabricado {


        try {

            rs!!.last()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    fun fleerSiguienteRegistro():  objetoArticuloFabricado {

        try {

            var existe: Boolean = rs!!.next()

            if (!existe) {

                rs!!.last()
            }
            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    fun fleerAnteriorRegistro():  objetoArticuloFabricado {


        try {

            var existe: Boolean = rs!!.previous()

            if (!existe) {

                rs!!.first()
            }
            articuloLeido = crearRegistroArticuloVisualizag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    fun crearRegistroArticuloVisualizag():objetoArticuloFabricado {

        var idLeido: Int = rs!!.getInt("id_ArticuloFabricado")
        var descripcionLeido: String = rs!!.getString("Descripcion").toString()
        var familiaLeido: Int = rs!!.getInt(3)
        var precioCoste: Float = rs!!.getFloat(4)
        var precioVenta: Float = rs!!.getFloat(5)
        var StockLeido: Int = rs!!.getInt(6)
        var StockMinimoLeido: Int = rs!!.getInt(7)
        var ImagenLeido: String = rs!!.getString(8).toString()



        var articuloLeido= objetoArticuloFabricado(idLeido, descripcionLeido, familiaLeido, precioCoste, precioVenta, StockLeido, StockMinimoLeido, ImagenLeido)

        return articuloLeido


    }


    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): objetoArticuloFabricado {

        try {
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()
//javax.swing.JOptionPane.showMessageDialog(null,posicion)
            while (rs!!.next()) {
                if (rs!!.getObject(snc) != null) {

                    //javax.swing.JOptionPane.showMessageDialog(null,rs!!.getRow())
                    var cadena: String = rs!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc:Int = cadena.indexOf(subcadena)

                    if (enc != -1) {
                        articuloLeido = crearRegistroArticuloVisualizag()

                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rs!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {
        }
        return articuloLeido
    }

    fun fleerAnteriorBuuscar(datoAbuscar: String, snc: String): objetoArticuloFabricado {

        try {
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()

            while (rs!!.previous()) {
                if (rs!!.getObject(snc) != null) {
                    var cadena: String = rs!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {
                        articuloLeido = crearRegistroArticuloVisualizag()

                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rs!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {
        }
        return articuloLeido
    }



    fun leerCamposTabla(): ResultSetMetaData {

        lc = rs!!.getMetaData()

        return lc!!
    }


    // Leer el Scandallo, materias primas, de un Producto fabricado.
// SELECT * FROM  ArticulosFabricados
//  INNER JOIN EscandallosArticulosFabricados ON EscandallosArticulosFabricados.id_ArticuloFabricado = ArticulosFabricados.id_ArticuloFabricado
// INNER JOIN "MateriaPrimas" ON MateriaPrimas.id_MateriaPrima = EscandallosArticulosFabricados.id_MateriaPrima
// where ArticulosFabricados.id_ArticuloFabricado =5013
fun escandalloProductoFabricado(  codigoArticulo:Int):ArrayList<String>
    {
        var SQL:String="SELECT MateriaPrimas.* , EscandallosArticulosFabricados.*\n" +
                "       FROM  ArticulosFabricados as AF\n" +
                "       INNER JOIN EscandallosArticulosFabricados ON\n" +
                "          EscandallosArticulosFabricados.id_ArticuloFabricado = AF.id_ArticuloFabricado\n" +
                "          INNER JOIN  MateriaPrimas  ON\n" +
                "         MateriaPrimas.id_MateriaPrima = EscandallosArticulosFabricados.id_MateriaPrima\n" +
                "         where AF.id_ArticuloFabricado = "+codigoArticulo

        val data = ArrayList<String>()
        try {

             stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);



            var  rssss:ResultSet = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.


            data.add("Materia Prima - Cantidad")
            try{
            while (rssss!!.next() ) {
           //     println("Dato " + rs!!.getString(1) + " " + rssss!!.getString(2))

                data.add( rssss!!.getString("Descripcion")+" - "+ rssss!!.getInt("cantidad"));//+" - "+rssss!!.getInt(1))
            }
            }catch(e:Exception){}

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }
}