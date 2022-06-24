package io.github.tknk0369.crammer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.tknk0369.crammer.data.db.dao.KnowledgeListDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity

@Database(entities = [KnowledgeListEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun knowledgeListDao(): KnowledgeListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}