package edu.kit.pse.a1sicht.ui.administrator

import android.content.Intent
import android.os.Bundle
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import kotlinx.android.synthetic.main.activity_home_admin.*
import edu.kit.pse.a1sicht.ui.utils.BaseActivity

/**
 * The activity for the admin home screen of the application. *
 * @author Hristo Klechorov
 */
class HomeAdminActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        setUpBtns()
    }

    /**
     * Sets up the button OnClicks
     */
    private fun setUpBtns() {
        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        //Start ListReviewActivity when the reviews button is pressed
        reviewsButton.setOnClickListener {
            val intent = Intent(this, ListReviewActivity::class.java)
            startActivity(intent)
        }

        //Start ListUserActivity when the users button is pressed
        usersButton.setOnClickListener {
            val intent = Intent(this, ListUserActivity::class.java)
            startActivity(intent)
        }

        //Start ListRequestsActivity when the requests button is pressed
        requestsButton.setOnClickListener {
            val intent = Intent(this, ListRequestsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        //Nothing
    }
}