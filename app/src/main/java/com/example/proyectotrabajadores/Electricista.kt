package com.example.proyectotrabajadores

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electricistas")
data class Electricista(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellidos: String,
    val oficio: String = "Electricista"
)
