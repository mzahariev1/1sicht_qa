package edu.kit.pse.a1sicht.ui.student

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import org.junit.runner.RunWith
import java.lang.Exception
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import kotlinx.android.synthetic.main.activity_employee_review_screen.view.*
import org.hamcrest.Matchers
import org.junit.*


/**
 * Espresso test class for [StudentReviewScreenActivity] class.
 *
 *@author Hristo Klechorov
 */
@RunWith(AndroidJUnit4::class)
class StudentReviewScreenActivityTest{

    var intents: List<Intent>? = null

    @get:Rule
    var activityScenarioRule = ActivityTestRule(StudentReviewScreenActivity::class.java)

    @Before
    fun setUp() {
        val activity = activityScenarioRule.activity

        Intents.init()
        intents = Intents.getIntents()
        var i = 0
        while (i < Intents.getIntents().size) {
            intents!![i].putExtra("caller", "edu.kit.pse.a1sicht.ui.student.HomeStudentActivity")
            intents!![i].putExtra("reviewId", "2")
            i++
        }

        activity.findViewById<TextView>(R.id.title).text = "Title"
        activity.findViewById<TextView>(R.id.moreInfo).text = "Additional Information"
        activity.findViewById<TextView>(R.id.place_txt2).text = "Club33"
        activity.findViewById<TextView>(R.id.tv_showTimeslot).text = "00:00"
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    // Tests for the title

    @Test
    fun titleTVIsDisplayed() {
        onView(withId(R.id.title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun titleTVIsCompletelyDisplayed() {
        onView(withId(R.id.title))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun titleTVText() {
        onView(withId(R.id.title)).
            check(matches(withText("Title")))
    }

    // Tests for the settings button
    @Test
    fun settingsButtonIsClickable() {
        onView(withId(R.id.settingsButton))
            .check(matches((isClickable())))
    }

    @Test
    fun settingsButtonIsEnabled() {
        onView(withId(R.id.settingsButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun settingsButtonIsDisplayed() {
        onView(withId(R.id.settingsButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun settingsButtonIsCompletelyDisplayed() {
        onView(withId(R.id.settingsButton))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun validateSettingsButtonIntentSentToPackage() {
        onView(withId(R.id.settingsButton)).perform(click())
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()
    }

    // Tests for the place text view

    @Test
    fun placeTVIsDisplayed() {
        onView(withId(R.id.place_txt))
            .check(matches(isDisplayed()))
    }

    @Test
    fun placeTVIsCompletelyDisplayed() {
        onView(withId(R.id.place_txt))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun placeTVText() {
        onView(withId(R.id.place_txt))
            .check(matches(withText(R.string.reviewPlace)))
    }

    // Tests for the place text view containing the information

    @Test
    fun place2TVIsDisplayed() {
        onView(withId(R.id.place_txt))
            .check(matches(isDisplayed()))
    }

    @Test
    fun place2TVIsCompletelyDisplayed() {
        onView(withId(R.id.place_txt))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun place2TVText() {
        onView(withId(R.id.place_txt))
            .check(matches(withText("Club33")))
    }

    // Tests for the additional information text view

    @Test
    fun addInfoTVIsDisplayed() {
        onView(withId(R.id.more_info_txt))
            .check(matches(isDisplayed()))
    }

    @Test
    fun addInfoTVIsCompletelyDisplayed() {
        onView(withId(R.id.more_info_txt))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun addInfoTVText() {
        onView(withId(R.id.more_info_txt))
            .check(matches(withText("Additional Information")))
    }

    // Tests for the timeslot text view

    @Test
    fun timeslotTVIsDisplayed() {
        onView(withId(R.id.timeslot_tv))
            .check(matches(isDisplayed()))
    }

    @Test
    fun timeslotTVIsCompletelyDisplayed() {
        onView(withId(R.id.timeslot_tv))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun timeslotTVText() {
        onView(withId(R.id.timeslot_tv))
            .check(matches(withText(R.string.timeslot)))
    }

    // Tests for the show timeslot text view

    @Test
    fun showTimeslotTVIsDisplayed() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(isDisplayed()))
    }

    @Test
    fun showTimeslotTVIsCompletelyDisplayed() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun showTimeslotTVText() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(withText("00:00")))
    }

    // Choose timeslot text view tests

    @Test
    fun chooseTimeslotTVIsDisplayed() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(isDisplayed()))
    }

    @Test
    fun chooseTimeslotTVIsCompletelyDisplayed() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun chooseTimeslotTVText() {
        onView(withId(R.id.tv_showTimeslot))
            .check(matches(withText(R.string.choose_time_slot)))
    }

    // Timeslot spinner tests
}