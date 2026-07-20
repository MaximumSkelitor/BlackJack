package com.weberpackage.blackjack.common.presentation

import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.data.interfaces.DataError
import com.weberpackage.blackjack.common.presentation.utils.UiText


@Suppress("unused")
object AsUiText {

    fun DataError.asUiText(): UiText {
        return when (this) {


            DataError.Local.UNKNOWN -> UiText(
                R.string.data_error_unknown
            )

            DataError.Local.JSON_ERROR -> UiText(
                R.string.data_error_json_parsing
            )

            DataError.Network.UNKNOWN -> UiText(
                R.string.data_error_unknown
            )

            is DataError.Server -> UiText(data)

            is DataError.UiText -> UiText(resId, args)

        }
    }
}