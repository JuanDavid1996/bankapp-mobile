package com.example.bankapp.utils

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class Shared {
    companion object {
        fun hasTextInputLayoutText(expectedErrorText: String): Matcher<View> {
            return object : TypeSafeMatcher<View>() {

                override fun matchesSafely(view: View): Boolean {
                    if (view !is TextInputLayout) return false
                    val error = view.error ?: return false
                    val hint = error.toString()
                    return expectedErrorText == hint
                }

                override fun describeTo(description: Description) {

                }
            }
        }

        fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> {
            return object : TypeSafeMatcher<View>() {

                override fun matchesSafely(view: View): Boolean {
                    if (view !is TextInputLayout) return false
                    val error = view.hint ?: return false
                    val hint = error.toString()
                    return expectedErrorText == hint
                }

                override fun describeTo(description: Description) {

                }
            }
        }
    }
}