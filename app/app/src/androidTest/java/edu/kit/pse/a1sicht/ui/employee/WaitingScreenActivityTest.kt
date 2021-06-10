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
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.hamcrest.Matchers


/**
 *Testing class for the WaitingScreenActivity
 *
 *@author Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class WaitingScreenActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(WaitingScreenActivity::class.java)


    /**
     * Tests if the Text viewed in the waiting screen is the correct one.
     */
    @Test
    @Throws(Exception::class)
    fun testText() {
        Intents.init()

        onView(withId(R.id.textView))
            .check(matches(withText(Matchers.containsString("Please wait for"))));

        Intents.release()
    }
}