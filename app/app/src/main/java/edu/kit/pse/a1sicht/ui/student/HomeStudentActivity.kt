package edu.kit.pse.a1sicht.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.ui.administrator.ListReviewViewModel
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import edu.kit.pse.a1sicht.ui.utils.ReviewAdapter
import kotlinx.android.synthetic.main.activity_home_student.*
import kotlinx.android.synthetic.main.activity_list_review.listReview
import kotlinx.android.synthetic.main.activity_list_review.settingsButton
import java.sql.Timestamp

/**
 * The activity for the student home screen
 * @author Hristo Klechorov
 */
class HomeStudentActivity : BaseActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: ReviewAdapter
    private lateinit var listReviewViewModel: ListReviewViewModel
    private lateinit var studentReviewViewModel: StudentReviewViewModel
    private var studentId: Int = 0


    //////////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_student)
        listReviewViewModel = ViewModelProviders.of(this).get(ListReviewViewModel::class.java)
        studentReviewViewModel = ViewModelProviders.of(this).get(StudentReviewViewModel::class.java)


        /////////////////////////////////


        buildRecyclerView()
        setUpBtns()
    }

    /**
     * Sets up the button OnClicks
     */
    private fun setUpBtns() {
        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("caller", "edu.kit.pse.a1sicht.ui.student.HomeStudentActivity")
            startActivity(intent)
        }
        review_sign_up_btn.setOnClickListener {
            val intent = Intent(this, ListReviewsActivity::class.java)
            intent.putExtra("caller", "edu.kit.pse.a1sicht.ui.student.HomeStudentActivity")
            startActivity(intent)
        }
    }

    /**
     * Sets up the list with all the reviews
     */
    private fun buildRecyclerView() {
        //Initialise the linear layout manager and assign it to our recycle view
        linearLayoutManager = LinearLayoutManager(this)
        listReview.layoutManager = linearLayoutManager

        getStudentReviews()
    }

    override fun onBackPressed() {
        //Nothing
    }
    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }
    /**
     * Populates the reviews ArrayList with the reviews at which the student will take part
     */
    private fun getStudentReviews() {
        val reviews: java.util.ArrayList<Review> = java.util.ArrayList()
        studentReviewViewModel.getStudent().observe(this, Observer<Student> { student ->
            if (student != null) {
                studentId = student.id
                listReviewViewModel.getAllReviewsForStudent(studentId).observe(this, Observer<List<Review>> { list ->
                    list.forEach {
                        if (!it.date!!.before(Timestamp(System.currentTimeMillis()))) {
                            reviews.add(it)
                        }
                    }
                    createAdapterForReviews(reviews)
                })
            }
        })

    }

    /**
     * Initializes the adapter and implements the onClick methods
     * @param reviews The reviews that will be displayed in the RecyclerView
     */
    private fun createAdapterForReviews(reviews: ArrayList<Review>) {
        adapter = ReviewAdapter(reviews, true)
        listReview.adapter = adapter

        //Implementation of the onClick methods
        val mListener = object : ReviewAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                val review = reviews[position]
                studentReviewViewModel.signOutFromTimeSlot(studentId, review.id!!)
                Toast.makeText(this@HomeStudentActivity, getString(R.string.std_toast), Toast.LENGTH_LONG).show()
                getStudentReviews()
            }

            override fun onItemClick(position: Int, v: View) {
                val context = v.context

                val showReviewIntent = Intent(context, StudentReviewScreenActivity::class.java)
                showReviewIntent.putExtra("reviewId", reviews[position].id)
                showReviewIntent.putExtra("caller", "HomeStudentActivity")
                context.startActivity(showReviewIntent)

            }
        }

        adapter.setOnItemClickListener(mListener)
    }
}

