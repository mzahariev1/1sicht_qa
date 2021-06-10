package edu.kit.pse.a1sicht.ui.employee


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.kit.pse.a1sicht.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 *Testing class for the HomeEmployeeActivity
 *
 *@author Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class HomeEmployeeActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(HomeEmployeeActivity::class.java)


    /**
     *    Tests the createReviewButtons title.
     * */
    @Test
    fun createReviewButtonText() {
        onView(withId(R.id.btn_create_review)).check(matches(withText(R.string.reviewCreationHeader)))
    }

    /**
     * Tests if the CreateReviewButton is clickable.
     */
    @Test
    fun createReviewButtonIsClickable() {
        onView(withId(R.id.btn_create_review)).check(matches((isClickable())))
    }

    /**
     * Tests if the CreateReviewButton is enabled.
     */
    @Test
    fun createReviewButtonIsEnabled() {
        onView(withId(R.id.btn_create_review)).check(matches(isEnabled()))
    }

    /**
     * Tests if the CreateReviewButton is displayed.
     */
    @Test
    fun createReviewButtonIsDisplayed() {
        onView(withId(R.id.btn_create_review)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the CreateReviewButton is completly displayed.
     */
    @Test
    fun createReviewButtonIsCompletelyDisplayed() {
        onView(withId(R.id.btn_create_review)).check(matches(isCompletelyDisplayed()))
    }


    /**
     * Tests the Button to create reviews and cancels to come back to HomeEmployeeActivity.
     * @throws Exception on Error
     */
    @Test
    @Throws(Exception::class)
    fun testCreateReviewButtonAndCancel() {
        Intents.init()

        onView(withId(R.id.btn_create_review)).perform(click())
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.cancel_btn)).perform(click())
        intended(hasComponent(HomeEmployeeActivity::class.java.canonicalName))

        Intents.release()
    }
}