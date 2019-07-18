package  escandallos
import java.io.FileReader


import com.google.gson.JsonElement
import com.google.gson.JsonParser
import escandallos.objetoArticuloFabricacion


class leerJson() {
    val stringList: ArrayList<String> = arrayListOf<String>()
    var listaArticulos: MutableList<objetoArticuloFabricacion> = mutableListOf<objetoArticuloFabricacion>()


    fun LeerArticulosPorFamilia(elemento: JsonElement, familia:Int):MutableList<objetoArticuloFabricacion> {
        if (elemento.isJsonObject) {


            val obj = elemento.asJsonObject


            var campoIdArticlo:Int=obj.get("iDarticulo").asInt

            var campoDescripcion:String=obj.get("Descripcion").toString()
            campoDescripcion= campoDescripcion.replace('"',' ')

            var campoFamilia:Int=obj.get("iDfamilia").asInt

            var campoPrecioCoste:Float=obj.get("precioCoste").asFloat

            var campoImagen:String=obj.get("imagen").toString()
            campoImagen= campoImagen.replace('"',' ')

            if(familia.equals(campoFamilia)) {
                var articluoLeido: objetoArticuloFabricacion = objetoArticuloFabricacion(campoIdArticlo, campoDescripcion, campoFamilia, campoPrecioCoste, campoImagen)

                listaArticulos.add(articluoLeido)
        }

        } else if (elemento.isJsonArray) {
            val array = elemento.asJsonArray
            //  println("Es array. Numero de elementos: " + array.size())

            val iter = array.iterator()
            while (iter.hasNext()) {
                val entrada = iter.next()
                LeerArticulosPorFamilia(entrada,familia)
            }
        } else if (elemento.isJsonPrimitive) {
            //   println("Es primitiva")
            val valor = elemento.asJsonPrimitive

            stringList.add(valor.toString())
            //  }
        } else if (elemento.isJsonNull) {
            println("Es NULL")
        } else {
            println("Es otra cosa")
        }

        return listaArticulos
    }



    fun LeerArticulos(elemento: JsonElement ): ArrayList<String> {
        if (elemento.isJsonObject) {


            val obj = elemento.asJsonObject


            val entradas = obj.entrySet()
            val iter = entradas.iterator()
            while (iter.hasNext()) {
                val entrada = iter.next()
             //   println("Clave: " + entrada.key)
            //    println("Valor: " + entrada.value)
                LeerArticulos(entrada.value )
            }

        } else if (elemento.isJsonArray) {
            val array = elemento.asJsonArray

            val iter = array.iterator()
            while (iter.hasNext()) {
                val entrada = iter.next()
                LeerArticulos(entrada )
            }
        } else if (elemento.isJsonPrimitive) {
            //   println("Es primitiva")
            val valor = elemento.asJsonPrimitive

            //    if (valor.isBoolean) {
            //   println("Es booleano: " + valor.asBoolean)
            //  } else if (valor.isNumber) {
            //      println("Es numero: " + valor.asNumber)
            //   } else if (valor.isString) {
            //      println("Es texto: " + valor.asString)
            print(valor.toString() + " \t\t")
            stringList.add(valor.toString())
            //  }
        } else if (elemento.isJsonNull) {
            println("Es NULL")
        } else {
            println("Es otra cosa")
        }

        return stringList
    }
    fun LeerFamilias(elemento: JsonElement): ArrayList<String> {
        if (elemento.isJsonObject) {

            val obj = elemento.asJsonObject
            val entradas = obj.entrySet()
            val iter = entradas.iterator()
            while (iter.hasNext()) {
                val entrada = iter.next()
            //    println("Clave: " + entrada.key)
            //    println("Valor: " + entrada.value)  campoDescripcion= campoDescripcion.replace('"',' ')
                if((entrada.key.toString().trim()).equals("Familia")){
                stringList.add(obj.get("idFamilia").toString().replace('"',' ')+"-"+entrada.value.toString().replace('"',' '))}
                LeerFamilias(entrada.value)
            }

        } else if (elemento.isJsonArray) {
            val array = elemento.asJsonArray


            val iter = array.iterator()
            while (iter.hasNext()) {
                val entrada = iter.next()
                LeerFamilias(entrada)
            }
        } else if (elemento.isJsonPrimitive) {
            //   println("Es primitiva")
            val valor = elemento.asJsonPrimitive


        } else if (elemento.isJsonNull) {
            println("Es NULL")
        } else {
            println("Es otra cosa")
        }
       // println("Enviados: "+stringList)
        return stringList
    }

}
fun main(args: Array<String>) {
    println("Leer Articulos")

    println("===========================================================")

    val parser = JsonParser()
    var fr = FileReader("G:\\Para lunes 29\\kEquipos\\src\\Articulos.json")
    var datos = parser.parse(fr)
    var obj: leerJson = leerJson()
    obj.LeerArticulos(datos )

    println("Leer Fanilias")
    println("===========================================================")
    fr = FileReader("G:\\Para lunes 29\\kEquipos\\src\\familias.json")
    datos = parser.parse(fr)
    obj.LeerFamilias(datos)


    println("Leer Articulos  Fanilias")
    println("===========================================================")
     fr = FileReader("G:\\Para lunes 29\\kEquipos\\src\\Articulos.json")
      datos = parser.parse(fr)

    obj.LeerArticulosPorFamilia(datos,2)


}