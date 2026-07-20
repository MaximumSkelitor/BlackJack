package com.weberpackage.blackjack.core.di

import android.content.IntentFilter
import com.weberpackage.blackjack.core.constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

object ReceiverModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AdminFilter

    @Module
    @InstallIn(SingletonComponent::class)
    object ReceiverModule {

        @AdminFilter
        @Provides
        @Singleton
        fun provideAdminIntentFilter(): IntentFilter {
            return IntentFilter(Constants.ACTION_ADD_CREDITS)

        }
    }
}
