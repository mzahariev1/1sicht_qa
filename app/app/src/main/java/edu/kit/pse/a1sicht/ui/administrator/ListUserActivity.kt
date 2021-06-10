package edu.kit.pse.a1sicht.ui.administrator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.ui.shared.SettingsActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import edu.kit.pse.a1sicht.ui.utils.UsersAdapter
import kotlinx.android.synthetic.main.activity_home_admin.settingsButton
import kotlinx.android.synthetic.main.activity_list_user.*


import kotlin.collections.ArrayList

/**
 * The activity for the screen where an administrator can
 * view and delete user accounts *
 * @author Hristo Klechorov
 */
class ListUserActivity: BaseActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: UsersAdapter
    private lateinit var listUserViewModel: ListUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_user)
        listUserViewModel = ViewModelProviders.of(this).get(ListUserViewModel::class.java)
        buildRecyclerView()

        setUpBtns()
    }

    /**
     * Adds all users from the database to the users ArrayList
     * in order to show them in the RecyclerView
     */
    private fun getAll(){
        val users:ArrayList<Any> = ArrayList()
        listUserViewModel.getAllEmployees().observe(this, Observer<List<Employee>> { list ->
            list.forEach {
                users.add(it)
            }
        })
        listUserViewModel.getAllStudents().observe(this, Observer<List<Student>> { list ->
            list.forEach {
                users.add(it)
            }
        })
        createAdapterForUsers(users)
    }

    /**
     * Initializes the adapter and implements the onClick listeners
     * @param users The ArrayList with users that will be displayed by the RecyclerView
     */
    private fun createAdapterForUsers(users:ArrayList<Any>){
        adapter = UsersAdapter(users)
        listUser.adapter = adapter

        val mListener = object: UsersAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val user = users[position]
                if(user is Employee) {
                    listUserViewModel.deleteEmployeeById(user.id!!)
                    getAll()
                } else if (user is Student) {
                    listUserViewModel.deleteStudentById(user.id)
                    getAll()
                }
            }
        }

        adapter.setOnItemClickListener(mListener)
    }

    /**
     * Sets up the list with all the users
     */
    private fun buildRecyclerView() {
        //Initialise the linear layout manager and assign it to our recycle view
        linearLayoutManager = LinearLayoutManager(this)
        listUser.layoutManager = linearLayoutManager

        getAll()
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