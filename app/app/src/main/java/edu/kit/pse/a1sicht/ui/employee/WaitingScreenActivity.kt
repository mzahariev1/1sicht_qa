package edu.kit.pse.a1sicht.ui.employee

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.ui.shared.LogInActivity
import edu.kit.pse.a1sicht.ui.utils.BaseActivity

/**
 * The Activity class for the waiting screen that a user sees while
 * waiting for employee confirmation from admin
 *
 * @author Hristo Klechorov, Tihomir Georgiev
 */
class WaitingScreenActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        //Go back to log in screen
        startActivity(Intent(this, LogInActivity::class.java))

        //Log out from google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso).signOut()
        FirebaseAuth.getInstance().signOut()

    }
}