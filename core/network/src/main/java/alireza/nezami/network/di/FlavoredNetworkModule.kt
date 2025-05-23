package alireza.nezami.network.di

import alireza.nezami.network.data_source.VideoDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun VideoDataSource.binds(): VideoDataSource
}
