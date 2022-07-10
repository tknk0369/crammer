package io.github.tknk0369.crammer.ui.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import io.github.tknk0369.crammer.data.repository.KnowledgeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val knowledgeListRepository: KnowledgeListRepository,
    private val knowledgeRepository: KnowledgeRepository,
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

    fun saveKnowledgeAsCSV(listId: String, uri: Uri?, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    uri?.let {
                        val outputStream = context.contentResolver.openOutputStream(uri)
                        val outputStreamWriter = OutputStreamWriter(outputStream)
                        outputStreamWriter.write(
                            knowledgeRepository.getKnowledgeFromListId(listId)
                                .getOrThrow()
                                .map { "${it.question}, ${it.answer}\n" }
                                .reduce { acc, s -> acc + s }
                        )
                        outputStreamWriter.close()
                        outputStream?.close()
                    }
                }
            }
        }
    }

    fun importKnowledgeFromCSV(uri: Uri?, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    uri?.let {
                        val listUuid = UUID.randomUUID().toString()
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val inputStreamReader = InputStreamReader(inputStream)
                        inputStreamReader
                            .readText()
                            .split("\n")
                            .map { it.split(",").map { comma -> comma.trim() } }
                            .forEach {
                                val uuid = UUID.randomUUID().toString()
                                try {
                                    knowledgeRepository.addKnowledge(
                                        KnowledgeEntity(uuid, listUuid, it[0], it[1])
                                    )
                                } catch (e: Exception) {}
                            }
                        knowledgeListRepository.addKnowledgeList(
                            KnowledgeListEntity(listUuid, listUuid, "root")
                        )
                        inputStreamReader.close()
                        inputStream?.close()
                    }
                }
            }
        }
    }
}