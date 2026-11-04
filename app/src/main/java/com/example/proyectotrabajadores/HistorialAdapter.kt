package com.example.proyectotrabajadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter para gestionar la lista de trabajadores en el RecyclerView.
 */
class HistorialAdapter(
    private val trabajadores: List<Trabajador>,
    private val onItemClick: ((Trabajador) -> Unit)? = null,
    private val onItemLongClick: ((Trabajador) -> Unit)? = null
) : RecyclerView.Adapter<HistorialAdapter.TrabajadorViewHolder>() {

    class TrabajadorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagenFoto: ImageView = view.findViewById(R.id.imagenFotoFila)
        val textoNombreCompleto: TextView = view.findViewById(R.id.textoNombreCompletoFila)
        val textoOficio: TextView = view.findViewById(R.id.textoOficioFila)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrabajadorViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_trabajador, parent, false)
        return TrabajadorViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TrabajadorViewHolder, position: Int) {
        val trabajador = trabajadores[position]
        holder.textoNombreCompleto.text = "${trabajador.nombre} ${trabajador.apellidos}"
        holder.textoOficio.text = "Puesto: ${trabajador.oficio}"

        // Asignamos la imagen dinámicamente según el oficio guardado
        val fotoResId = when (trabajador.oficio) {
            "Albañil" -> R.drawable.albanil
            "Electricista" -> R.drawable.electricista
            "Fontanero" -> R.drawable.fontanero
            else -> R.drawable.albanil
        }
        holder.imagenFoto.setImageResource(fotoResId)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(trabajador)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(trabajador)
            true
        }
    }

    override fun getItemCount(): Int = trabajadores.size
}
