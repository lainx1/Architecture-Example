package com.soft.lain.architectureexample.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val description: String,
        @ColumnInfo(name = "priority")
        val priority: Int){

}