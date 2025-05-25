package alireza.nezami.detail.di

import alireza.nezami.detail.presentation.contract.VideoPlayerManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VideoPlayerModule {
    @Provides
    @Singleton
    fun provideVideoPlayerManager(@ApplicationContext context: Context): VideoPlayerManager {
        return VideoPlayerManager(context)
    }
}