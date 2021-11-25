package com.masorone.hw

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.masorone.hw._1_text_lecture.MyClickableSpan
import com.masorone.hw._3_text_input_and_buttons.SimpleTextWatcher
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var agreementTextView: TextView
    private lateinit var icImageView: ImageView
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: TextInputEditText

    private val textWatcher: TextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            val input = s.toString()
            val valid = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
            textInputLayout.isErrorEnabled = !valid
            val error = if (valid) "" else getString(R.string.invalid_email_message)
            textInputLayout.error = error
            if (valid)
                Toast.makeText(
                    this@MainActivity,
                    R.string.valid_email_message,
                    Toast.LENGTH_LONG
                ).show()

            if (input.endsWith("@g")) {
                val fullMail = "${input}mail.com"
                setText(fullMail)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTextInputAndButtons()
    }

    private fun setText(text: String) {
        textInputEditText.removeTextChangedListener(textWatcher)
        textInputEditText.setTextCorrectly(text)
        textInputEditText.addTextChangedListener(textWatcher)
    }

    private companion object {
        const val URL =
            "https://zavistnik.com/wp-content/uploads/2020/03/Android-kursy-zastavka.jpg"
    }

    private fun initTextInputAndButtons() {
        textInputLayout = findViewById(R.id.textInputLayout)
        textInputEditText = textInputLayout.editText as TextInputEditText

        textInputEditText.addTextChangedListener(textWatcher)
    }

    private fun initImageView() {
//        icImageView = findViewById(R.id.iconImageView)
        icImageView.loadWithGlide(URL)
    }

    private fun initTextView() {
//        agreementTextView = findViewById(R.id.agreementTextView)
        val fullText = getString(R.string.agreement_full_text)
        val confidential = getString(R.string.confidential_info)
        val policy = getString(R.string.privacy_policy)
        val spannableString = SpannableString(fullText)

        val confidentialClickable = MyClickableSpan {
            Snackbar.make(it, "Go to link 1", Snackbar.LENGTH_SHORT).show()
        }

        val policyClickable = MyClickableSpan {
            Snackbar.make(it, "Go to link 2", Snackbar.LENGTH_SHORT).show()
        }

        spannableString.setSpan(
            confidentialClickable,
            fullText.indexOf(confidential),
            fullText.indexOf(confidential) + confidential.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            policyClickable,
            fullText.indexOf(policy),
            fullText.indexOf(policy) + policy.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        agreementTextView.run {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.parseColor("#ACACAC")
        }
    }
}

fun TextInputEditText.setTextCorrectly(text: CharSequence) {
    setText(text)
    setSelection(text.length)
}

fun ImageView.loadWithPicasso(url: String) {
    Picasso.get().load(url).centerInside()
        .resize(720, 1280)
        .placeholder(android.R.drawable.ic_media_pause)
        .error(android.R.drawable.ic_dialog_alert)
        .into(this)
}

fun ImageView.loadWithGlide(url: String) {
    Glide.with(this).load(url).centerInside()
        .apply(RequestOptions().override(720, 1280))
        .placeholder(android.R.drawable.ic_media_pause)
        .error(android.R.drawable.ic_dialog_alert)
        .into(this);
}

fun TextView.setColor(@ColorRes colorResId: Int, theme: Resources.Theme? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(resources.getColor(colorResId, theme))
    } else {
        setTextColor(resources.getColor(colorResId))
    }
}