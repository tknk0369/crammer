package io.github.tknk0369.crammer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val knowledgeListRepository: KnowledgeListRepository
) : ViewModel() {
    val knowledgeList = knowledgeListRepository.getKnowledgeListsRealTime

    fun addNewKnowledgeList(name: String) {
        viewModelScope.launch {
            knowledgeListRepository.addKnowledgeList(
                KnowledgeListEntity(UUID.randomUUID().toString(), name, "root")
            )
        }
    }

    fun renameKnowledgeList(knowledgeList: KnowledgeListEntity, name: String) {
        viewModelScope.launch {
            knowledgeListRepository.updateKnowledgeList(knowledgeList.copy(name = name))
        }
    }

    fun deleteKnowledgeList(knowledgeList: KnowledgeListEntity) {
        viewModelScope.launch {
            knowledgeListRepository.deleteKnowledgeList(knowledgeList)
        }
    }
}