package edu.kit.pse.a1sicht.ui.shared

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 *Testing class for [LogInActivity]
 * @author Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(LogInActivity::class.java)


    /**
     *    Tests the SignInButtons title.
     * */
    @Test
    fun signInButtonText() {
        onView(withId(R.id.signInBtn)).check(matches(withText(R.string.sign_up)))
    }

    /**
     * Tests if the SignInButton is clickable.
     */
    @Test
    fun signInButtonIsClickable() {
        onView(withId(R.id.signInBtn)).check(matches((isClickable())))
    }

    /**
     * Tests if the SignInButton is enabled.
     */
    @Test
    fun signInButtonIsEnabled() {
        onView(withId(R.id.signInBtn)).check(matches(isEnabled()))
    }

    /**
     * Tests if the SignInButton is displayed.
     */
    @Test
    fun signInButtonIsDisplayed() {
        onView(withId(R.id.signInBtn)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the SignInButton is completly displayed.
     */
    @Test
    fun signInButtonIsCompletelyDisplayed() {
        onView(withId(R.id.signInBtn)).check(matches(isCompletelyDisplayed()))
    }
}
