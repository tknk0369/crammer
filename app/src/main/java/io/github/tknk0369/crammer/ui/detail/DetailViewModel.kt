package io.github.tknk0369.crammer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val knowledgeListRepository: KnowledgeListRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun init(id: String?) {
        viewModelScope.launch {
            _name.value = knowledgeListRepository.getKnowledgeListFromId(id).getOrNull()?.name ?: ""
        }
    }
}