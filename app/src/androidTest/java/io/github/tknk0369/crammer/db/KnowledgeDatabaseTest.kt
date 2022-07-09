package io.github.tknk0369.crammer.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.tknk0369.crammer.data.db.dao.KnowledgeDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
class KnowledgeDatabaseTest {
    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    lateinit var knowledgeDao: KnowledgeDao

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
    fun insertKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf<KnowledgeEntity>())
        knowledgeDao.insert(ex)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex))
    }

    @Test
    fun deleteKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        val ex2 = KnowledgeEntity("e", "f", "g", "h")
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf<KnowledgeEntity>())
        knowledgeDao.insert(ex)
        knowledgeDao.insert(ex2)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex, ex2))
        knowledgeDao.delete(ex2)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex))
    }

    @Test
    fun updateKnowledge() = runTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        val ex2 = KnowledgeEntity("a", "f", "g", "h")
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf<KnowledgeEntity>())
        knowledgeDao.insert(ex)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex))
        knowledgeDao.update(ex2)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex2))
    }

    @Test
    fun selectFromListIdFlow() = runTest {
        val list = mutableListOf<List<KnowledgeEntity>>()
        val ex = KnowledgeEntity("a", "id", "c", "d")
        val ex2 = KnowledgeEntity("a", "id", "g", "h")
        knowledgeDao.selectFromListIdFlow("id").take(4).collectIndexed { index, value ->
            list.add(value)
            when (index) {
                0 -> knowledgeDao.insert(ex)
                1 -> knowledgeDao.update(ex2)
                2 -> knowledgeDao.delete(ex2)
            }
        }
        assertThat(list).containsExactly(
            listOf<KnowledgeEntity>(),
            listOf(ex),
            listOf(ex2),
            listOf<KnowledgeEntity>(),
        )
    }

    @Test
    fun selectFromListId() = runTest {
        val ex = KnowledgeEntity("a", "id", "c", "d")
        val ex2 = KnowledgeEntity("b", "id", "g", "h")
        val ex3 = KnowledgeEntity("c", "id2", "g", "h")
        knowledgeDao.insert(ex)
        knowledgeDao.insert(ex2)
        knowledgeDao.insert(ex3)
        assertThat(knowledgeDao.selectFromListId("id")).containsExactly(ex, ex2)
        assertThat(knowledgeDao.selectFromListId("id2")).containsExactly(ex3)
        assertThat(knowledgeDao.selectAll()).containsExactly(ex, ex2, ex3)
    }
}