package edu.kit.pse.a1sicht.ui.employee

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import edu.kit.pse.a1sicht.databinding.ActivityEmployeeReviewScreenBinding
import kotlinx.android.synthetic.main.activity_employee_review_screen.*
import kotlinx.android.synthetic.main.activity_home_admin.settingsButton

/**
 * The Activity for the screen with review information available to the employee.
 *
 * @author Hristo Klechorov, Tihomir Georgiev
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ReviewScreenActivity: BaseActivity(){
    private lateinit var reviewScreenViewModel: ReviewScreenViewModel
    private lateinit var binding :ActivityEmployeeReviewScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_employee_review_screen)

        reviewScreenViewModel = ViewModelProviders.of(this).get(ReviewScreenViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_employee_review_screen)

        binding.lifecycleOwner = this
        binding.review = reviewScreenViewModel
        getReview()
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        edit_btn.setOnClickListener {
            editButton()
        }
    }

    private fun getReview() {
        val reviewId = intent.extras.getInt("reviewID")
        reviewScreenViewModel.getReviewById(reviewId)
    }

    private fun editButton(){
        val intent = Intent(this, ReviewCreationActivity::class.java)
        intent.putExtra("caller","ReviewScreen")
        intent.putExtra("reviewId",this.intent.extras.getInt("reviewID"))
        startActivity(intent)
    }
}