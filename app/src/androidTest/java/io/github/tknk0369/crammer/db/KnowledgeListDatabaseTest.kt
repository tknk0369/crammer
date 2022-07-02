package io.github.tknk0369.crammer.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.tknk0369.crammer.data.db.dao.KnowledgeListDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
class KnowledgeListDatabaseTest {
    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    lateinit var knowledgeListDao: KnowledgeListDao

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
    fun insertKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListDao.insert(ex)
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf(ex))
    }

    @Test
    fun deleteKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("d", "e", "f")
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListDao.insert(ex)
        knowledgeListDao.insert(ex2)
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf(ex, ex2))
        knowledgeListDao.delete(ex2)
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf(ex))
    }

    @Test
    fun updateKnowledgeList() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("a", "e", "f")
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf<KnowledgeListEntity>())
        knowledgeListDao.insert(ex)
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf(ex))
        knowledgeListDao.update(ex2)
        assertThat(knowledgeListDao.selectAll()).isEqualTo(listOf(ex2))
    }

    @Test
    fun selectFromId() = runTest {
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("d", "e", "f")
        assertThat(knowledgeListDao.selectFromId("a")).isNull()
        assertThat(knowledgeListDao.selectFromId("d")).isNull()
        knowledgeListDao.insert(ex)
        knowledgeListDao.insert(ex2)
        assertThat(knowledgeListDao.selectFromId("a")).isEqualTo(ex)
        assertThat(knowledgeListDao.selectFromId("d")).isEqualTo(ex2)
    }

    @Test
    fun selectAllFlow() = runTest {
        val list = mutableListOf<List<KnowledgeListEntity>>()
        val ex = KnowledgeListEntity("a", "b", "c")
        val ex2 = KnowledgeListEntity("a", "d", "c")
        knowledgeListDao.selectAllFlow().take(4).collectIndexed { index, value ->
            list.add(value)
            when (index) {
                0 -> knowledgeListDao.insert(ex)
                1 -> knowledgeListDao.update(ex2)
                2 -> knowledgeListDao.delete(ex2)
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