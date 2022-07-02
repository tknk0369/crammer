package io.github.tknk0369.crammer.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
class KnowledgeListRepositoryTest {
    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    lateinit var knowledgeListRepository: KnowledgeListRepository

    @Before
    fun setup() {
        rule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        assertThat(knowledgeListRepository.getKnowledgeLists())
            .isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListRepository.addKnowledgeList(ex)
        assertThat(knowledgeListRepository.getKnowledgeLists()).isEqualTo(listOf(ex))
    }

    @Test
    fun deleteKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("d", "e", "f")
        assertThat(knowledgeListRepository.getKnowledgeLists())
            .isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListRepository.addKnowledgeList(ex)
        knowledgeListRepository.addKnowledgeList(ex2)
        assertThat(knowledgeListRepository.getKnowledgeLists()).isEqualTo(listOf(ex, ex2))
        knowledgeListRepository.deleteKnowledgeList(ex2)
        assertThat(knowledgeListRepository.getKnowledgeLists()).isEqualTo(listOf(ex))
    }

    @Test
    fun updateKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("a", "e", "f")
        assertThat(knowledgeListRepository.getKnowledgeLists())
            .isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListRepository.addKnowledgeList(ex)
        assertThat(knowledgeListRepository.getKnowledgeLists()).isEqualTo(listOf(ex))
        knowledgeListRepository.updateKnowledgeList(ex2)
        assertThat(knowledgeListRepository.getKnowledgeLists()).isEqualTo(listOf(ex2))
    }

    @Test
    fun getKnowledgeListFromId() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("d", "e", "f")
        assertThat(knowledgeListRepository.getKnowledgeListFromId("a").getOrNull()).isNull()
        assertThat(knowledgeListRepository.getKnowledgeListFromId("a").getOrNull()).isNull()
        knowledgeListRepository.addKnowledgeList(ex)
        knowledgeListRepository.addKnowledgeList(ex2)
        assertThat(knowledgeListRepository.getKnowledgeListFromId("a").getOrNull()).isEqualTo(ex)
        assertThat(knowledgeListRepository.getKnowledgeListFromId("d").getOrNull()).isEqualTo(ex2)
    }

    @Test
    fun getKnowledgeListsRealTime() = runTest {
        val list = mutableListOf<List<KnowledgeListEntity>>()
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("a", "d", "c")
        knowledgeListRepository.getKnowledgeListsRealTime.take(4).collectIndexed { index, value ->
            list.add(value)
            when (index) {
                0 -> knowledgeListRepository.addKnowledgeList(ex)
                1 -> knowledgeListRepository.updateKnowledgeList(ex2)
                2 -> knowledgeListRepository.deleteKnowledgeList(ex2)
            }
        }
        assertThat(list).containsExactly(
            listOf<KnowledgeListEntity>(),
            listOf(KnowledgeListEntity("a", "b", "c")),
            listOf(KnowledgeListEntity("a", "d", "c")),
            listOf<KnowledgeListEntity>()
        )
    }
}