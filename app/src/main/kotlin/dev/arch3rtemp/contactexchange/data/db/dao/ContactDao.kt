package dev.arch3rtemp.contactexchange.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.arch3rtemp.contactexchange.data.db.model.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact_table WHERE is_my == 1 ORDER BY name ASC")
    fun selectMyContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact_table WHERE is_my == 0 ORDER BY name ASC")
    fun selectScannedContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact_table WHERE id == :id")
    suspend fun selectContactById(id: Int): ContactEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contactEntity: ContactEntity)

    @Update
    suspend fun update(contactEntity: ContactEntity)

    @Query("DELETE FROM contact_table WHERE id == :id")
    suspend fun delete(id: Int)
}
