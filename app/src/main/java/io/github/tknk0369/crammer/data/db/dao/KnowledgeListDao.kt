package io.github.tknk0369.crammer.data.db.dao

import androidx.room.*
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity

@Dao
interface KnowledgeListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(knowledgeList: KnowledgeListEntity)

    @Update
    fun update(knowledgeList: KnowledgeListEntity)

    @Delete
    fun delete(knowledgeList: KnowledgeListEntity)

    @Query("select * from knowledge_list")
    fun selectAll(): List<KnowledgeListEntity>
}