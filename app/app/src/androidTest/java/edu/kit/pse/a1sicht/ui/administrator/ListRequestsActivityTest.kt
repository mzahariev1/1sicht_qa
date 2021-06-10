package edu.kit.pse.a1sicht.ui.administrator


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.annotation.UiThreadTest
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
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import edu.kit.pse.a1sicht.CustomMatchers.Companion.atPositionOnView
import edu.kit.pse.a1sicht.CustomMatchers.Companion.itemAtPosition
import edu.kit.pse.a1sicht.CustomMatchers.Companion.withItemCount
import edu.kit.pse.a1sicht.database.entities.Employee
import org.junit.Before

/**
 * Espresso tests for the ListRequestsActivity class
 *
 * @author Hristo Klechorov
 */
@RunWith(AndroidJUnit4::class)
class ListRequestsActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityTestRule(ListRequestsActivity::class.java)

    private lateinit var requests: ArrayList<Employee>

    // Tests for the title
    @Test
    fun titleTVText() {
        onView(withId(R.id.tv_title)).check(matches(withText(R.string.requestsButton)))
    }

    @Test
    fun titleTVIsDisplayed() {
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
    }

    @Test
    fun titleTVIsCompletelyDisplayed() {
        onView(withId(R.id.tv_title)).check(matches(isCompletelyDisplayed()))
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
        intended(IntentMatchers.toPackage("edu.kit.pse.a1sicht"))
        intended(hasComponent(SettingsActivity::class.java.name))
        Intents.assertNoUnverifiedIntents()

        Intents.release()
    }

    // Tests for the recycle view

    @UiThreadTest
    @Before
    fun setUpRecyclerViewTests() {
        requests = arrayListOf(Employee(1,"123","Gosho","Goshev",false),
        Employee(2, "456", "Misho", "Mishev", false),
        Employee(3, "789", "Dancho", "Danchev", false))

        val activity = activityScenarioRule.activity
        activity.adapter.requests = requests
        activity.findViewById<RecyclerView>(R.id.listRequests).layoutManager = LinearLayoutManager(activity.applicationContext)



    }

    @Test
    fun testNumberOfRecycleViewItems() {
        // 3 items are expected because 3 dummy items were created
        onView(withId(R.id.listRequests))
            .check(matches(withItemCount(3)))
    }

    @Test
    fun testFirstItem() {
        // Check if the item at position 0 is displayed
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, isDisplayed())))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, isCompletelyDisplayed())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(0, R.id.listRequests, R.id.tv_name)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.tv_name)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, hasDescendant(withText("Gosho Goshev")))))

        // Check if the item at position 0 has all the views it should have
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.btn_approve)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.btn_deny)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.iv_profilePic)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(0, hasDescendant(withId(R.id.tv_name)))))

        // Check whether the approve and deny buttons are displayed and clickable
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_approve)).check(matches(isClickable()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_approve)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_approve)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_approve)).check(matches(isEnabled()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_deny)).check(matches(isClickable()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_deny)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_deny)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.btn_deny)).check(matches(isEnabled()))

        // Check whether the avatar is completely displayed
        onView(atPositionOnView(0, R.id.listRequests, R.id.iv_profilePic)).check(matches(isDisplayed()))
        onView(atPositionOnView(0, R.id.listRequests, R.id.iv_profilePic)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testSecondItem() {
        // Check if the item at position 1 is displayed
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, isDisplayed())))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, isCompletelyDisplayed())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(1, R.id.listRequests, R.id.tv_name)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.tv_name)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, hasDescendant(withText("Misho Mishev")))))

        // Check if the item at position 1 has all the views it should have
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.btn_approve)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.btn_deny)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.iv_profilePic)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(1, hasDescendant(withId(R.id.tv_name)))))

        // Check whether the approve and deny buttons are displayed and clickable
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_approve)).check(matches(isClickable()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_approve)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_approve)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_approve)).check(matches(isEnabled()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_deny)).check(matches(isClickable()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_deny)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_deny)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.btn_deny)).check(matches(isEnabled()))

        // Check whether the avatar is completely displayed
        onView(atPositionOnView(1, R.id.listRequests, R.id.iv_profilePic)).check(matches(isDisplayed()))
        onView(atPositionOnView(1, R.id.listRequests, R.id.iv_profilePic)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testThirdItem() {
        // Check if the item at position 2 is displayed
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, isDisplayed())))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, isCompletelyDisplayed())))

        // Check whether the text view is being completely displayed with the proper name
        onView(atPositionOnView(2, R.id.listRequests, R.id.tv_name)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.tv_name)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, hasDescendant(withText("Dancho Danchev")))))

        // Check if the item at position 2 has all the views it should have
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.btn_approve)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.btn_deny)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.iv_profilePic)))))
        onView(withId(R.id.listRequests)).check(matches(itemAtPosition(2, hasDescendant(withId(R.id.tv_name)))))

        // Check whether the approve and deny buttons are displayed and clickable
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_approve)).check(matches(isClickable()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_approve)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_approve)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_approve)).check(matches(isEnabled()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_deny)).check(matches(isClickable()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_deny)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_deny)).check(matches(isCompletelyDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.btn_deny)).check(matches(isEnabled()))

        // Check whether the avatar is completely displayed
        onView(atPositionOnView(2, R.id.listRequests, R.id.iv_profilePic)).check(matches(isDisplayed()))
        onView(atPositionOnView(2, R.id.listRequests, R.id.iv_profilePic)).check(matches(isCompletelyDisplayed()))
    }

}