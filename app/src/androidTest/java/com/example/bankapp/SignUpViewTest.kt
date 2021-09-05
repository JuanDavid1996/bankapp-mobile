package com.example.bankapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.bankapp.presentation.session.SignUpActivity
import com.example.bankapp.repository.session.SessionRepository
import com.example.bankapp.utils.Shared.Companion.hasTextInputLayoutText
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SignUpViewTest {

    @get:Rule
    var singUpActivity: IntentsTestRule<SignUpActivity> =
        IntentsTestRule(SignUpActivity::class.java)

    private var repository: SessionRepository = SessionRepository()


    @Before
    fun cleanSession() {
        runBlocking {
            repository.logOut()
        }
    }

    @Test
    fun signUpShouldBeSuccess() {
        onView(withId(R.id.firstName)).perform(typeText("Jhon"), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(typeText("Doe"), closeSoftKeyboard())
        onView(withId(R.id.identification)).perform(typeText("100568745"), closeSoftKeyboard())
        onView(withId(R.id.username)).perform(
            typeText("jhondoe" + Date().time),
            closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(typeText("Test123?"), closeSoftKeyboard())
        onView(withId(R.id.passwordConfirmation)).perform(typeText("Test123?"), closeSoftKeyboard())
        onView(withId(R.id.signUp)).perform(click())
    }

    @Test
    fun signUpFailInvalidUsername() {
        onView(withId(R.id.signUp)).perform(click())
        onView(withId(R.id.layoutUsername))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un nombre de usuario válido")))
    }

    @Test
    fun signUpFailInvalidPassword() {
        onView(withId(R.id.signUp)).perform(click())
        onView(withId(R.id.layoutPassword))
            .check(matches(hasTextInputLayoutText("Por favor ingresa la contraseña")))
    }

    @Test
    fun signUpFail() {
        onView(withId(R.id.signUp)).perform(click())
        onView(withId(R.id.layoutUsername))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un nombre de usuario válido")))
        onView(withId(R.id.layoutPassword))
            .check(matches(hasTextInputLayoutText("Por favor ingresa la contraseña")))
        onView(withId(R.id.identificationLayout))
            .check(matches(hasTextInputLayoutText("Por favor ingresa una identicicación válida")))
        onView(withId(R.id.firstNameLayout))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un nombre válido")))
        onView(withId(R.id.lastNameLayout))
            .check(matches(hasTextInputLayoutText("Por favor ingresa un apellido válido")))
    }

}