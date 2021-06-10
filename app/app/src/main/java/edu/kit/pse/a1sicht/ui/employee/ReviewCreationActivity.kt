@file:Suppress("DEPRECATION")

package edu.kit.pse.a1sicht.ui.employee

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_home_admin.settingsButton
import kotlinx.android.synthetic.main.activity_review_creation.*
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.databinding.ActivityReviewCreationBinding
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*


/**
 *  This is the activity class that implements the [BaseActivity] class
 *  and creates the review creation screen, uses the [ReviewScreenViewModel]
 *  and [ReviewScreenViewModel] for managing and storing information.
 *
 *  @author Hristo Klechorov, Tihomir Georgiev
 *  @see [BaseActivity]
 *  @see [ReviewCreationViewModel]
 *  @see [ReviewScreenViewModel]
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ReviewCreationActivity : BaseActivity() {

    private lateinit var reviewCreationViewModel: ReviewCreationViewModel
    private lateinit var reviewScreenViewModel: ReviewScreenViewModel
    private lateinit var binding: ActivityReviewCreationBinding

    // Array with possible choices
    private val items = arrayOf("5", "10", "15", "20", "25", "30")


    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hours = calendar.get(Calendar.HOUR_OF_DAY)
    private var minutes = calendar.get(Calendar.MINUTE)
    private var date = Date(year, month, day, hours, minutes)
    private val formatter = DateFormat.getTimeInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set the view models
        reviewCreationViewModel = ViewModelProviders.of(this).get(ReviewCreationViewModel::class.java)
        reviewScreenViewModel = ViewModelProviders.of(this).get(ReviewScreenViewModel::class.java)
        setContentView(R.layout.activity_review_creation)

        //Set the data binding
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_review_creation)
        binding.lifecycleOwner = this

        val timeSlotSpinner = findViewById<Spinner>(R.id.timeslotSpinner)

        // Adapter for the drop-down menu with the options.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSlotSpinner!!.adapter = adapter

        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        createDatePicker()
        createTimePicker()
        getReview()
        createReview()
        cancelCreation()

    }

    /**
     * This method sets action when the cancel button is clicked,
     * which cancels the current creation of a review.
     */
    private fun cancelCreation(){
        cancel_btn.setOnClickListener{
            val intent = Intent(this, HomeEmployeeActivity::class.java)
            startActivity(intent)
        }
    }
    /**
     * This method gets the information from the screen and creates a new review or updates a review.
     */
    private fun createReview() {
        var id = 0

        //Getting the id of the current employee
        reviewCreationViewModel.getEmployee().observe(this, Observer<Employee> {
            if (it != null) {
                id = it.id!!
            }
        })

        if(reviewScreenViewModel.getReviewDateTime().value != null){
            date= reviewScreenViewModel.getReviewDateTime().value!!
            binding.time = formatter.format(date)
            date.year+=1900
            binding.date = "${date.date}/${date.month+1}/${date.year}"
        }

        //Set the action on click on save button
        save_btn.setOnClickListener {
            val name: String? = nameET.text.toString()
            val room: String? = placeET.text.toString()
            val timeSlotLength = Integer.parseInt(timeslotSpinner.selectedItem.toString())
            var numberOfTimeSlots = 0
            if(timeslotNumET.text.toString() != ""){
                 numberOfTimeSlots = Integer.parseInt(timeslotNumET.text.toString())
            }
            val info: String? = moreInfoET.text.toString()
            var maxStudents = 0
            if(maxStudentsPerTimeslotET.text.toString() != ""){
                maxStudents = Integer.parseInt(maxStudentsPerTimeslotET.text.toString())
            }

            //In case of update
            if (name == null || room == null || numberOfTimeSlots == 0 || maxStudents == 0) {
               Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_LONG).show()
            } else {
                if (intent.extras != null) {
                    val caller = intent.extras.getString("caller")
                    if (caller == "ReviewScreen") {
                        val review = Review(
                            intent.extras.getInt("reviewId"), name, room, Timestamp(
                                date.year, date.month, date.date, date.hours,
                                date.minutes, 0, 0
                            ), timeSlotLength, numberOfTimeSlots,
                            id, info
                        )
                        reviewCreationViewModel.updateReview(intent.extras.getInt("reviewId"), review,maxStudents)
                    }
                } else {
                        reviewCreationViewModel.createReview(
                            name, room, date.year, date.month, date.date,
                            date.hours, date.minutes, timeSlotLength, numberOfTimeSlots, info, maxStudents, id
                        )
                }

                val intent = Intent(this, HomeEmployeeActivity::class.java)
                startActivity(intent)
           }
        }
    }

    /**
     * This method creates a new time dialog showing the current time.
     */
    private fun createTimePicker() {
        binding.time = formatter.format(date)
        pickTimeBtn.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                TimePickerDialog.OnTimeSetListener { _, mHours, mMinutes ->
                    if (mHours in 7..19) {
                        date.hours = mHours
                        date.minutes = mMinutes
                        binding.time = formatter.format(date)
                    } else {
                        Toast.makeText(this, "Time not allowed!", Toast.LENGTH_LONG).show()
                    }
                },
                date.hours,
                date.minutes,
                true
            )
            timePicker.show()
        }
    }

    /**
     * This method creates a new date dialog showing the current date.
     */
    private fun createDatePicker() {

        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)+1
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        binding.date = "${date.date}/${date.month+1}/${date.year}"

        pickDateBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    //Only future dates allowed
                    if (currentYear > mYear || (currentYear == mYear && currentMonth-1 > mMonth) || (currentYear == mYear && currentMonth-1== mMonth && currentDay > mDay)) {
                        Toast.makeText(this, "Only future dates allowed!", Toast.LENGTH_LONG).show()
                    } else {
                        date.year = mYear
                        date.month = mMonth
                        date.date = mDay
                        binding.date = "${date.date}/${date.month+1}/${date.year}"
                    }
                },
                date.year,
                date.month,
                date.date
            )
            datePicker.show()
        }
    }

    /**
     * This method gets the review from the [ReviewScreenActivity] in case of edit or update of the
     * review.
     */
    private fun getReview() {
        if (intent.extras != null) {
            val caller = intent.extras.getString("caller")
            if (caller == "ReviewScreen") {
                reviewScreenViewModel.getReviewById(intent.extras.getInt("reviewId"))
                binding.vm = reviewScreenViewModel
            }
        }
    }
}