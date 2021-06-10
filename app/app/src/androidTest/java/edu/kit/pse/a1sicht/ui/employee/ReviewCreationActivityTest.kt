package edu.kit.pse.a1sicht.ui.employee

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import androidx.test.espresso.intent.Intents
import org.hamcrest.Matchers


/**
 *Class for Testing ReviewCreationActivity
 *
 *@author Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class ReviewCreationActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(ReviewCreationActivity::class.java)


    /**
     *    Tests for the save Button.
     * */
    @Test
    fun saveButtonText() {
        onView(withId(R.id.save_btn)).check(matches(withText(R.string.save)))
    }

    /**
     * Tests if the SaveButton is clickable.
     */
    @Test
    fun saveButtonIsClickable() {
        onView(withId(R.id.save_btn)).check(matches((isClickable())))
    }

    /**
     * Tests if the SaveButton is enabled.
     */
    @Test
    fun saveButtonIsEnabled() {
        onView(withId(R.id.save_btn)).check(matches(isEnabled()))
    }

    /**
     * Tests if the SaveButton is displayed.
     */
    @Test
    fun saveButtonIsDisplayed() {
        onView(withId(R.id.save_btn)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the SaveButton is completly displayed.
     */
    @Test
    fun usersButtonIsCompletelyDisplayed() {
        onView(withId(R.id.save_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     *    Tests for the cancel Button.
     * */
    @Test
    fun cancelButtonText() {
        onView(withId(R.id.cancel_btn)).check(matches(withText(R.string.cancel)))
    }

    /**
     * Tests if the CancelButton is clickable.
     */
    @Test
    fun cancelButtonIsClickable() {
        onView(withId(R.id.cancel_btn)).check(matches((isClickable())))
    }

    /**
     * Tests if the CancelButton is enabled.
     */
    @Test
    fun cancelButtonIsEnabled() {
        onView(withId(R.id.cancel_btn)).check(matches(isEnabled()))
    }

    /**
     * Tests if the CancelButton is displayed.
     */
    @Test
    fun cancelButtonIsDisplayed() {
        onView(withId(R.id.cancel_btn)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the CancelButton is completly displayed.
     */
    @Test
    fun cancelButtonIsCompletelyDisplayed() {
        onView(withId(R.id.cancel_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests the if the TimeslotSpinner works
     * @throws Exception on Error
     */
    @Test
    @Throws(Exception::class)
    fun testTimeSlotSpinner() {
        Intents.init()

        onView(withId(R.id.timeslotSpinner))
            .perform(click());
        Espresso.onData(Matchers.hasToString(Matchers.startsWith("3")))
            .perform(click());
        onView(withId(R.id.timeslotSpinner))
            .check(matches(withSpinnerText(Matchers.containsString("3"))));

        Intents.release()
    }

    /**
     * Checks if the TextFields work.
     * @throws Exception on Error
     */
    @Test
    @Throws(Exception::class)
    fun testTextFields() {
        Intents.init()

        onView(withId(R.id.btn_create_review)).perform(click())
        onView(withId(R.id.nameET))
            .perform(ViewActions.typeText("name"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.placeET))
            .perform(ViewActions.typeText("place"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.timeslotNumET))
            .perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.maxStudentsPerTimeslotET))
            .perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.moreInfoET))
            .perform(ViewActions.typeText("info"), ViewActions.closeSoftKeyboard())


        onView(withId(R.id.nameET))
            .check(matches(withText(Matchers.containsString("name"))))
        onView(withId(R.id.placeET))
            .check(matches(withText(Matchers.containsString("place"))))
        onView(withId(R.id.timeslotNumET))
            .check(matches(withText(Matchers.containsString("1"))))
        onView(withId(R.id.maxStudentsPerTimeslotET))
            .check(matches(withText(Matchers.containsString("1"))))
        onView(withId(R.id.moreInfoET))
            .check(matches(withText(Matchers.containsString("info"))))

        Intents.release()
    }
}