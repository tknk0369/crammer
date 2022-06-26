package io.github.tknk0369.crammer.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.tknk0369.crammer.data.db.dao.KnowledgeDao
import io.github.tknk0369.crammer.data.db.entity.KnowledgeEntity
import io.github.tknk0369.crammer.data.db.entity.KnowledgeListEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    }

    @Test
    fun insertKnowledge() = runBlockingTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf<KnowledgeEntity>())
        knowledgeDao.insert(ex)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex))
    }

    @Test
    fun deleteKnowledge() = runBlockingTest {
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
    fun updateKnowledge() = runBlockingTest {
        val ex = KnowledgeEntity("a", "b", "c", "d")
        val ex2 = KnowledgeEntity("a", "f", "g", "h")
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf<KnowledgeEntity>())
        knowledgeDao.insert(ex)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex))
        knowledgeDao.update(ex2)
        assertThat(knowledgeDao.selectAll()).isEqualTo(listOf(ex2))
    }
}