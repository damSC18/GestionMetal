package Taller




import Presupuestos.objetoIdiomas
import Presupuestos.objetoPresupuesto
import Presupuestos.objetosLP
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData


class TallerBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var articuloLeido: objetoIdiomas = objetoIdiomas(0,0,"",0,"",0,"",0)

     var articuloPresupuesto: objetoPresupuesto =objetoPresupuesto(0,0,java.sql.Date(0),"","")

    var stmt: Statement? = null
    var OrdenSql:Statement? = null

    var connection: Connection? = null
    var rs: ResultSet? = null
    var  rsArticulosFabricados:ResultSet?= null
    var  rsrsPresupuestosBuscar:ResultSet?= null

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


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }

    fun ejecutaConsulta(sql:String):ResultSet
    {

        var OrdenSqlConsultapresupuestos:Statement? = null

        var rsConsulta: ResultSet? = null

        OrdenSqlConsultapresupuestos = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rsConsulta = OrdenSqlConsultapresupuestos.executeQuery(sql)

        return rsConsulta


    }



    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): ResultSet {

        try {

            var en = false
            if (rsrsPresupuestosBuscar!!.isAfterLast()) {
                rsrsPresupuestosBuscar!!.last()
            }
            if (rsrsPresupuestosBuscar!!.isBeforeFirst()) {
                rsrsPresupuestosBuscar!!.first()
            }
            posicion = rsrsPresupuestosBuscar!!.getRow()

            while (rsrsPresupuestosBuscar!!.next()) {

                if (rsrsPresupuestosBuscar!!.getObject(snc) != null) {
                    var cadena: String = rsrsPresupuestosBuscar!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {


                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rsrsPresupuestosBuscar!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {ex.printStackTrace()
        }
        return rsrsPresupuestosBuscar!!


    }

    fun fleerAnteriorBuuscar(datoAbuscar: String, snc: String):  ResultSet  {

        try {

            var en = false
            if (rsrsPresupuestosBuscar!!.isAfterLast()) {
                rsrsPresupuestosBuscar!!.last()
            }
            if (rsrsPresupuestosBuscar!!.isBeforeFirst()) {
                rsrsPresupuestosBuscar!!.first()
            }
            posicion = rsrsPresupuestosBuscar!!.getRow()

            while (rsrsPresupuestosBuscar!!.previous()) {

                if (rsrsPresupuestosBuscar!!.getObject(snc) != null) {
                    var cadena: String = rsrsPresupuestosBuscar!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {


                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rsrsPresupuestosBuscar!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {ex.printStackTrace()
        }
        return rsrsPresupuestosBuscar!!

    }


    fun leerCamposTabla(): ResultSetMetaData {
        val SQL = "SELECT * FROM  Presupuestos"

         OrdenSql = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);


        rsrsPresupuestosBuscar = OrdenSql!!.executeQuery(SQL)
        lc = rsrsPresupuestosBuscar!!.getMetaData()


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