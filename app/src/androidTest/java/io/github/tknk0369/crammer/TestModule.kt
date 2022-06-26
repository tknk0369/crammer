package io.github.tknk0369.crammer

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.tknk0369.crammer.data.db.AppDatabase
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepositoryImpl
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

    @Singleton
    @Provides
    fun provideKnowledgeListDao(
        db: AppDatabase
    ) = db.knowledgeListDao()

    @Singleton
    @Provides
    fun provideKnowledgeDao(
        db: AppDatabase
    ) = db.knowledgeDao()

    @Singleton
    @Provides
    fun provideKnowledgeListRepository(
        knowledgeListRepositoryImpl: KnowledgeListRepositoryImpl
    ): KnowledgeListRepository = knowledgeListRepositoryImpl
}