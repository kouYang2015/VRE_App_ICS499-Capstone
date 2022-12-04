package com.metrostateics499.vre_app.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.Objects

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Objects::class], version = 1, exportSchema = false)
public abstract class VreRoomDatabase : RoomDatabase() {

//    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: VreRoomDatabase? = null

        fun getDatabase(context: Context): VreRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VreRoomDatabase::class.java,
                    "vre_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}