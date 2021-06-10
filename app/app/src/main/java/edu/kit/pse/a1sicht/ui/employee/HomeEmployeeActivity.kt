package edu.kit.pse.a1sicht.ui.employee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import edu.kit.pse.a1sicht.ui.utils.ReviewAdapter
import kotlinx.android.synthetic.main.activity_home_employee.*
import kotlinx.android.synthetic.main.activity_list_review.settingsButton
import java.sql.Timestamp

/**
 * This is an activity class that shows information for the
 * home screen of a employee user, it uses the [HomeEmployeeViewModel]
 * class for providing information.
 *
 * @author Hristo Klechorov
 * @author Tihomir Georgiev
 *
 * @see [Employee]
 * @see [HomeEmployeeViewModel]
 * @see [BaseActivity]
 */
class HomeEmployeeActivity : BaseActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ReviewAdapter
    private lateinit var homeEmployeeViewModel: HomeEmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_employee)
        homeEmployeeViewModel = ViewModelProviders.of(this).get(HomeEmployeeViewModel::class.java)

        buildRecyclerView()
        setUpBtns()
    }


    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }

    override fun onBackPressed() {
        //Nothing
    }
    /**
     * This method gets all the reviews for the current logged employee and puts them in an
     * adapter.
     */
    private fun getAll() {
        var reviews: ArrayList<Review> = ArrayList()
        homeEmployeeViewModel.getEmployee().observe(this, Observer<Employee> { employee ->
            if (employee != null) {
                homeEmployeeViewModel.getAllReviewsForEmployee(employee.id!!)
                    .observe(this, Observer<List<Review>> { s ->
                        s.forEach {
                            reviews.add(it)
                        }
                    })
                createAdapterForReviews(reviews)
            }
        })
    }

    /**
     * This method creates an adapter for reviews.
     *
     * @param reviews This is the list with the reviews
     */
    private fun createAdapterForReviews(reviews: ArrayList<Review>) {
        adapter = ReviewAdapter(reviews, true)
        rv_review_list.adapter = adapter

        //Implementation of the onClick methods
        val mListener = object : ReviewAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                val review = reviews[position]
                homeEmployeeViewModel.deleteReviewById(review.id!!)
                getAll()
                Toast.makeText(
                    this@HomeEmployeeActivity,
                    getString(R.string.hm_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onItemClick(position: Int, v: View) {
                val context = v.context

                val showReviewIntent = Intent(context, ReviewScreenActivity::class.java)
                showReviewIntent.putExtra("reviewID", reviews[position].id)
                context.startActivity(showReviewIntent)

            }
        }

        adapter.setOnItemClickListener(mListener)
    }

    /**
     * Sets up the list with all the reviews made by the employee
     */
    private fun buildRecyclerView() {
        //Initialise the linear layout manager and assign it to our recycle view
        linearLayoutManager = LinearLayoutManager(this)
        rv_review_list.layoutManager = linearLayoutManager

        getAll()

    }

    /**
     * Sets up the Button onClick listeners
     */
    private fun setUpBtns() {
        // Star ReviewCreationActivity when the button is pressed
        btn_create_review.setOnClickListener {
            val intent = Intent(this, ReviewCreationActivity::class.java)
            startActivity(intent)
        }

        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            getAll()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}