package alireza.nezami.network.di

import alireza.nezami.network.data_source.VideoDataSource
import alireza.nezami.network.data_source.VideoDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FlavoredNetworkModule {

    @Binds
    abstract fun bindVideoDataSource(
            videoDataSourceImpl: VideoDataSourceImpl
    ): VideoDataSource
}
