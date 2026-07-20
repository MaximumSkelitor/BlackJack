package com.weberpackage.blackjack.core.di

import com.weberpackage.blackjack.changelog.data.repo.ChangelogRepo
import com.weberpackage.blackjack.changelog.data.repo.ChangelogRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindChangelogRepo(
        changelogRepoImpl: ChangelogRepoImpl
    ): ChangelogRepo
}
