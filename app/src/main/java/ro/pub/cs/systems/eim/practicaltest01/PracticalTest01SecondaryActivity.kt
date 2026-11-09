package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity: AppCompatActivity() {
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)

        okButton = findViewById<View>(R.id.ok_button) as Button
        cancelButton = findViewById<View>(R.id.cancel_button) as Button
        editText = findViewById<View>(R.id.secondary_center_edit_text) as EditText

        okButton.setOnClickListener(genericButtonClickListener)
        cancelButton.setOnClickListener(genericButtonClickListener)

        val intentFromParent = intent
        val data = intentFromParent.extras
        editText.setText(data!!.getString("sum"))
    }

    private val genericButtonClickListener = GenericButtonClickListener()

    private inner class GenericButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id) {
                R.id.ok_button -> {
                    setResult(RESULT_OK)
                    finish()
                }
                R.id.cancel_button -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}