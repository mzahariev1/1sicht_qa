package edu.kit.pse.a1sicht.ui.student

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.CustomMatchers.Companion.atPositionOnView
import edu.kit.pse.a1sicht.CustomMatchers.Companion.itemAtPosition
import edu.kit.pse.a1sicht.CustomMatchers.Companion.withItemCount
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Timestamp

/**
 * Test class for [ListReviewsActivity]
 * @author Hristo Klechorov
 */
@RunWith(AndroidJUnit4::class)
class ListReviewsActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityTestRule(ListReviewsActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    // Tests for the title
    @Test
    fun titleTVText() {
        onView(withId(R.id.title)).check(matches(withText(R.string.listReviewHeader)))
    }

    @Test
    fun titleTVIsDisplayed() {
        onView(withId(R.id.title)).check(matches(isDisplayed()))
    }

    @Test
    fun titleTVIsCompletelyDisplayed() {
        onView(withId(R.id.title)).check(matches(isCompletelyDisplayed()))
    }

    // Tests for the settings button
    @Test
    fun settingsButtonIsClickable() {
        onView(withId(R.id.settingsButton)).check(matches((isClickable())))
    }

    @Test
    fun settingsButtonIsEnabled() {
        onView(withId(R.id.settingsButton)).check(matches(isEnabled()))
    }

    @Test
    fun settingsButtonIsDisplayed() {
        onView(withId(R.id.settingsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun settingsButtonIsCompletelyDisplayed() {
        onView(withId(R.id.settingsButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun validateSettingsButtonIntentSentToPackage() {
        onView(withId(R.id.settingsButton)).perform(click())
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()
    }

    // Recycler View Tests

    @UiThreadTest
    @Before
    fun setUpRecyclerViewTests() {
        val reviews = arrayListOf(
            Review(1, "TGI", "25", Timestamp(25), 2, 3, 5, "run"),
            Review(2, "HM", "26", Timestamp(28), 1, 4, 2, "walk"),
            Review(1, "LA1", "27", Timestamp(26), 9, 5, 7, "crawl"))

        val activity = activityScenarioRule.activity
        activity.adapter.reviews = reviews
        activity.findViewById<RecyclerView>(R.id.listReview).layoutManager = LinearLayoutManager(activity.applicationContext)
    }

    @Test
    fun testNumberOfRecycleViewItems() {
        // 3 items are expected because 3 dummy items were created
        onView(withId(R.id.listReview))
            .check(matches(withItemCount(3)))
    }

    @Test
    fun testFirstItem() {
        // Check if the item at position 0 is displayed and clickable
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, isDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, isCompletelyDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, isClickable())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, isEnabled())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(0, R.id.listReview, R.id.reviewName)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listReview, R.id.reviewName)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, hasDescendant(withText("TGI")))))

        // Check if the item at position 0 has all the views it should have
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.imageView2)))))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.reviewName)))))

        // Check whether the picture is completely displayed
        onView(atPositionOnView(0, R.id.listReview, R.id.imageView2)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listReview, R.id.imageView2)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testSecondItem() {
        // Check if the item at position 1 is displayed and clickable
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, isDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, isCompletelyDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, isClickable())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, isEnabled())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(1, R.id.listReview, R.id.reviewName)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listReview, R.id.reviewName)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, hasDescendant(withText("HM")))))


        // Check if the item at position 1 has all the views it should have
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.imageView2)))))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.reviewName)))))


        // Check whether the picture is completely displayed
        onView(atPositionOnView(1, R.id.listReview, R.id.imageView2)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listReview, R.id.imageView2)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testThirdItem() {
        // Check if the item at position 2 is displayed and clickable
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, isDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, isCompletelyDisplayed())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, isClickable())))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, isEnabled())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(2, R.id.listReview, R.id.reviewName)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listReview, R.id.reviewName)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, hasDescendant(withText("LA1")))))


        // Check if the item at position 2 has all the views it should have
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.imageView2)))))
        onView(withId(R.id.listReview)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.reviewName)))))


        // Check whether the picture is completely displayed
        onView(atPositionOnView(2, R.id.listReview, R.id.imageView2)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listReview, R.id.imageView2)).check(matches(isCompletelyDisplayed()))
    }
}