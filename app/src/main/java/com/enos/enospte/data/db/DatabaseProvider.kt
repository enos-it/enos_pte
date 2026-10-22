package com.enos.enospte.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "enos_pte.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build().also { INSTANCE = it }
        }
    }
}