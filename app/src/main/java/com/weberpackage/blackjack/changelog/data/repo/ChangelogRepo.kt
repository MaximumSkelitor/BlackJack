package com.weberpackage.blackjack.changelog.data.repo

import com.weberpackage.blackjack.changelog.presentation.model.ChangelogData
import com.weberpackage.blackjack.common.data.interfaces.DataError
import com.weberpackage.blackjack.common.data.interfaces.Result

interface ChangelogRepo {
    suspend fun getChangelogData(): Result<ChangelogData, DataError>
}