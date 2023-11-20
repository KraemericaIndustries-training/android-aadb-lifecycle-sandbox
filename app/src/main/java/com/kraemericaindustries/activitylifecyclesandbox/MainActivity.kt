package com.kraemericaindustries.activitylifecyclesandbox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.kraemericaindustries.activitylifecyclesandbox.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), TestFragment.TestFragmentListener {

    private lateinit var binding: ActivityMainBinding
    private val testFragment = TestFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonExit.setOnClickListener { showDialog() }
        binding.buttonSave.setOnClickListener { saveMessage() }
        binding.buttonShowFragment.setOnClickListener { showFragment() }
        binding.buttonRemoveFragment.setOnClickListener { removeFragment() }
        onBackPressedDispatcher.addCallback { showDialog() }

        binding.textViewSavedMessage.text = savedInstanceState?.getString("savedMessage")
    }

    private fun showFragment() {
        supportFragmentManager.commit {
            add(R.id.fragement_container, testFragment)
        }
    }

    private fun removeFragment() {
        supportFragmentManager.commit {
            remove(testFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val savedTextViewMessage = binding.textViewSavedMessage.text.toString()
        outState.putString("savedMessage", savedTextViewMessage)
    }

    private fun saveMessage() {
        val userMessage = binding.editTextMessage.text
        File(filesDir, "user message.txt").writeText(userMessage.toString())
        binding.textViewSavedMessage.text = "Your message has been saved!\n\nMessage Preview:\n\n$userMessage"
        binding.editTextMessage.setText("")
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("Warning!")
//            .setMessage("You are about to leave the app.  Are you sure you want to exit?")
            .setView(R.layout.dialog_warning)
            .setPositiveButton("Okay") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("More info") { dialog, _ ->
                Toast.makeText(this, "This is where more info could be provided", Toast.LENGTH_LONG)
                    .show()
                dialog.dismiss()
            }
            .show()
    }

    override fun clearActivityScreen() {
        binding.editTextMessage.setText("")
        binding.textViewSavedMessage.text = ""
        removeFragment()
    }
}