package com.weberpackage.blackjack.common.presentation.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ThousandsSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formatted = try {
            val number = originalText.toLong()
            java.text.NumberFormat.getNumberInstance(java.util.Locale.US).format(number)
        } catch (e: Exception) {
            originalText
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val textBeforeCursor = originalText.substring(0, offset)
                val formattedBeforeCursor = try {
                    val number = textBeforeCursor.toLong()
                    java.text.NumberFormat.getNumberInstance(java.util.Locale.US).format(number)
                } catch (e: Exception) {
                    textBeforeCursor
                }
                return formattedBeforeCursor.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val transformedBeforeCursor = formatted.substring(0, offset.coerceAtMost(formatted.length))
                return transformedBeforeCursor.count { it.isDigit() }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}