package edu.kit.pse.a1sicht.ui.shared

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Administrator
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.databinding.ActivitySettingsBinding
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import androidx.lifecycle.Observer


/**
 * The activity for the settings screen of the application.
 *
 * @author Tihomir Georgiev
 * @author Hristo Klechorov
 */
class SettingsActivity : BaseActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var saveButton: Button
    private var userType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivitySettingsBinding>(this, R.layout.activity_settings)
        binding.lifecycleOwner = this
        saveButton = findViewById(R.id.save_btn)

        saveButton()
        getUserInformation(binding)
        showStudentProperties()
        langBtn()
        signOutButton()
        deleteAccount()
    }

    /**
     * This method gets the user's information from [SettingsViewModel] and binds them to the [SettingsActivity] layout.
     * @param binding The binding parameter of the layout
     */
    private fun getUserInformation(binding: ActivitySettingsBinding) {
        settingsViewModel.getEmployeeUser().observe(this, Observer<Employee> {
            if (it != null) {
                userType = 1
                binding.firstName = it.firstName
                binding.lastName = it.lastName
            }
        })
        settingsViewModel.getStudentUser().observe(this, Observer<Student> {
            if (it != null) {
                userType = 2
                binding.firstName = it.firstName
                binding.lastName = it.lastName
                binding.mNumber = it.matriculationNumber
            }
        })
        settingsViewModel.getAdministratorUser().observe(this, Observer<Administrator> {
            if (it != null) {
                userType = 3
                binding.firstName = it.firstName
                binding.lastName = it.lastName
            }
        })
    }

    /**
     * Makes the additional fields for student visible,in case a student user
     * goes to settings screen.
     */
    private fun showStudentProperties() {
        val caller = intent.getStringExtra("caller")
        if (caller == "edu.kit.pse.a1sicht.ui.student.HomeStudentActivity") {
            mn_et_settings.visibility = View.VISIBLE
            mn_text_view_settings.visibility = View.VISIBLE
        } else {
            mn_et_settings.visibility = View.GONE
            mn_text_view_settings.visibility = View.GONE
        }
    }


    /**
     * The buttons for changing the language in the app.
     * They are using the open library LocaleHelper
     * to set the new locale and save it in the
     * preferences.
     */
    private fun langBtn() {
        de_btn.setOnClickListener {
            updateLocale(Locale.GERMAN)
        }
        gb_btn.setOnClickListener {
            updateLocale(Locale.ENGLISH)
        }
    }

    /**
     * This method determines what to happen, when the sign out button is clicked.
     */
    private fun signOutButton() {
        sign_out_real_btn.setOnClickListener {
            signOut()
        }
    }

    /**
     * This method updates the information about the user when save button is clicked.
     */
    private fun saveButton() {
        save_btn.setOnClickListener {
            if (settingsViewModel.getEmployeeUser().value != null) {
                settingsViewModel.setLastName(last_name_edit_text.text.toString(), 1)
                settingsViewModel.setFirstName(name_edit_text.text.toString(), 1)
            }
            if (settingsViewModel.getStudentUser().value != null) {
                settingsViewModel.setLastName(last_name_edit_text.text.toString(), 2)
                settingsViewModel.setFirstName(name_edit_text.text.toString(), 2)
                if (!settingsViewModel.setMatriculationNumber(Integer.parseInt(mn_et_settings.text.toString()))) {
                    Toast.makeText(this,getString(R.string.settingsToast2),Toast.LENGTH_LONG).show()
                }
            }
            if (settingsViewModel.getAdministratorUser().value != null) {
                settingsViewModel.setLastName(last_name_edit_text.text.toString(), 3)
                settingsViewModel.setFirstName(name_edit_text.text.toString(), 3)
            }
            Toast.makeText(this,getString(R.string.settingsToast),Toast.LENGTH_LONG).show()
        }
    }

    /**
     * This method
     */
    private fun deleteAccount(){
        delete_acc_btn.setOnClickListener {
            settingsViewModel.deleteUser(userType)
            signOut()
        }
    }
    /**
     * This method signs out the current logged in user and deletes the current information for the user in
     * the local database.
     */
    private fun signOut() {

        //Go back to log in screen
        startActivity(Intent(this, LogInActivity::class.java))

        //Log out from google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso).signOut()
        FirebaseAuth.getInstance().signOut()

        //Delete the user's information in the local database.
        settingsViewModel.deleteUserInformation()
    }
}
