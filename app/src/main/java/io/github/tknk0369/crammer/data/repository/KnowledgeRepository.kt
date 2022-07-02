package io.github.tknk0369.crammer.data.repository

import io.github.tknk0369.crammer.data.db.dao.KnowledgeDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface KnowledgeRepository {
    fun getKnowledge(): List<KnowledgeEntity>

    suspend fun addKnowledge(knowledge: KnowledgeEntity)

    suspend fun updateKnowledge(knowledge: KnowledgeEntity)

    suspend fun deleteKnowledge(knowledge: KnowledgeEntity)

    suspend fun getKnowledgeFromListIdFlow(listId: String): Flow<List<KnowledgeEntity>>
}

class KnowledgeRepositoryImpl @Inject constructor() : KnowledgeRepository {
    @Inject
    lateinit var knowledgeDao: KnowledgeDao

    override fun getKnowledge(): List<KnowledgeEntity> = knowledgeDao.selectAll()

    override suspend fun addKnowledge(knowledge: KnowledgeEntity) {
        withContext(Dispatchers.IO) {
            knowledgeDao.insert(knowledge)
        }
    }

    override suspend fun updateKnowledge(knowledge: KnowledgeEntity) {
        withContext(Dispatchers.IO) {
            knowledgeDao.update(knowledge)
        }
    }

    override suspend fun deleteKnowledge(knowledge: KnowledgeEntity) {
        withContext(Dispatchers.IO) {
            knowledgeDao.delete(knowledge)
        }
    }

    override suspend fun getKnowledgeFromListIdFlow(listId: String): Flow<List<KnowledgeEntity>> {
        return knowledgeDao.selectFromListIdFlow(listId)
    }
}