package io.github.tknk0369.crammer.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "knowledge_list")
data class KnowledgeListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val groupId: String,
)
