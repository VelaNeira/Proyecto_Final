package com.example.parcial2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.parcial2.DAO.DetallePedidoDao
import com.example.parcial2.MODEL.DetallePedido
import com.example.parcial2.MODEL.Mesa
import com.example.parcial2.DAO.MesaDao
import com.example.parcial2.DAO.PedidoDao
import com.example.parcial2.DAO.ProductoDao
import com.example.parcial2.DAO.UsuarioDao
import com.example.parcial2.MODEL.Pedido
import com.example.parcial2.MODEL.Producto
import com.example.parcial2.MODEL.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Mesa::class, Producto::class, Pedido::class, DetallePedido::class, Usuario::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mesaDao(): MesaDao
    abstract fun productoDao(): ProductoDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detallePedidoDao(): DetallePedidoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Esto recreará la base de datos
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Callback para insertar datos predeterminados al crear la base de datos
class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getDatabase(context)

            // Crear usuarios predeterminados
            val usuarioDao = database.usuarioDao()
            val usuarios = listOf(
                Usuario(
                    username = "admin",
                    password = "admin123",
                    rol = "ADMIN"
                ),
                Usuario(
                    username = "mesero",
                    password = "mesero123",
                    rol = "MESERO"
                )
            )
            usuarios.forEach { usuario ->
                usuarioDao.insertUsuario(usuario)
            }

            // Agregar los datos predeterminados en un hilo separado
            CoroutineScope(Dispatchers.IO).launch {
                val database = AppDatabase.getDatabase(context)
                val mesaDao = database.mesaDao()
                val productoDao = database.productoDao()

                // Insertamos las 9 mesas con un estado predeterminado de "Libre"
                val mesas = listOf(
                    Mesa(nombre = "Mesa 1", estado = "Libre"),
                    Mesa(nombre = "Mesa 2", estado = "Libre"),
                    Mesa(nombre = "Mesa 3", estado = "Libre"),
                    Mesa(nombre = "Mesa 4", estado = "Libre"),
                    Mesa(nombre = "Mesa 5", estado = "Libre"),
                    Mesa(nombre = "Mesa 6", estado = "Libre"),
                    Mesa(nombre = "Mesa 7", estado = "Libre"),
                    Mesa(nombre = "Mesa 8", estado = "Libre"),
                    Mesa(nombre = "Mesa 9", estado = "Libre")
                )

                // Inserta todas las mesas de forma concurrente
                mesaDao.insertMesas(mesas)

                // Insertamos 10 productos entre bebidas y comida
                val productos = listOf(
                    // Bebidas
                    Producto(nombre = "Coca Cola", precio = 8.0, categoria = "Bebida"),
                    Producto(nombre = "Pepsi", precio = 7.5, categoria = "Bebida"),
                    Producto(nombre = "Fanta", precio = 7.0, categoria = "Bebida"),
                    Producto(nombre = "Sprite", precio = 7.0, categoria = "Bebida"),
                    Producto(nombre = "Jugo de Naranja", precio = 10.0, categoria = "Bebida"),
                    Producto(nombre = "Jugo de Manzana", precio = 10.0, categoria = "Bebida"),
                    Producto(nombre = "Jugo de Uva", precio = 9.5, categoria = "Bebida"),
                    Producto(nombre = "Agua Mineral", precio = 5.0, categoria = "Bebida"),
                    Producto(nombre = "Limonada", precio = 6.5, categoria = "Bebida"),

                    // Comida
                    Producto(nombre = "Hamburguesa", precio = 15.0, categoria = "Comida"),
                    Producto(nombre = "Pizza", precio = 20.0, categoria = "Comida"),
                    Producto(nombre = "Tacos", precio = 12.0, categoria = "Comida"),
                    Producto(nombre = "Ensalada", precio = 10.0, categoria = "Comida"),
                    Producto(nombre = "Sándwich", precio = 8.0, categoria = "Comida"),
                    Producto(nombre = "Papas Fritas", precio = 5.5, categoria = "Comida"),
                    Producto(nombre = "Pollo a la Parrilla", precio = 18.0, categoria = "Comida"),
                    Producto(nombre = "Spaghetti", precio = 14.0, categoria = "Comida")
                )

                // Inserta todos los productos de forma concurrente
                productos.forEach { producto -> productoDao.insertProducto(producto) }
            }
        }
    }
}
