package Multimedia




import Multimedia.objetoMultimedia
import java.io.FileInputStream


import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData
import java.util.ArrayList


class MultimediaBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var articuloLeido: objetoMultimedia = objetoMultimedia(0,"",0,"")
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


            val SQL = "SELECT * FROM multimedia"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }

    fun listado(): MutableList<objetoMultimedia> {
        // Declare the JDBC objects.

        var listaArticulos: MutableList<objetoMultimedia> = mutableListOf<objetoMultimedia>()
        val SQL = "SELECT * FROM multimedia"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)

            val SQL = "SELECT * FROM multimedia"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.
            while (rs!!.next()) {
                println("Dato " + rs!!.getString(1) + " " + rs!!.getString(2))
                var registroLeiodo = objetoMultimedia(rs!!.getInt(1), rs!!.getString(2), rs!!.getInt(3) ,rs!!.getString(4))
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
        val SQL = "SELECT * FROM multimedia"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)

            val SQL = "SELECT * FROM multimedia"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffgrabaRegistro(articulo: objetoMultimedia) {
        val SQL = " insert into multimedia (Descripcion,tipo, url) values(" + "'" + articulo.descripcion+ "'," + articulo.tipo + ",'" + articulo.url + "')"
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

    fun ffmodificaRegistro(articulo: objetoMultimedia){
        val SQL = " update   multimedia set descripcion='"+articulo.descripcion+"',tipo="+articulo.tipo+",url='"+articulo.url+"' where id_multimedia="+articulo.id
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

        val SQL = "delete  from  multimedia where id_multimedia=" + idProducto
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

    fun fleerPrimerRegistro(): objetoMultimedia {


        try {

            rs!!.first()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }


    fun fleerUltimoRegistro(): objetoMultimedia {


        try {

            rs!!.last()

            articuloLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    fun fleerSiguienteRegistro(): objetoMultimedia {


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

    fun fleerAnteriorRegistro(): objetoMultimedia {


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

    fun crearRegistroArticuloVisualizag(): objetoMultimedia {

        var idLeido: Int = rs!!.getInt(1)
        var descripcionLeido: String = rs!!.getString(2).toString()
        var tipoLeido: Int = rs!!.getInt(3)

        var urlLeido: String = rs!!.getString(4).toString()



        articuloLeido=objetoMultimedia (idLeido, descripcionLeido,tipoLeido, urlLeido)

        return articuloLeido


    }


    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): objetoMultimedia {

        try {
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()

            while (rs!!.next()) {
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

    fun fleerAnteriorBuuscar(datoAbuscar: String, snc: String): objetoMultimedia {

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


}