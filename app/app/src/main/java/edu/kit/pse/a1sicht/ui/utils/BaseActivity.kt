package edu.kit.pse.a1sicht.ui.utils

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

/**
 * Extends the LocaleAwareCompatActivity from the
 * LocaleHelper library in order to use it's functionality
 *
 * @author Hristo Klechorov
 */
abstract class BaseActivity: LocaleAwareCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}