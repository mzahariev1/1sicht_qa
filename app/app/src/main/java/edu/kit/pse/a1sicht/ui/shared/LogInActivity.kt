package edu.kit.pse.a1sicht.ui.shared

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import edu.kit.pse.a1sicht.R
import kotlinx.android.synthetic.main.activity_log_in.*
import edu.kit.pse.a1sicht.ui.utils.BaseActivity
import android.os.StrictMode
import android.annotation.SuppressLint
import edu.kit.pse.a1sicht.ui.administrator.HomeAdminActivity
import edu.kit.pse.a1sicht.ui.employee.HomeEmployeeActivity
import edu.kit.pse.a1sicht.ui.employee.WaitingScreenActivity
import edu.kit.pse.a1sicht.ui.student.HomeStudentActivity


/**
 * The activity class for the log-in screen of the application.
 *
 * @author Tihomir Georgiev
 * @author Hristo Klechorov
 */
class LogInActivity : BaseActivity() {

    private val RC_SIGN_IN: Int = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var mAuth: FirebaseAuth

    private lateinit var logInViewModel: LogInViewModel

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        logInViewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)

        configureGoogleSignIn()
        setupUI()

        mAuth = FirebaseAuth.getInstance()


    }


    override fun onBackPressed() {
        //Nothing
    }

    /**
     * Makes the connection with the google for the google sign in.
     */
    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(
                getString(
                    R.string.default_web_client_id
                )
            )
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

    }

    /**
     * Checks the current logged in user.
     */
    private fun checkUser() {
        val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)!!
        val extras = Bundle()
        extras.putString("caller", "LogInActivity")
        when (logInViewModel.defineUser(acct.id!!)) {
             0 -> {
                 val intent = Intent(this, RegisterActivity::class.java)
                 extras.putString("First_name", acct.givenName)
                 extras.putString("Last_name", acct.familyName)
                 extras.putString("googleID", acct.id)
                 intent.putExtras(extras)
                 startActivity(intent)
             }
             2 -> {
                 val intent = Intent(this, HomeEmployeeActivity::class.java)
                 intent.putExtras(extras)
                 startActivity(intent)
             }
             4 -> {
                 val intent = Intent(this, WaitingScreenActivity::class.java)
                 intent.putExtras(extras)
                 startActivity(intent)
             }
             3 -> {
                 val intent = Intent(this, HomeAdminActivity::class.java)
                 intent.putExtras(extras)
                 startActivity(intent)
             }
             1 -> {
                 val intent = Intent(this, HomeStudentActivity::class.java)
                 intent.putExtras(extras)
                 startActivity(intent)
             }
             -1 ->{
                 Toast.makeText(this,getString(R.string.no_connection),Toast.LENGTH_LONG).show()
             }
         }
    }

    /**
     * Sets action on a button.
     */
    private fun setupUI() {
        signInBtn.setOnClickListener {
            signIn()
        }
    }

    /**
     * Calls the google activity window for sign in.
     */
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                fbAuthWithGoogle(account)

            } catch (e: ApiException) {
                Toast.makeText(
                    this, e.toString(), Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Authorizes the google account, that is trying to log in, throws an error when unsuccessful.
     */
    private fun fbAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)

        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                checkUser()
                //Go to other activity

            } else {
                Toast.makeText(
                    this, getString(R.string.sign_in_failed), Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
