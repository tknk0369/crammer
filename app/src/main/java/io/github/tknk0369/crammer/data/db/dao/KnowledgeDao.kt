package io.github.tknk0369.crammer.data.db.dao

import androidx.room.*
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(knowledge: KnowledgeEntity)

    @Update
    fun update(knowledge: KnowledgeEntity)

    @Delete
    fun delete(knowledge: KnowledgeEntity)

    @Query("select * from knowledge")
    fun selectAll(): List<KnowledgeEntity>

    @Query("select * from knowledge where listId = :listId")
    fun selectFromListIdFlow(listId: String): Flow<List<KnowledgeEntity>>

    @Query("select * from knowledge where listId = :listId")
    fun selectFromListId(listId: String): List<KnowledgeEntity>
}