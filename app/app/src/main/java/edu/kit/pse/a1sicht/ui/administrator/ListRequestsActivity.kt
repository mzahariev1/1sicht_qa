package edu.kit.pse.a1sicht.ui.administrator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import edu.kit.pse.a1sicht.ui.utils.RequestsAdapter
import kotlinx.android.synthetic.main.activity_list_requests.*
import kotlinx.android.synthetic.main.activity_list_user.settingsButton

/**
 * The Activity for the screen that displays all the employee requests to the admin.
 * @author Hristo Klechorov
 */
class ListRequestsActivity : BaseActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var listRequestsViewModel: ListRequestsViewModel
    lateinit var adapter: RequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_requests)
        listRequestsViewModel = ViewModelProviders.of(this).get(ListRequestsViewModel::class.java)

        buildRecyclerView()
        setUpBtns()
    }

    /**
     * Adds all unverified employees from the database to the requests ArrayList
     * in order to show them in the RecyclerView
     */
    private fun getAllUnverified() {
        val requests:ArrayList<Employee> = ArrayList()
        listRequestsViewModel.getAllUnverifiedEmployees().observe(this, Observer<List<Employee>> { list ->
            list.forEach {
                requests.add(it)
            }
            createAdapterForRequest(requests)
        })
    }

    /**
     * Initializes the adapter for the RecyclerView and implements the button OnClicks
     * @param requests The ArrayList of requests that will be displayed by the RecyclerView
     */
    private fun createAdapterForRequest(requests: ArrayList<Employee>) {
        adapter = RequestsAdapter(requests)
        listRequests.adapter = adapter

        val mListener = object: RequestsAdapter.OnItemClickListener{
            override fun onAcceptClick(position: Int) {
                listRequestsViewModel.verifyEmployeeById(requests[position].id!!)
                Toast.makeText(this@ListRequestsActivity,getString(R.string.emp_confirm_toast),Toast.LENGTH_LONG).show()
                getAllUnverified()
            }

            override fun onDeclineClick(position: Int) {
                listRequestsViewModel.deleteEmployeeById(requests[position].id!!)
                Toast.makeText(this@ListRequestsActivity,getString(R.string.emp_decline_toast),Toast.LENGTH_LONG).show()
                getAllUnverified()
            }
        }

        adapter.setOnItemClickListener(mListener)
    }

    /**
     * Sets up the list with all employee requests
     */
    fun buildRecyclerView() {
        //Initialise the linear layout manager and assign it to our recycle view
        linearLayoutManager = LinearLayoutManager(this)
        listRequests.layoutManager = linearLayoutManager

        //Initialise the adapter and assign it to our RecycleView
        getAllUnverified()

    }

    /**
     * Sets up the button onClicks
     */
    private fun setUpBtns() {
        // Start SettingsActivity when the button is pressed
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}