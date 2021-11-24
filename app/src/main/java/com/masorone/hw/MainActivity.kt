package com.masorone.hw

import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.widget.TextView
import androidx.annotation.ColorRes

class MainActivity : AppCompatActivity() {

    private lateinit var agreementTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        agreementTextView = findViewById(R.id.agreementTextView)
        val fullText = getString(R.string.agreement_full_text)
        val confidential = getString(R.string.confidential_info)
        val policy = getString(R.string.privacy_policy)
        val spannableString = SpannableString(fullText)


    }
}

fun TextView.setColor(@ColorRes colorResId: Int, theme: Resources.Theme? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(resources.getColor(colorResId, theme))
    } else {
        setTextColor(resources.getColor(colorResId))
    }
}