package com.weberpackage.blackjack.changelog.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.changelog.data.repo.ChangelogRepo
import com.weberpackage.blackjack.changelog.presentation.contract.ChangelogContract
import com.weberpackage.blackjack.changelog.presentation.model.ChangelogData
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.data.interfaces.Result
import com.weberpackage.blackjack.common.presentation.AsUiText.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangelogViewModel @Inject constructor(
    private val changelogRepo: ChangelogRepo
) : BaseViewModel<ChangelogContract.Event, ChangelogContract.State, ChangelogContract.Effect>() {

    init {
        getChangelogData()
    }

    override fun setInitialState() = ChangelogContract.State(
        changelogData = ChangelogData(),
        isInitialLoading = true,
        isDataError = false
    )

    override fun handleEvents(event: ChangelogContract.Event) {}

    private fun getChangelogData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = changelogRepo.getChangelogData()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {
                        setEffect {
                            ChangelogContract.Effect.Notification(
                                text = result.error.asUiText(),
                                error = true
                            )
                        }
                    }


                    is Result.Success -> {
                        setState {
                            copy(
                                changelogData = result.data,
                                isInitialLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}