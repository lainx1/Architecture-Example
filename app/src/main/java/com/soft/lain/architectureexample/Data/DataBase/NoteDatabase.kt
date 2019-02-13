package com.soft.lain.architectureexample.Data.DataBase

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.soft.lain.architectureexample.Data.Dao.NoteDao
import com.soft.lain.architectureexample.Model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase constructor(): RoomDatabase() {
    companion object {
        @Volatile private var instance: NoteDatabase? = null
        val roomCallback = object: RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance!!).execute()
            }
        }

        fun getInstance(context: Context): NoteDatabase {
            instance = if (instance == null)
                Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java,
                        "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
            else
                instance

            return instance!!
        }
    }

    abstract fun noteDao(): NoteDao

    class PopulateDbAsyncTask(db: NoteDatabase): AsyncTask<Unit, Unit, Unit>() {
        var noteDao: NoteDao?= null
        init {
            this.noteDao = db.noteDao()
        }
        override fun doInBackground(vararg params: Unit?): Unit {
            noteDao!!.insert(Note(0, "Title 1", "Description 1", 1))
            noteDao!!.insert(Note(0, "Title 2", "Description 2", 2))
            noteDao!!.insert(Note(0, "Title 3", "Description 3", 3))
            return
        }
    }
}