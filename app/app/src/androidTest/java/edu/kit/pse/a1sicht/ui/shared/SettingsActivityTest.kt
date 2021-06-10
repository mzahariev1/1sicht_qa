package edu.kit.pse.a1sicht.ui.shared


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
 * Test class for [SettingsActivity] class.
 *
 * @author Tihomir Georgiev, Maximilian Ackermann
 */
@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {
    @get:Rule
    var activityScenarioRule = ActivityTestRule(SettingsActivity::class.java)


    /**
     *    Tests the SaveButtons title.
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
    fun saveButtonIsCompletelyDisplayed() {
        onView(withId(R.id.save_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests if the SaveButton brings the user to the correct following Activity.
     */
    @Test
    fun validateSaveButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.save_btn)).perform(click())
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    /**
     *    Tests the deButtons title.
     * */
    @Test
    fun deButtonImage() {
        onView(withId(R.id.de_btn)).check(matches(withText(R.drawable.de)))
    }

    /**
     * Tests if the deButton is clickable.
     */
    @Test
    fun deButtonIsClickable() {
        onView(withId(R.id.de_btn)).check(matches((isClickable())))
    }

    /**
     * Tests if the deButton is enabled.
     */
    @Test
    fun deButtonIsEnabled() {
        onView(withId(R.id.de_btn)).check(matches(isEnabled()))
    }

    /**
     * Tests if the deButton is displayed.
     */
    @Test
    fun deButtonIsDisplayed() {
        onView(withId(R.id.de_btn)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the deButton is completly displayed.
     */
    @Test
    fun deButtonIsCompletelyDisplayed() {
        onView(withId(R.id.de_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests the following Activity on deButton click.
     */
    @Test
    fun validateDeButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.de_btn)).perform(click())
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    /**
     *    Tests the gbButtons title.
     * */
    @Test
    fun gbButtonImage() {
        onView(withId(R.id.gb_btn)).check(matches(withText(R.drawable.gb)))
    }

    /**
     * Tests if the gbButton is clickable.
     */
    @Test
    fun gbButtonIsClickable() {
        onView(withId(R.id.gb_btn)).check(matches((isClickable())))
    }

    /**
     * Tests if the gbButton is enabled.
     */
    @Test
    fun gbButtonIsEnabled() {
        onView(withId(R.id.gb_btn)).check(matches(isEnabled()))
    }

    /**
     * Tests if the gbButton is displayed.
     */
    @Test
    fun gbButtonIsDisplayed() {
        onView(withId(R.id.gb_btn)).check(matches(isDisplayed()))
    }

    /**
     * Tests if the gbButton is completly displayed.
     */
    @Test
    fun gbButtonIsCompletelyDisplayed() {
        onView(withId(R.id.gb_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests the following activity on gbButton click.
     */
    @Test
    fun validateGbButtonIntentSentToPackage() {
        Intents.init()

        onView(withId(R.id.de_btn)).perform(click())
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    /**
     * Tests for SignOutButton
     */
    @Test
    fun signOutButtonImage() {
        onView(withId(R.id.sign_out_real_btn)).check(matches(withText(R.string.sign_out)))
    }

    @Test
    fun signOutButtonIsClickable() {
        onView(withId(R.id.sign_out_real_btn)).check(matches((isClickable())))
    }

    @Test
    fun signOutButtonIsEnabled() {
        onView(withId(R.id.sign_out_real_btn)).check(matches(isEnabled()))
    }

    @Test
    fun signOutButtonIsDisplayed() {
        onView(withId(R.id.sign_out_real_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun signOutButtonIsCompletelyDisplayed() {
        onView(withId(R.id.sign_out_real_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Tests for DeleteAccountButton
     */
    @Test
    fun deleteAccountButtonImage() {
        onView(withId(R.id.delete_acc_btn)).check(matches(withText(R.string.delete_account)))
    }

    @Test
    fun deleteAccountButtonIsClickable() {
        onView(withId(R.id.delete_acc_btn)).check(matches((isClickable())))
    }

    @Test
    fun deleteAccountButtonIsEnabled() {
        onView(withId(R.id.delete_acc_btn)).check(matches(isEnabled()))
    }

    @Test
    fun deleteAccountButtonIsDisplayed() {
        onView(withId(R.id.delete_acc_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteAccountButtonIsCompletelyDisplayed() {
        onView(withId(R.id.delete_acc_btn)).check(matches(isCompletelyDisplayed()))
    }

    /**
     * Test language change on buttons clicked
     * @throws Exception on error
     */
    @Test
    @Throws(Exception::class)
    fun testLanguageChange() {
        onView(withId(R.id.de_btn)).perform(click())
        onView(withId(R.id.save_btn)).check(matches(withText("Speichern")))
        onView(withId(R.id.gb_btn)).perform(click())
        onView(withId(R.id.save_btn)).check(matches(withText("Save")))
    }

    /**
     * Test sign out button
     * @throws Exception on error
     */
    @Test
    @Throws(Exception::class)
    fun testSignOut() {
        Intents.init()
        onView(withId(R.id.sign_out_real_btn)).perform(click())
        intended(hasComponent(LogInActivity::class.java.canonicalName))
        onView(withId(R.id.signInBtn)).check(matches(isDisplayed()))
        Intents.release()
    }

    /**
     * Test Name/Last name TextFields
     */
    @Test
    @Throws(Exception::class)
    fun testNames() {
        Intents.init()

        onView(withId(R.id.name_edit_text))
            .perform(ViewActions.typeText("name"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.last_name_edit_text))
            .perform(ViewActions.typeText("lastName"), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.name_edit_text))
            .check(matches(withText(Matchers.containsString("name"))))
        onView(withId(R.id.last_name_edit_text))
            .check(matches(withText(Matchers.containsString("lastName"))))

        Intents.release()
    }
}