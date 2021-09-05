package com.example.bankapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.bankapp.presentation.session.SignInActivity
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.bankapp.utils.Shared.Companion.hasTextInputLayoutText

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SigInViewTest {

    @get:Rule
    var singInActivity: IntentsTestRule<SignInActivity> =
        IntentsTestRule(SignInActivity::class.java)

    private var repository: SessionRepository = SessionRepository()

    @Before
    fun cleanSession() {
        runBlocking {
            repository.logOut()
        }
    }

    @Test
    fun signInShouldBeSuccess() {
        onView(withId(R.id.username)).perform(typeText("jhondoe"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("Test123?"), closeSoftKeyboard())
        onView(withId(R.id.signIn)).perform(click())
    }

    @Test
    fun signInFailInvalidUsername() {
        onView(withId(R.id.signIn)).perform(click())
        onView(withId(R.id.layoutUsername))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un nombre de usuario válido")))
    }

    @Test
    fun signInFailInvalidPassword() {
        onView(withId(R.id.signIn)).perform(click())
        onView(withId(R.id.layoutPassword))
            .check(matches(hasTextInputLayoutText("Por favor ingresa la contraseña")))
    }

    @Test
    fun signInFailInvalidUserNamePassword() {
        onView(withId(R.id.signIn)).perform(click())
        onView(withId(R.id.layoutUsername))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un nombre de usuario válido")))
        onView(withId(R.id.layoutPassword))
            .check(matches(hasTextInputLayoutText("Por favor ingresa la contraseña")))
    }

}