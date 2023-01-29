package com.example.myapplication.di

import com.example.myapplication.util.LocalHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalHelperModule {

    @Provides
    @Singleton
    fun provideLocalHelper(): LocalHelper {
        return LocalHelper(MyApplication.getApplicationContext())
    }

}