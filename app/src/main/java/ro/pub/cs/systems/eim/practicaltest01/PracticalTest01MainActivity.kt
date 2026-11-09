package ro.pub.cs.systems.eim.practicaltest01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {
    private lateinit var leftButton: Button
    private lateinit var  rightButton: Button
    private lateinit var leftEditText: EditText
    private lateinit var rightEditText: EditText
    private lateinit var navigateButton: Button
    private final var THRESHOLD = 5

    private val genericButtonClickListener = GenericButtonClickListener()

    private inner class GenericButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id) {
                R.id.left_button -> {
                    val value = leftEditText.text.toString()
                    val int_val = value.toInt() + 1
                    leftEditText.setText(int_val.toString())

                    if (int_val + rightEditText.text.toString().toInt() > THRESHOLD) {
                        val intent = Intent(this@PracticalTest01MainActivity, PracticalTest01Service::class.java)
                        intent.putExtra("leftVal", int_val)
                        intent.putExtra("rightVal", rightEditText.text.toString().toInt())
                        startService(intent)
                    }
                }
                R.id.right_button -> {
                    val value = rightEditText.text.toString()
                    val int_val = value.toInt() + 1
                    rightEditText.setText(int_val.toString())

                    if (int_val + leftEditText.text.toString().toInt() > THRESHOLD) {
                        val intent = Intent(this@PracticalTest01MainActivity, PracticalTest01Service::class.java)
                        intent.putExtra("leftVal", leftEditText.text.toString().toInt())
                        intent.putExtra("rightVal", int_val)
                        startService(intent)
                    }
                }
                R.id.navigate_button_good -> {
                    val leftCount = leftEditText.text.toString().toInt()
                    val rightCount = rightEditText.text.toString().toInt()
                    val sum = leftCount + rightCount
                    val intent = Intent(this@PracticalTest01MainActivity, PracticalTest01SecondaryActivity::class.java)
                    intent.putExtra("sum", sum.toString())
                    startActivityForResult(intent, 1)

                }
            }
        }
    }

    override fun onDestroy() {
        val intent = Intent(this, PracticalTest01Service::class.java)
        stopService(intent)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)

        leftButton = findViewById<View>(R.id.left_button) as Button
        rightButton = findViewById<View>(R.id.right_button) as Button
        leftEditText = findViewById<View>(R.id.left_text) as EditText
        rightEditText = findViewById<View>(R.id.right_text) as EditText
        navigateButton = findViewById<View>(R.id.navigate_button_good) as Button

        leftButton.setOnClickListener(genericButtonClickListener)
        rightButton.setOnClickListener(genericButtonClickListener)
        navigateButton.setOnClickListener(genericButtonClickListener)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
       if (requestCode == 1) {
           if (resultCode == RESULT_OK) {
               Toast.makeText(this, "Result from secondary activity: OK", Toast.LENGTH_LONG).show()
           } else if (resultCode == RESULT_CANCELED) {
               Toast.makeText(this, "Result from secondary activity: CANCELED", Toast.LENGTH_LONG).show()
           }
       }
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PracticalTest01Broadcast", intent?.getStringExtra("message") ?: "Empty")
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction("ro.pub.cs.systems.eim.action1")
        addAction("ro.pub.cs.systems.eim.action2")
        addAction("ro.pub.cs.systems.eim.action3")
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }
}