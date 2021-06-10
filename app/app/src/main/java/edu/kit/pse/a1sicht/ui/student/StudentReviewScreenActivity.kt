package edu.kit.pse.a1sicht.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.databinding.ActivityStudentReviewScreenBinding
import edu.kit.pse.a1sicht.ui.employee.ReviewScreenViewModel
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_home_admin.settingsButton
import kotlinx.android.synthetic.main.activity_student_review_screen.*
import kotlinx.android.synthetic.main.activity_student_review_screen.timeslotSpinner
import java.text.DateFormat
import kotlin.collections.ArrayList

/**
 * The activity for the review screen that appears to the student
 * @author Hristo Klechorov, Tihomir Georgiev
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StudentReviewScreenActivity : BaseActivity() {

    private lateinit var reviewScreenViewModel: ReviewScreenViewModel
    private lateinit var studentReviewViewModel: StudentReviewViewModel
    private lateinit var binding: ActivityStudentReviewScreenBinding
    private var reviewId: Int = 0
    private var studentId: Int = 0
    private var caller: String = ""
    // Array with possible choices
    private var itemss: ArrayList<String> = arrayListOf()
    private val formatter = DateFormat.getDateTimeInstance()
    // when repository is ready must be implemented
    private var isRegistered = false


    //////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_review_screen)
        isRegistered()


        reviewScreenViewModel = ViewModelProviders.of(this).get(ReviewScreenViewModel::class.java)
        studentReviewViewModel = ViewModelProviders.of(this).get(StudentReviewViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_review_screen)
        binding.lifecycleOwner = this
        binding.reviewScreenVM = reviewScreenViewModel

        getCurrentStudent()
        changeTimeSlot()

        reviewId = intent.extras.getInt("reviewId")
        if (intent.extras.getString("caller") != null) {
            caller = intent.extras.getString("caller")
        }

        getTimeSlot()
        isRegistered()

        review_sign_up_btn.setOnClickListener {
            signUp()
        }

        signOut()
        reviewInfo()
        settingsBtn()
    }

    /**
     * This method gets the current logged in student user.
     */
    private fun getCurrentStudent() {
        studentReviewViewModel.getStudent().observe(this, Observer<Student> { student ->
            if (student != null) {
                studentId = student.id
            }
        })
    }

    /**
     * This method gets the time slot's date for which the current student is signed for.
     */
    private fun getTimeSlot() {
        if (isRegistered || caller == "HomeStudentActivity") {
            studentReviewViewModel.getStudent().observe(this, Observer<Student> { student ->
                if (student != null) {
                    studentId = student.id
                    studentReviewViewModel.getTimeSlotDate(reviewId, studentId).observe(this, Observer {
                        binding.timeSlot = it
                    })
                }
            })
        }

    }

    /**
     * Checks if the student has already registered himself for the review
     * If that is the case an alternative hidden part of the layout becomes visible
     */
    private fun isRegistered() {
        val timeSlotTxt = findViewById<TextView>(R.id.timeslot_tv)
        val showTimeSlotTxt = findViewById<TextView>(R.id.tv_showTimeslot)
        val changeTimeSlotBtn = findViewById<Button>(R.id.btn_changeTimeslot)
        val signOutBtn = findViewById<Button>(R.id.btn_deregister)

        val timeSlotUnsigned = findViewById<TextView>(R.id.timeslot)
        val timeslotSpinner = findViewById<Spinner>(R.id.timeslotSpinner)
        val signUpBtn = findViewById<Button>(R.id.review_sign_up_btn)
        if (caller == "HomeStudentActivity" || isRegistered) {
            timeSlotTxt.visibility = View.VISIBLE
            showTimeSlotTxt.visibility = View.VISIBLE
            changeTimeSlotBtn.visibility = View.VISIBLE
            signOutBtn.visibility = View.VISIBLE
            timeSlotUnsigned.visibility = View.GONE
            timeslotSpinner.visibility = View.GONE
            signUpBtn.visibility = View.GONE
            getTimeSlot()
        } else {
            timeSlotTxt.visibility = View.GONE
            showTimeSlotTxt.visibility = View.GONE
            changeTimeSlotBtn.visibility = View.GONE
            signOutBtn.visibility = View.GONE
            timeSlotUnsigned.visibility = View.VISIBLE
            timeslotSpinner.visibility = View.VISIBLE
            signUpBtn.visibility = View.VISIBLE
        }
    }

    /**
     * This method fills the [timeslotSpinner] with the time slots of a review.
     */
    private fun reviewInfo() {
        val timeslotSpinner = findViewById<Spinner>(R.id.timeslotSpinner)
        reviewScreenViewModel.getReviewById(reviewId)
        studentReviewViewModel.getTimeSlotsForReview(reviewId).observe(this, Observer { list ->
            if (itemss.isEmpty()) {
                list.forEach {
                    itemss.add(formatter.format(it.startTime))
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemss)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                timeslotSpinner!!.adapter = adapter
            }
        })
    }

    /**
     * This method signs up the current logged in student for a review(time slot).
     */
    private fun signUp() {
        val id = timeslotSpinner.selectedItemId
        var i: Long
        studentReviewViewModel.getTimeSlotsForReview(reviewId).observe(this, Observer { list ->
            i = 0
            list.forEach {
                if (id == i) {
                    if (studentReviewViewModel.signUpForTimeSlot(studentId, it.id!!, reviewId)) {
                        Toast.makeText(this, getString(R.string.successful_toast), Toast.LENGTH_LONG).show()
                        isRegistered = true
                        isRegistered()
                        val intent = Intent(this, HomeStudentActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, getString(R.string.ts_full_toast), Toast.LENGTH_LONG).show()
                    }
                    i++
                } else {
                    i++
                }
            }
        })
    }

    /**
     * This method sets action on the [btn_changeTimeslot], which changes the time slot of
     * the current logged in student.
     */
    private fun changeTimeSlot(){
        var isTrue = false
        btn_changeTimeslot.setOnClickListener {
            //First click show options
            if(!isTrue){
                timeslotSpinner.visibility = View.VISIBLE
                timeslot.visibility = View.VISIBLE
                reviewInfo()
                isTrue = true
            }
            //On second click changes the time slots
            else{
                val id = timeslotSpinner.selectedItemId
                var i: Long
                studentReviewViewModel.getTimeSlotsForReview(reviewId).observe(this, Observer { list ->
                    i = 0
                    list.forEach {
                        if (id == i) {
                            when {
                                //Checks if the new time slot is full
                                it.currentStudents == it.maxStudents -> Toast.makeText(this, getString(R.string.ts_full_toast), Toast.LENGTH_LONG).show()
                                studentReviewViewModel.signOutFromTimeSlot(studentId, reviewId) -> {
                                    timeslotSpinner.visibility = View.GONE
                                    timeslot.visibility = View.GONE
                                    isTrue = false
                                    studentReviewViewModel.signUpForTimeSlot(studentId, it.id!!, reviewId)
                                    Toast.makeText(this, getString(R.string.successful_toast), Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, HomeStudentActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            i++
                        } else {
                            i++
                        }
                    }
                })
            }
        }

    }

    /**
     * This method sets action on the [btn_deregister], which signs out the current logged in
     * student from a review(time slot).
     */
    private fun signOut() {
        btn_deregister.setOnClickListener {
            if (studentReviewViewModel.signOutFromTimeSlot(studentId, reviewId)) {
                //Tells if the operation was successful
                Toast.makeText(this, getString(R.string.success_sign_out_toast), Toast.LENGTH_LONG).show()
                isRegistered = false

                //Goes back to home screen
                val intent = Intent(this, HomeStudentActivity::class.java)
                startActivity(intent)
            } else {
                //Shows error message on fail
                Toast.makeText(this, getString(R.string.error_toast), Toast.LENGTH_LONG).show()
            }
        }

    }


    /**
     * Sets up the button onClicks
     */
    private fun settingsBtn() {
        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("caller", "edu.kit.pse.a1sicht.ui.student.HomeStudentActivity")
            startActivity(intent)
        }
    }
}