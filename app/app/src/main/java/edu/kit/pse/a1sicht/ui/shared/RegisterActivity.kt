package edu.kit.pse.a1sicht.ui.shared

import android.content.Context
import android.content.Intent
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.employee.HomeEmployeeActivity
import edu.kit.pse.a1sicht.ui.employee.WaitingScreenActivity
import edu.kit.pse.a1sicht.ui.student.HomeStudentActivity
import kotlinx.android.synthetic.main.activity_register.*

/**
 * The activity for the registration form of the application.
 *
 * @author Tihomir Georgiev
 */
class RegisterActivity : BaseActivity() {

    private lateinit var editTextMNumber: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var textViewMNumber: TextView
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val w: Window = window
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR)

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        editTextMNumber = findViewById(R.id.m_number)
        textViewMNumber = findViewById(R.id.m_number_text)
        studentProperties()
        confirm_button.setOnClickListener {
            confirmButton()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //Log out from google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso).signOut()
        FirebaseAuth.getInstance().signOut()
    }
    /**
     * Shows the additional information, when student button is checked, hides it when it is not.
     */
    private fun studentProperties() {
        radioGroup = findViewById(R.id.myRadioGroup)
        radioGroup.setOnCheckedChangeListener { _, i ->
            if (R.id.empbtn == i) {
                editTextMNumber.visibility = View.GONE
                textViewMNumber.visibility = View.GONE
            }
            if (R.id.stdbtn == i) {
                editTextMNumber.visibility = View.VISIBLE
                textViewMNumber.visibility = View.VISIBLE
            }
        }
    }

    /**
     * This method starts a new activity, based on what radio button
     * is selected on the screen.If the student radio button is checked,
     * there is additional information that need to be filled before
     * confirmation.
     */
    private fun confirmButton() {
        radioGroup = findViewById(R.id.myRadioGroup)
        when {
            radioGroup.checkedRadioButtonId == R.id.empbtn -> {
                registerEmployeeUser(1,null)
                toastMessage(this, R.string.toast_register_2)
                startActivity(Intent(this, WaitingScreenActivity::class.java))
            }
            radioGroup.checkedRadioButtonId == R.id.stdbtn -> {
                if (TextUtils.isEmpty(editTextMNumber.text)) {
                    toastMessage(this, R.string.toast_register_3)
                } else {
                    registerEmployeeUser(2,Integer.valueOf(editTextMNumber.text.toString()))
                    toastMessage(this, R.string.toast_register_2)
                    val extras = Bundle()
                    extras.putString("caller", "Register")
                    val intent = Intent(this, HomeStudentActivity::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)
                }
            }
            else -> toastMessage(this, R.string.toast_register_1)
        }
    }


    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun registerEmployeeUser(option: Int, m_number: Int?) {
        val bundle = intent.extras
        val firstName = bundle.getString("First_name")!!
        val lastName = bundle.getString("Last_name")!!
        val googleId = bundle.getString("googleID")!!

        if (option == 1) registerViewModel.createEmployee(googleId, firstName, lastName)
        else registerViewModel.createStudent(googleId, firstName, lastName, m_number!!)
    }

    /**
     * Toast a new message.
     * @param context The context in that the toast must be shown
     * @param int The message on the toast that need to be shown
     */
    private fun toastMessage(context: Context, int: Int) {
        Toast.makeText(
            context, int, Toast.LENGTH_SHORT
        ).show()
    }

}
