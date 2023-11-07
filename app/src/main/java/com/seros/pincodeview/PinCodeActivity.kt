package com.seros.pincodeview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.seros.pincodeview.databinding.ActivityPinCodeBinding

@Suppress("DEPRECATION")
class PinCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinCodeBinding
    private var currentInputIndex = 0 // Индекс текущего ввода
    private val pinCode = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createEmptyCircles(4)

        binding.apply {
            btn1.setOnClickListener {
                handlePinInput("1")
            }
            btn2.setOnClickListener {
                handlePinInput("2")
            }
            btn3.setOnClickListener {
                handlePinInput("3")
            }
            btn4.setOnClickListener {
                handlePinInput("4")
            }
            btn5.setOnClickListener {
                handlePinInput("5")
            }
            btn6.setOnClickListener {
                handlePinInput("6")
            }
            btn7.setOnClickListener {
                handlePinInput("7")
            }
            btn8.setOnClickListener {
                handlePinInput("8")
            }
            btn9.setOnClickListener {
                handlePinInput("9")
            }
            btn0.setOnClickListener {
                handlePinInput("0")
            }
            btnDelete.setOnClickListener {
                if (currentInputIndex > 0) {
                    currentInputIndex--
                    clearCircle(currentInputIndex)
                }
            }
            btnForgotPassword.setOnClickListener {
                Log.d("PinCode", "Actual Pin: $pinCode")
            }

            // Добавь обработку для других кнопок аналогичным образом
        }
    }

    private fun handlePinInput(input: String) {
        if (currentInputIndex < 4) { // Убедимся, что не введено больше 4 цифр
            fillCircle(currentInputIndex) // Заполняем кружок, чтобы показать введенную цифру
            currentInputIndex++
            binding.pinLayout.getChildAt(currentInputIndex - 1).tag = input // Устанавливаем введенную цифру в тег кружка

            if (currentInputIndex == 4) {
                val enteredPin = getEnteredPin()
                if (enteredPin == pinCode) {
                    // Пин-код верный, выполните необходимые действия, например, перейдите на следующий экран
                    // Здесь можно добавить свой код для обработки успешного ввода пин-кода
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
                    clearAllCircles() // Очистим кружки после успешной проверки
                } else {
                    // Пин-код неверный, выполните необходимые действия, например, покажите сообщение об ошибке
                    // Здесь можно добавить свой код для обработки неверного пин-кода
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

                    clearAllCircles() // Очистим кружки после неудачной проверки
                }
            }
        }
    }

    private fun getEnteredPin(): String {
        val enteredPin = StringBuilder()
        for (i in 0 until 4) {
            enteredPin.append(binding.pinLayout.getChildAt(i).tag.toString())
        }
        return enteredPin.toString()
    }

    private fun clearAllCircles() {
        val pinLayout = findViewById<LinearLayout>(R.id.pinLayout)
        for (i in 0 until pinLayout.childCount) {
            clearCircle(i)
        }
        currentInputIndex = 0
    }

    private fun createEmptyCircles(count: Int) {
        val pinLayout = findViewById<LinearLayout>(R.id.pinLayout)
        pinLayout.removeAllViews() // Убедимся, что не останется предыдущих кружочков

        for (i in 0 until count) {
            val circle = ImageView(this)
            circle.setImageResource(R.drawable.empty_circle)
            val layoutParams = LinearLayout.LayoutParams(48, 48)
            layoutParams.setMargins(24, 0, 24, 0) // Расстояние между кружочками
            circle.layoutParams = layoutParams
            pinLayout.addView(circle)
        }
    }

    private fun fillCircle(index: Int) {
        val pinLayout = findViewById<LinearLayout>(R.id.pinLayout)
        if (index >= 0 && index < pinLayout.childCount) {
            val circle = pinLayout.getChildAt(index) as ImageView
            circle.setImageResource(R.drawable.filled_circle) // Ресурс для заполненного кружочка
        }
    }

    private fun clearCircle(index: Int) {
        val pinLayout = findViewById<LinearLayout>(R.id.pinLayout)
        if (index >= 0 && index < pinLayout.childCount) {
            val circle = pinLayout.getChildAt(index) as ImageView
            circle.setImageResource(R.drawable.empty_circle) // Вернуть к пустому кружочку
        }
    }
}
