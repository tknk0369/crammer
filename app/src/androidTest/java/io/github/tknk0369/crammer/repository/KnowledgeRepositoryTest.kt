package io.github.tknk0369.crammer.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import io.github.tknk0369.crammer.data.repository.KnowledgeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class KnowledgeRepositoryTest {
    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    lateinit var knowledgeRepository: KnowledgeRepository

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
    fun addKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        assertThat(knowledgeRepository.getKnowledges())
            .isEqualTo(listOf<KnowledgeEntity>())
        knowledgeRepository.addKnowledge(ex)
        assertThat(knowledgeRepository.getKnowledges()).isEqualTo(listOf(ex))
    }

    @Test
    fun deleteKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        val ex2 = KnowledgeEntity("e", "f", "g", "h")
        assertThat(knowledgeRepository.getKnowledges())
            .isEqualTo(listOf<KnowledgeEntity>())
        knowledgeRepository.addKnowledge(ex)
        knowledgeRepository.addKnowledge(ex2)
        assertThat(knowledgeRepository.getKnowledges()).isEqualTo(listOf(ex, ex2))
        knowledgeRepository.deleteKnowledge(ex2)
        assertThat(knowledgeRepository.getKnowledges()).isEqualTo(listOf(ex))
    }

    @Test
    fun updateKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        val ex2 = KnowledgeEntity("a", "f", "g", "h")
        assertThat(knowledgeRepository.getKnowledges())
            .isEqualTo(listOf<KnowledgeEntity>())
        knowledgeRepository.addKnowledge(ex)
        assertThat(knowledgeRepository.getKnowledges()).isEqualTo(listOf(ex))
        knowledgeRepository.updateKnowledge(ex2)
        assertThat(knowledgeRepository.getKnowledges()).isEqualTo(listOf(ex2))
    }
}