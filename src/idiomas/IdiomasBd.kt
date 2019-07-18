package idiomas




import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData


class IdiomasBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var articuloLeido: objetoIdiomas = objetoIdiomas(0,0,"",0,"",0,"",0)
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


            val SQL = "SELECT * FROM DatosPorIdioma"



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
        val SQL = "SELECT * FROM DatosPorIdioma"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffgrabaRegistro(articulo: objetoIdiomas) {
        val SQL = " insert into DatosPorIdioma (id_Idioma,id_ArticuloFabricado, DatosTecnicos,id_Multimedia_DTecnicos,DatosMantenimiento,id_Multimedia_DMantenimiento,DatosMontaje,id_Multimedia_DMontaje) values(" + "" + articulo.id_idioma+ "," +articulo.id_articuloFabricado+ ",'" + articulo.DatosTecnicos + "',"+articulo.MDtecnicos+  ",'" + articulo.DatosMantenimiento + "',"+articulo.MDMantenimiento+ ",'" + articulo.DatosMontaje + "',"+articulo.MDmontaje+")"
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

    fun ffmodificaRegistro(articulo: objetoIdiomas){
        val SQL = " update   DatosPorIdioma set id_Idioma="+articulo.id_idioma+",id_ArticuloFabricado="+articulo.id_articuloFabricado+",DatosTecnicos='"+articulo.DatosTecnicos+"',id_Multimedia_DTecnicos="+articulo.MDtecnicos+",DatosMantenimiento='"+articulo.DatosMantenimiento+"',id_Multimedia_DMantenimiento="+articulo.MDMantenimiento+",DatosMontaje='"+articulo.DatosMontaje+"',id_Multimedia_DMontaje="+articulo.MDmontaje+" where id_Idioma="+ articulo.id_idioma+" and id_ArticuloFabricado="+articulo.id_articuloFabricado
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

    fun ffborraRegistro(idIdioma:Int,idProducto: Int) {

        val SQL = "delete  from  DatosPorIdioma where  id_Idioma="+idProducto+" and id_ArticuloFabricado="+idProducto
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

    fun fleerPrimerRegistro(): objetoIdiomas {


        try {

            rs!!.first()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }


    fun fleerUltimoRegistro(): objetoIdiomas {


        try {

            rs!!.last()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    fun fleerSiguienteRegistro(): objetoIdiomas {


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

    fun fleerAnteriorRegistro(): objetoIdiomas {


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


    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): String {

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

        return rsArticulosFabricadosBuscar!!.getString(1)+"-"+rsArticulosFabricadosBuscar!!.getString(2)
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


}