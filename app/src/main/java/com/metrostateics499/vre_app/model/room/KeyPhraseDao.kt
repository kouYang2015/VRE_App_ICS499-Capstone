package com.metrostateics499.vre_app.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyPhraseDao {

    @Query("SELECT * FROM keyphrase_table ORDER BY keyPhrase ASC")
    fun getAlphabetizedWords(): Flow<List<KeyPhrase>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(keyPhrase: KeyPhrase, inUse: Boolean)

    @Query("DELETE FROM keyphrase_table")
    suspend fun deleteAll()
}