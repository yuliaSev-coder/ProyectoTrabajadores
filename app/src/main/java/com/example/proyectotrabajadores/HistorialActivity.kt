package com.example.proyectotrabajadores

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HistorialActivity : ComponentActivity() {

    private lateinit var adapter: HistorialAdapter
    private lateinit var textoContador: TextView
    private lateinit var textoListaVacia: TextView
    private lateinit var recycler: RecyclerView
    private val listaTrabajadores = mutableListOf<Trabajador>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        textoContador = findViewById(R.id.textoContador)
        textoListaVacia = findViewById(R.id.textoListaVacia)
        recycler = findViewById(R.id.recyclerTrabajadores)
        val botonVaciar = findViewById<Button>(R.id.botonVaciarHistorial)
        val botonVolver = findViewById<Button>(R.id.botonVolverHistorial)

        // Configuramos el RecyclerView con LinearLayoutManager
        recycler.layoutManager = LinearLayoutManager(this)

        // Configuramos el Adapter con click corto (abrir detalle) y click largo (borrar elemento)
        adapter = HistorialAdapter(
            trabajadores = listaTrabajadores,
            onItemClick = { trabajador ->
                val fotoResId = when (trabajador.oficio) {
                    "Albañil" -> R.drawable.albanil
                    "Electricista" -> R.drawable.electricista
                    "Fontanero" -> R.drawable.fontanero
                    else -> R.drawable.albanil
                }
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("NOMBRE_EXTRA", trabajador.nombre)
                    putExtra("APELLIDOS_EXTRA", trabajador.apellidos)
                    putExtra("OFICIO_EXTRA", trabajador.oficio)
                    putExtra("FOTO_EXTRA", fotoResId)
                }
                startActivity(intent)
            },
            onItemLongClick = { trabajador ->
                // Borrar un elemento manteniendo pulsada la fila
                lifecycleScope.launch {
                    val db = AppDatabase.obtener(this@HistorialActivity)
                    db.trabajadorDao().borrarPorId(trabajador.id)
                    Toast.makeText(
                        this@HistorialActivity,
                        "Trabajador ${trabajador.nombre} eliminado",
                        Toast.LENGTH_SHORT
                    ).show()
                    cargarTrabajadoresDesdeBaseDeDatos()
                }
            }
        )
        recycler.adapter = adapter

        // Botón para vaciar el historial en la base de datos
        botonVaciar.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabase.obtener(this@HistorialActivity)
                db.trabajadorDao().vaciarHistorial()
                cargarTrabajadoresDesdeBaseDeDatos()
                Toast.makeText(this@HistorialActivity, "Historial vaciado correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para volver a la pantalla anterior
        botonVolver.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Cargamos los trabajadores desde la base de datos cada vez que la pantalla vuelve a ser visible
        cargarTrabajadoresDesdeBaseDeDatos()
    }

    private fun cargarTrabajadoresDesdeBaseDeDatos() {
        lifecycleScope.launch {
            val db = AppDatabase.obtener(this@HistorialActivity)
            val trabajadoresDb = db.trabajadorDao().obtenerTodos()

            listaTrabajadores.clear()
            listaTrabajadores.addAll(trabajadoresDb)

            adapter.notifyDataSetChanged()
            actualizarEstadoLista()
        }
    }

    private fun actualizarEstadoLista() {
        val total = listaTrabajadores.size
        textoContador.text = "$total trabajador${if (total == 1) "" else "es"} registrado${if (total == 1) "" else "s"}"

        if (total == 0) {
            textoListaVacia.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            textoListaVacia.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }
}
