package com.example.trabalhovenda.database

import android.content.Context
import androidx.room.Room
import com.example.trabalhovenda.database.AppDatabase

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cadastro_alunos_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}