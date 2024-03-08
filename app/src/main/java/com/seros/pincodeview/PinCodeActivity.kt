package com.seros.pincodeview

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.seros.pincodeview.databinding.ActivityPinCodeBinding

class PinCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinCodeBinding
    private var currentInputIndex = 0
    private lateinit var pinLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pinCode: String
    private lateinit var confirmPinCode: String


    companion object {
        private const val PIN_LENGTH = 4
        private const val CIRCLE_SIZE = 60
        private const val CIRCLE_MARGIN = 24
        private const val PREFS_NAME = "PinCodePrefs"
        private const val PIN_KEY = "pin_key"
        private const val CONFIRM_PIN_KEY = "confirm_pin_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinCodeBinding.inflate(layoutInflater)
        pinLayout = binding.pinLayout
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        pinCode = sharedPreferences.getString(PIN_KEY, "") ?: ""
        confirmPinCode = sharedPreferences.getString(CONFIRM_PIN_KEY, "") ?: ""

        createEmptyCircles(PIN_LENGTH)
        if (pinCode.isEmpty()) {
            setupNewPin()
        } else {
            setupButtonListeners(binding)
        }
    }

    private fun setupButtonListeners(binding: ActivityPinCodeBinding) {
        binding.apply {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
            for (button in buttons) {
                button.setOnClickListener {
                    handlePinInput(button.text.toString())
                }
            }

            btnDelete.setOnClickListener {
                handleDeleteButton()
            }

            btnForgotPassword.setOnClickListener {
                handleForgotPassword()
            }

            btnLoginFinger.setOnClickListener {
                setupNewPin()
                val editor = sharedPreferences.edit()
                editor.putString(PIN_KEY, "")
                editor.putString(CONFIRM_PIN_KEY, "")
            }
        }
    }


    private fun handlePinInput(input: String) {
        if (currentInputIndex < PIN_LENGTH) {
            fillCircle(currentInputIndex)
            currentInputIndex++
            pinLayout.getChildAt(currentInputIndex - 1).tag = input

            if (currentInputIndex == PIN_LENGTH) {
                val enteredPin = getEnteredPin()
                if (enteredPin == pinCode) {
                    Toast.makeText(this, "Добро пожаловать", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                    clearAllCircles()
                } else {
                    Toast.makeText(this, "PIN-код не правильный", Toast.LENGTH_SHORT).show()
                    clearAllCircles()
                }
            }
        }
    }

    private fun setupNewPin() {
        binding.titleTextView.text = "Введите новый PIN-код"

        binding.apply {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
            for (button in buttons) {
                button.setOnClickListener {
                    handleNewPinInput(button.text.toString())
                }
            }
        }
    }


    private fun handleNewPinInput(input: String) {
        if (currentInputIndex < PIN_LENGTH) {
            fillCircle(currentInputIndex)
            currentInputIndex++
            pinLayout.getChildAt(currentInputIndex - 1).tag = input

            if (currentInputIndex == PIN_LENGTH) {
                pinCode = getEnteredPin()
                if (pinCode.isNotEmpty()){
                    saveConfirmPin(pinCode)
                    confirmNewPin()
                    clearAllCircles()
                }
            }
        }
    }

    private fun confirmNewPin() {
        binding.titleTextView.text = "Подтвердите ваш PIN-код"

        binding.apply {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
            for (button in buttons) {
                button.setOnClickListener {
                    handleConfirmPinInput(button.text.toString())
                }
            }
        }
    }

    private fun handleConfirmPinInput(input: String) {
        if (currentInputIndex < PIN_LENGTH) {
            fillCircle(currentInputIndex)
            currentInputIndex++
            pinLayout.getChildAt(currentInputIndex - 1).tag = input

            if (currentInputIndex == PIN_LENGTH) {
                confirmPinCode = getEnteredPin()
                if (pinCode == confirmPinCode){
                    Toast.makeText(this, "Добро пожаловать", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                    savePin(pinCode)
                    clearAllCircles()
                }else {
                    Toast.makeText(this, "PIN-коды не совпадают, начните сначала", Toast.LENGTH_SHORT).show()
                    setupNewPin()
                    clearAllCircles()
                }
            }
        }
    }


    private fun savePin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PIN_KEY, pin)
        editor.apply()
    }

    private fun saveConfirmPin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putString(CONFIRM_PIN_KEY, pin)
        editor.apply()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleDeleteButton() {
        if (currentInputIndex > 0) {
            currentInputIndex--
            clearCircle(currentInputIndex)
        }
    }

    private fun handleForgotPassword() {
        pinCode = sharedPreferences.getString(PIN_KEY, "") ?: ""
        Log.d("PinCode", "Actual Pin: $pinCode")
    }


    private fun getEnteredPin(): String {
        val enteredPin = StringBuilder()
        for (i in 0 until PIN_LENGTH) {
            enteredPin.append(pinLayout.getChildAt(i).tag.toString())
        }
        return enteredPin.toString()
    }

    private fun clearAllCircles() {
        for (i in 0 until pinLayout.childCount) {
            clearCircle(i)
        }
        currentInputIndex = 0
    }

    private fun createEmptyCircles(count: Int) {
        pinLayout.removeAllViews()

        for (i in 0 until count) {
            val circle = ImageView(this)
            circle.setImageResource(R.drawable.empty_circle)
            val layoutParams = LinearLayout.LayoutParams(CIRCLE_SIZE, CIRCLE_SIZE)
            layoutParams.setMargins(CIRCLE_MARGIN, 0, CIRCLE_MARGIN, 0)
            circle.layoutParams = layoutParams
            pinLayout.addView(circle)
        }
    }

    private fun fillCircle(index: Int) {
        if (index >= 0 && index < pinLayout.childCount) {
            val circle = pinLayout.getChildAt(index) as ImageView
            circle.setImageResource(R.drawable.filled_circle)
        }
    }

    private fun clearCircle(index: Int) {
        if (index >= 0 && index < pinLayout.childCount) {
            val circle = pinLayout.getChildAt(index) as ImageView
            circle.setImageResource(R.drawable.empty_circle)
        }
    }
}
