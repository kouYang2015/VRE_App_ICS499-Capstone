package com.metrostateics499.vre_app.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyphrase_table")
class KeyPhrase(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "keyPhrase") val keyPhrase: String,
    @ColumnInfo(name = "in_use") val inUse: Boolean
)