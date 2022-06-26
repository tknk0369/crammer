package io.github.tknk0369.crammer.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "knowledge")
data class KnowledgeEntity(
    @PrimaryKey val id: String,
    val listId: String,
    val question: String,
    val answer: String,
)
