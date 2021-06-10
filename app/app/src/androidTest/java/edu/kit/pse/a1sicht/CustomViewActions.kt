package edu.kit.pse.a1sicht

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

class CustomViewActions {

    companion object {

        class ClickButtonWithId {

            companion object {
                fun clickChildViewWithId (id: Int): ViewAction {
                    return object : ViewAction {

                        override fun getDescription(): String {
                            return "Click on a child view with specified id."
                        }

                        override fun getConstraints(): Matcher<View>? {
                            return null
                        }

                        override fun perform(uiController: UiController?, view: View) {
                            val v = view.findViewById<ImageButton>(id)
                            v.performClick()
                        }
                    }
                }
            }

        }
    }
}