package com.weberpackage.blackjack.core.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.common.data.repo.RemoteConfigRepository
import com.weberpackage.blackjack.common.data.repo.RemoteConfigRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder().apply {
                    setFetchTimeoutInSeconds(2000)
                    setMinimumFetchIntervalInSeconds(
                        if (BuildConfig.DEBUG) 0 else 3600
                    )
                }.build()
            )
        }
    }


    @Provides
    @Singleton
    fun provideRemoteConfigRepository(
        impl: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository = impl
}
