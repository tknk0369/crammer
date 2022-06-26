package io.github.tknk0369.crammer

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.tknk0369.crammer.data.db.AppDatabase
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepository
import io.github.tknk0369.crammer.data.repository.KnowledgeListRepositoryImpl
import io.github.tknk0369.crammer.data.repository.KnowledgeRepository
import io.github.tknk0369.crammer.data.repository.KnowledgeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

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

    @Singleton
    @Provides
    fun provideKnowledgeRepository(
        knowledgeRepositoryImpl: KnowledgeRepositoryImpl
    ): KnowledgeRepository = knowledgeRepositoryImpl
}