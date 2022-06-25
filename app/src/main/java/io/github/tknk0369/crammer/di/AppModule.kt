package io.github.tknk0369.crammer.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.tknk0369.crammer.data.db.AppDatabase
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
}