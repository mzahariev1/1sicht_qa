package edu.kit.pse.a1sicht

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * Holds all the custom matchers for the espresso tests
 *
 * @author Hristo Klechorov
 */
class CustomMatchers {

    companion object {

        /**
         * checks the item count of a recycler view
         * @param count the expected count
         * @return true if the expected count matches the number of items displayed
         * by the recycler view. false otherwise
         */
        fun withItemCount(count: Int): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item count: $count")
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    return item?.adapter?.itemCount == count
                }
            }
        }

        /**
         * Executes matcher on item at a given position in recyclerview
         *
         * @param position the position of the item in the recycler view that we want to test
         * @param itemMatcher the itemMatcher we want to use on the item
         * @return true if the view at the position passes the itemMatcher test. false otherwise
         */
        fun itemAtPosition(position: Int, itemMatcher: Matcher<View>?): Matcher<View> {
            checkNotNull(itemMatcher)
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description?) {
                    description?.appendText("Navigates to $position in a recycler view and uses a matcher on it")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    checkNotNull(item)
                    val viewHolder = item.findViewHolderForAdapterPosition(position)

                    return itemMatcher.matches(viewHolder?.itemView)
                }
            }
        }

        /**
         * Gains access to a specific view at a specific position in order to
         * run matchers on it
         * @param position the position of the item
         * @param recyclerViewId the id of the recycler view whose item we want to test
         * @param targetViewId the view that belongs to the item of the recycler view
         * @return true if the target view is found. false otherwise
         */
        fun atPositionOnView(position: Int,recyclerViewId: Int ,targetViewId: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {

                lateinit var resources: Resources

                override fun describeTo(description: Description?) {

                    var idDescription = recyclerViewId.toString()

                    if(this.resources != null) {
                        description?.appendText("")
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId)
                        } catch (exception: Resources.NotFoundException) {
                            idDescription = "resource name not found"
                        }
                    }

                    description!!.appendText("with id: " + idDescription)
                }

                override fun matchesSafely(item: View?): Boolean {
                    this.resources = item!!.resources
                    var childView: View? = null

                    if(childView == null) {
                        val recyclerView = item.rootView.findViewById<RecyclerView>(recyclerViewId)
                        if (recyclerView != null && recyclerView.id == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                        } else {
                            return false
                        }
                    }

                    if(targetViewId == -1) {
                        return item == childView
                    } else {
                        val targetView = childView.findViewById<View>(targetViewId)
                        return item == targetView
                    }
                }
            }
        }
    }

}