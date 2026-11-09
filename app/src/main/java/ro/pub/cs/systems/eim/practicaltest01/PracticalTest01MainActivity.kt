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

class PracticalTest01MainActivity : AppCompatActivity() {
    private lateinit var leftButton: Button
    private lateinit var  rightButton: Button
    private lateinit var leftEditText: EditText
    private lateinit var rightEditText: EditText

    private val genericButtonClickListener = GenericButtonClickListener()

    private inner class GenericButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id) {
                R.id.left_button -> {
                    val value = leftEditText.text.toString()
                    val int_val = value.toInt() + 1
                    leftEditText.setText(int_val.toString())
                }
                R.id.right_button -> {
                    val value = rightEditText.text.toString()
                    val int_val = value.toInt() + 1
                    rightEditText.setText(int_val.toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)

        leftButton = findViewById<View>(R.id.left_button) as Button
        rightButton = findViewById<View>(R.id.right_button) as Button
        leftEditText = findViewById<View>(R.id.left_text) as EditText
        rightEditText = findViewById<View>(R.id.right_text) as EditText

        leftButton.setOnClickListener(genericButtonClickListener)
        rightButton.setOnClickListener(genericButtonClickListener)
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("leftEditText", leftEditText.text.toString())
        savedInstanceState.putString("rightEditText", rightEditText.text.toString())
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("loguri", savedInstanceState.getString("leftEditText") ?: "cheia nu exista")
        if (savedInstanceState.containsKey("leftEditText")) {
            leftEditText.setText(savedInstanceState.getString("leftEditText"))
        }
        if (savedInstanceState.containsKey("rightEditText")) {
            rightEditText.setText(savedInstanceState.getString("rightEditText"))
        }
    }
}