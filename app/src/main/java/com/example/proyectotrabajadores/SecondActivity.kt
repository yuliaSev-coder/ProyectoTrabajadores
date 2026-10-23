package com.example.proyectotrabajadores

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class SecondActivity : ComponentActivity() {

    private var reproductor: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textoNombreCompleto = findViewById<TextView>(R.id.textoNombreCompleto)
        val textoOficioDetalle = findViewById<TextView>(R.id.textoOficioDetalle)
        val imagenFotoDetalle = findViewById<ImageView>(R.id.imagenFotoDetalle)
        val botonVolver = findViewById<Button>(R.id.botonVolver)
        val botonVerLista = findViewById<Button>(R.id.botonVerListaDesdeDetalle)

        // Recogemos los datos enviados por el Intent
        val nombre = intent.getStringExtra("NOMBRE_EXTRA") ?: "Empleado"
        val apellidos = intent.getStringExtra("APELLIDOS_EXTRA") ?: ""
        val oficio = intent.getStringExtra("OFICIO_EXTRA") ?: "Albañil"
        val fotoResId = intent.getIntExtra("FOTO_EXTRA", R.drawable.albanil)

        // Mostramos los datos en la pantalla
        textoNombreCompleto.text = "$nombre $apellidos"
        textoOficioDetalle.text = "Puesto: $oficio"
        imagenFotoDetalle.setImageResource(fotoResId)

        // Asignamos y reproducimos un sonido según el oficio elegido
        val sonidoId: Int = when (oficio) {
            "Albañil" -> R.raw.sonido1
            "Electricista" -> R.raw.sonido2
            "Fontanero" -> R.raw.sonido3
            else -> R.raw.sonido1
        }

        reproductor = MediaPlayer.create(this, sonidoId)
        reproductor?.start()

        // Botón para volver a la pantalla anterior
        botonVolver.setOnClickListener {
            finish()
        }

        // Botón para navegar al listado de trabajadores
        botonVerLista.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberamos el reproductor para evitar consumo innecesario de memoria
        reproductor?.release()
        reproductor = null
    }
}
