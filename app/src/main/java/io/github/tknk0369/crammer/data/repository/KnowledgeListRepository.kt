package io.github.tknk0369.crammer.data.repository

import io.github.tknk0369.crammer.data.db.dao.KnowledgeListDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface KnowledgeListRepository {
    fun getKnowledgeList(): List<KnowledgeListEntity>

    suspend fun addKnowledgeList(knowledgeList: KnowledgeListEntity)

    suspend fun updateKnowledgeList(knowledgeList: KnowledgeListEntity)

    suspend fun deleteKnowledgeList(knowledgeList: KnowledgeListEntity)
}

class KnowledgeListRepositoryImpl @Inject constructor() : KnowledgeListRepository {
    @Inject
    lateinit var knowledgeListDao: KnowledgeListDao

    override fun getKnowledgeList(): List<KnowledgeListEntity> = knowledgeListDao.selectAll()

    override suspend fun addKnowledgeList(knowledgeList: KnowledgeListEntity){
        withContext(Dispatchers.IO) {
            knowledgeListDao.insert(knowledgeList)
        }
    }

    override suspend fun updateKnowledgeList(knowledgeList: KnowledgeListEntity) {
        withContext(Dispatchers.IO) {
            knowledgeListDao.update(knowledgeList)
        }
    }

    override suspend fun deleteKnowledgeList(knowledgeList: KnowledgeListEntity) {
        withContext(Dispatchers.IO) {
            knowledgeListDao.delete(knowledgeList)
        }
    }
}