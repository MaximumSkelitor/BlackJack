package com.weberpackage.blackjack.changelog.data.repo

import android.content.Context
import com.weberpackage.blackjack.changelog.data.model.ChangelogDto
import com.weberpackage.blackjack.changelog.domain.ChangelogDtoToDataMapper
import com.weberpackage.blackjack.changelog.presentation.model.ChangelogData
import com.weberpackage.blackjack.common.data.interfaces.DataError
import com.weberpackage.blackjack.common.data.interfaces.Result
import com.weberpackage.blackjack.common.data.utils.readJsonFromAssets
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class ChangelogRepoImpl @Inject constructor(
    private val json: Json,
    @ApplicationContext private val context: Context
) : ChangelogRepo {

    override suspend fun getChangelogData(): Result<ChangelogData, DataError> {
        try {
            val jsonString = readJsonFromAssets("changelog.json", context = context)
            val changelogDto = json.decodeFromString<ChangelogDto>(jsonString)
            val changelogData = ChangelogDtoToDataMapper.map(changelogDto)

            return Result.Success(changelogData)

        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Timber.e(e, "Changelog JSON Error")
        }
        return Result.Error(DataError.Local.JSON_ERROR)
    }

}