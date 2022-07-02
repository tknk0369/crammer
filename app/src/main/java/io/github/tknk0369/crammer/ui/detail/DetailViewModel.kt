package io.github.tknk0369.crammer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import io.github.tknk0369.crammer.data.repository.KnowledgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val knowledgeListRepository: KnowledgeListRepository,
    private val knowledgeRepository: KnowledgeRepository,
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    private val _knowledge = MutableStateFlow(listOf<KnowledgeEntity>())
    val knowledge = _knowledge.asStateFlow()

    fun init(id: String?) {
        viewModelScope.launch {
            _name.value = knowledgeListRepository.getKnowledgeListFromId(id).getOrNull()?.name ?: ""
        }
        viewModelScope.launch {
            knowledgeRepository.getKnowledgeFromListIdFlow(id ?: "").collect {
                _knowledge.value = it
            }
        }
    }

    fun addKnowledge(question: String, answer: String, listId: String?) {
        viewModelScope.launch {
            knowledgeRepository.addKnowledge(
                KnowledgeEntity(
                    id = UUID.randomUUID().toString(),
                    listId = listId ?: "",
                    question = question,
                    answer = answer,
                )
            )
        }
    }

    fun updateKnowledge(question: String, answer: String, knowledgeEntity: KnowledgeEntity) {
        viewModelScope.launch {
            knowledgeRepository.updateKnowledge(
                knowledgeEntity.copy(
                    question = question,
                    answer = answer
                )
            )
        }
    }

    fun deleteKnowledge(knowledgeEntity: KnowledgeEntity) {
        viewModelScope.launch {
            knowledgeRepository.deleteKnowledge(knowledgeEntity)
        }
    }
}