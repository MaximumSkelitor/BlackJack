package com.weberpackage.blackjack.common.data.repo

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.weberpackage.blackjack.core.utils.AppUpdateConfigData
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.data.utils.readJsonFromAssets
import com.weberpackage.blackjack.common.presentation.utils.DialogAction
import com.weberpackage.blackjack.common.presentation.utils.DialogController
import com.weberpackage.blackjack.common.presentation.utils.DialogEvent
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.core.constants.AppConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds

class RemoteConfigRepositoryImpl @Inject constructor(
    @param:ApplicationContext val appContext: Context,
    private val remoteConfig: FirebaseRemoteConfig,
    private val json: Json
) : RemoteConfigRepository {

    override suspend fun fetchAppUpdateConfig(): AppUpdateConfigData? {
        val key = AppConfig.APP_UPDATE_CONFIG
        val defaults = mapOf(
            key to readJsonFromAssets("config_app_update.json", context = appContext)
        )
        remoteConfig.setDefaultsAsync(defaults).await()

        return try {
            withRetryBackoff {
                remoteConfig.fetchAndActivate().await().also {
                    if (!it) Timber.d("App Update Using Local Configs")
                }

                remoteConfig.fetchConfigJson<List<AppUpdateConfigData>>(key, json)
                    .filter {
                        it.appVersionCode > BuildConfig.VERSION_CODE &&
                                (BuildConfig.ALPHA_BUILD || !it.isAlpha)
                    }
                    .maxByOrNull { it.appVersionCode }
            }
        } catch (e: Exception) {
            Timber.e(e, "Firebase App Update fetch exception, fallback to defaults")
            showFirebaseErrorDialog()
            remoteConfig.fetchConfigJson<List<AppUpdateConfigData>>(key, json)
                .filter {
                    it.appVersionCode > BuildConfig.VERSION_CODE &&
                            (BuildConfig.ALPHA_BUILD || !it.isAlpha)
                }
                .maxByOrNull { it.appVersionCode }
        }
    }

    private suspend fun showFirebaseErrorDialog() {
        DialogController.sendEvent(
            event = DialogEvent(
                title = UiText(R.string.dialog_firebase_failed_title),
                message = UiText(R.string.dialog_firebase_failed_message),
                dismissible = false,
                positiveAction = DialogAction(
                    buttonText = UiText(R.string.close)
                )
            )
        )
    }

    private inline fun <reified T> FirebaseRemoteConfig.fetchConfigJson(
        key: String,
        json: Json
    ): T {
        val jsonString = getString(key)
        return json.decodeFromString(jsonString)
    }

    private suspend fun <T> withRetryBackoff(
        maxRetries: Int = 3,
        baseDelayMillis: Long = 1000,
        block: suspend () -> T
    ): T {
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()

                if (attempt == maxRetries - 1) {
                    throw e // last attempt, rethrow
                }

                val backoff = baseDelayMillis * (2.0.pow(attempt.toDouble())).toLong()
                Timber.w(e, "Retry attempt ${attempt + 1} failed, retrying in $backoff ms")
                delay(backoff.milliseconds)
            }
        }
        error("Unreachable") // compiler safety
    }
}