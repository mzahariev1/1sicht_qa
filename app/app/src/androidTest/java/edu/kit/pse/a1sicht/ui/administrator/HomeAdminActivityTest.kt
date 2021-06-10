package edu.kit.pse.a1sicht.ui.administrator


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test class for the HomeAdminActivity
 *
 * @author Hristo Klechorov
 */
@RunWith(AndroidJUnit4::class)
class HomeAdminActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(HomeAdminActivity::class.java)

    // Tests for the title
    @Test
    fun titleTVText() {
        onView(withId(R.id.title)).check(matches(withText(R.string.home_header)))
    }

    @Test
    fun titleTVIsDisplayed() {
        onView(withId(R.id.title)).check(matches(isDisplayed()))
    }

    @Test
    fun titleTVIsCompletelyDisplayed() {
        onView(withId(R.id.title)).check(matches(isCompletelyDisplayed()))
    }

    // Tests for the usersButton
    @Test
    fun usersButtonText() {
        onView(withId(R.id.usersButton)).check(matches(withText(R.string.usersButton)))
    }

    @Test
    fun usersButtonIsClickable() {
        onView(withId(R.id.usersButton)).check(matches((isClickable())))
    }

    @Test
    fun usersButtonIsEnabled() {
        onView(withId(R.id.usersButton)).check(matches(isEnabled()))
    }

    @Test
    fun usersButtonIsDisplayed() {
        onView(withId(R.id.usersButton)).check(matches(isDisplayed()))
    }

    @Test
    fun usersButtonIsCompletelyDisplayed() {
        onView(withId(R.id.usersButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun validateUsersButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.usersButton)).perform(click())
        intended(toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(ListUserActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    // Tests for the requestsButton
    @Test
    fun requestsButtonText() {
        onView(withId(R.id.requestsButton)).check(matches(withText(R.string.requestsButton)))
    }

    @Test
    fun requestsButtonIsClickable() {
        onView(withId(R.id.requestsButton)).check(matches((isClickable())))
    }

    @Test
    fun requestsButtonIsEnabled() {
        onView(withId(R.id.requestsButton)).check(matches(isEnabled()))
    }

    @Test
    fun requestsButtonIsDisplayed() {
        onView(withId(R.id.requestsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun requestsButtonIsCompletelyDisplayed() {
        onView(withId(R.id.requestsButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun validateRequestsButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.requestsButton)).perform(click())
        intended(toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(ListRequestsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    // Tests for the reviewsButton
    @Test
    fun buttonReviewsText() {
        onView(withId(R.id.reviewsButton)).check(matches(withText(R.string.reviewsButton)))
    }

    @Test
    fun reviewsButtonIsClickable() {
        onView(withId(R.id.reviewsButton)).check(matches((isClickable())))
    }

    @Test
    fun reviewsButtonIsEnabled() {
        onView(withId(R.id.reviewsButton)).check(matches(isEnabled()))
    }

    @Test
    fun reviewsButtonIsDisplayed() {
        onView(withId(R.id.reviewsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun reviewsButtonIsCompletelyDisplayed() {
        onView(withId(R.id.reviewsButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun validateReviewsButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.reviewsButton)).perform(click())
        intended(toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(ListReviewActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
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
        Intents.init()

        onView(withId(R.id.settingsButton)).perform(click())
        intended(toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }
}