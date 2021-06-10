package edu.kit.pse.a1sicht.ui.employee

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import androidx.test.espresso.intent.Intents


/**
 *Testing class for the ReviewScreenActivity
 *
 *@author Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class ReviewScreenActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(ReviewScreenActivity::class.java)

    /**
     *    Tests for the editButton.
     * */
    @Test
    fun saveButtonText() {
        onView(withId(R.id.edit_btn)).check(matches(withText(R.string.edit)))
    }

    @Test
    fun saveButtonIsClickable() {
        onView(withId(R.id.edit_btn)).check(matches((isClickable())))
    }

    @Test
    fun saveButtonIsEnabled() {
        onView(withId(R.id.edit_btn)).check(matches(isEnabled()))
    }

    @Test
    fun saveButtonIsDisplayed() {
        onView(withId(R.id.edit_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun usersButtonIsCompletelyDisplayed() {
        onView(withId(R.id.edit_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests if the creationButton bring the employee to the ReviewCreationActivity.
     * @throws Exception on error
     */
    @Test
    @Throws(Exception::class)
    fun studentRegister() {
        Intents.init()

        onView(withId(R.id.edit_btn)).perform(click())

   onView(withId(R.id.title)).check(matches(isDisplayed()))
        intended(hasComponent(ReviewCreationActivity::class.java.canonicalName))
        Intents.release()
    }
}