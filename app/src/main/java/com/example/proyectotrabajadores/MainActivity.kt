package com.example.proyectotrabajadores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var fotoSeleccionadaId: Int = R.drawable.albanil
    private var oficioSeleccionado: String = "Albañil"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editApellidos = findViewById<EditText>(R.id.editApellidos)
        val imagenFotoPreview = findViewById<ImageView>(R.id.imagenFotoPreview)
        val radioGroupOficio = findViewById<RadioGroup>(R.id.radioGroupOficio)
        val botonRegistrar = findViewById<Button>(R.id.botonRegistrar)
        val botonVerHistorial = findViewById<Button>(R.id.botonVerHistorial)
        val botonOlvidar = findViewById<Button>(R.id.botonOlvidar)

        // SharedPreferences: para recordar el último trabajador guardado
        val prefs = getSharedPreferences("MisDatosTrabajadores", MODE_PRIVATE)
        val nombreGuardado = prefs.getString("NOMBRE_GUARDADO", "") ?: ""
        val apellidosGuardados = prefs.getString("APELLIDOS_GUARDADOS", "") ?: ""

        editNombre.setText(nombreGuardado)
        editApellidos.setText(apellidosGuardados)

        // Cambio interactivo de foto y oficio según la opción seleccionada
        radioGroupOficio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioAlbanil -> {
                    fotoSeleccionadaId = R.drawable.albanil
                    oficioSeleccionado = "Albañil"
                }
                R.id.radioElectricista -> {
                    fotoSeleccionadaId = R.drawable.electricista
                    oficioSeleccionado = "Electricista"
                }
                R.id.radioFontanero -> {
                    fotoSeleccionadaId = R.drawable.fontanero
                    oficioSeleccionado = "Fontanero"
                }
            }
            imagenFotoPreview.setImageResource(fotoSeleccionadaId)
        }

        // Botón para borrar datos recordados en SharedPreferences
        botonOlvidar.setOnClickListener {
            prefs.edit().clear().apply()
            editNombre.setText("")
            editApellidos.setText("")
            Toast.makeText(this, "Datos guardados eliminados", Toast.LENGTH_SHORT).show()
        }

        // Botón de registro con validación mediante Toast y envío por Intent
        botonRegistrar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val apellidos = editApellidos.text.toString().trim()

            when {
                nombre.isEmpty() -> {
                    Toast.makeText(this, "Por favor, escribe el nombre del trabajador", Toast.LENGTH_SHORT).show()
                }
                apellidos.isEmpty() -> {
                    Toast.makeText(this, "Por favor, escribe los apellidos del trabajador", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Guardamos en SharedPreferences para recordar al volver a abrir
                    prefs.edit()
                        .putString("NOMBRE_GUARDADO", nombre)
                        .putString("APELLIDOS_GUARDADOS", apellidos)
                        .apply()

                    // Insertamos en la tabla correspondiente según el oficio seleccionado
                    lifecycleScope.launch {
                        val db = AppDatabase.obtener(this@MainActivity)
                        when (oficioSeleccionado) {
                            "Albañil" -> db.albanilDao().insertar(Albanil(nombre = nombre, apellidos = apellidos))
                            "Electricista" -> db.electricistaDao().insertar(Electricista(nombre = nombre, apellidos = apellidos))
                            "Fontanero" -> db.fontaneroDao().insertar(Fontanero(nombre = nombre, apellidos = apellidos))
                        }
                    }

                    // Navegamos a SecondActivity mandando los datos a través del Intent
                    val intent = Intent(this, SecondActivity::class.java).apply {
                        putExtra("NOMBRE_EXTRA", nombre)
                        putExtra("APELLIDOS_EXTRA", apellidos)
                        putExtra("OFICIO_EXTRA", oficioSeleccionado)
                        putExtra("FOTO_EXTRA", fotoSeleccionadaId)
                    }
                    startActivity(intent)
                }
            }
        }

        // Navegación directa a la lista completa
        botonVerHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }
    }
}
