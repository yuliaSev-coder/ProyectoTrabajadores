package com.example.proyectotrabajadores

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : ComponentActivity() {

    private lateinit var adapter: HistorialAdapter
    private lateinit var textoContador: TextView
    private lateinit var textoListaVacia: TextView
    private lateinit var recycler: RecyclerView

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

        // Configuramos el Adapter con la lista compartida y acción al pulsar cada elemento
        adapter = HistorialAdapter(Historial.trabajadores) { trabajador ->
            // Al pulsar una fila, abrimos la ficha detallada de ese trabajador
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("NOMBRE_EXTRA", trabajador.nombre)
                putExtra("APELLIDOS_EXTRA", trabajador.apellidos)
                putExtra("OFICIO_EXTRA", trabajador.oficio)
                putExtra("FOTO_EXTRA", trabajador.fotoResId)
            }
            startActivity(intent)
        }
        recycler.adapter = adapter

        actualizarEstadoLista()

        // Botón para vaciar el historial
        botonVaciar.setOnClickListener {
            if (Historial.trabajadores.isEmpty()) {
                Toast.makeText(this, "El historial ya está vacío", Toast.LENGTH_SHORT).show()
            } else {
                Historial.trabajadores.clear()
                adapter.notifyDataSetChanged()
                actualizarEstadoLista()
                Toast.makeText(this, "Historial vaciado correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para volver a la pantalla anterior
        botonVolver.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Si regresamos desde otra pantalla, refrescamos la lista y el contador
        adapter.notifyDataSetChanged()
        actualizarEstadoLista()
    }

    private fun actualizarEstadoLista() {
        val total = Historial.trabajadores.size
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
