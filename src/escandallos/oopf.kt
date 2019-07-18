package baseDeDatos

public class oopf {
    private var id:Int=0
    private var descripcion:String=String()
    private var familia:Int=0
    private var precioCoste:Float=0f
    private var precioVenta:Float=0f
    private var stock:Int=0
    private var stockMinimo:Int=0
    private var imagen:String=String()
    fun  oopf(id:Int,  descripcion: String,  familia: Int, precioCoste:Float,  precioVenta:Float, stock:Int, stockMinimo:Int,imagen:String) {
        this.id = id
        this.descripcion = descripcion
        this.familia = familia
        this.precioCoste = precioCoste
        this.precioVenta = precioVenta
        this.stock = stock
        this.stockMinimo = stockMinimo
        this.imagen = imagen
    }

    fun   getId():Int {
        return  id;
    }

    fun    getDescripcion():String {
        return descripcion;
    }
    fun  getFamilia():Int {
        return familia
    }
    fun    getPrecioCoste():Float {
        return precioCoste;
    }
    fun    getPrecioVenta():Float {
        return precioVenta;
    }
    fun    getStock():Int {
        return stock;
    }
    fun    getStockMinimo():Int {
        return stockMinimo;
    }

    fun    getImagen():String {
        return imagen;
    }
}