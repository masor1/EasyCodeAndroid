package com.masorone.hw

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private lateinit var loginButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var textCheckBox: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: ViewGroup
    private companion object {
        private const val TAG = "MainActivity"
        const val INITIAL = 0
        const val PROGRESS = 1
        const val SUCCESS = 2
        const val FAILED = 3
        const val URL =
            "https://zavistnik.com/wp-content/uploads/2020/03/Android-kursy-zastavka.jpg"
    }
    private var state = INITIAL
    private val textWatcher: TextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            Log.d(TAG, "changed -> ${s.toString()}")
            textInputLayout.isErrorEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            state = SUCCESS
        Log.d(TAG, "onCreate -> ${savedInstanceState == null}\nstate -> $state")
        initCheckBox()
    }

    private fun setText(text: String) {
        textInputEditText.removeTextChangedListener(textWatcher)
        textInputEditText.setTextCorrectly(text)
        textInputEditText.addTextChangedListener(textWatcher)
    }

    private fun initCheckBox() {
        checkBox = findViewById(R.id.checkBox)
        loginButton = findViewById(R.id.loginButton)
        textCheckBox = findViewById(R.id.textCheckBox)
        progressBar = findViewById(R.id.progressBar)
        textInputEditText = findViewById(R.id.textInputEditText)
        textInputLayout = findViewById(R.id.textInputLayout)
        contentLayout = findViewById(R.id.contentLayout)

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

        textCheckBox.run {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.parseColor("#ACACAC")
        }

        loginButton.isEnabled = false

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            loginButton.isEnabled = isChecked
        }

        loginButton.setOnClickListener {
            if (EMAIL_ADDRESS.matcher(textInputEditText.text.toString()).matches()) {
                hideKeyboard(textInputEditText)
                contentLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                state = PROGRESS
                Handler(Looper.myLooper()!!).postDelayed({
                    state = FAILED
                    contentLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    val dialog = Dialog(this)
                    val view = LayoutInflater.from(this).inflate(R.layout.dialog, contentLayout, false)
                    dialog.setCancelable(false)
                    view.findViewById<View>(R.id.closeButton).setOnClickListener {
                        state = INITIAL
                        dialog.dismiss()
                    }
                    dialog.setContentView(view)
                    dialog.show()
                }, 3000)

                Snackbar.make(loginButton, "Go to postLogin", Snackbar.LENGTH_LONG).show()
            } else {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = getString(R.string.invalid_email_message)
            }
        }
    }

    private fun initTextInputAndButtons() {
        textInputLayout = findViewById(R.id.textInputLayout)
        textInputEditText = textInputLayout.editText as TextInputEditText
        loginButton = findViewById(R.id.loginButton)

        textInputEditText.listenChanges {
            textInputLayout.isErrorEnabled = false
            loginButton.isEnabled = true
        }

        loginButton.setOnClickListener {
            if (EMAIL_ADDRESS.matcher(textInputEditText.text.toString()).matches()){
                hideKeyboard(textInputEditText)
                loginButton.isEnabled = false
                Snackbar.make(loginButton, "Go to postLogin", Snackbar.LENGTH_LONG).show()
            } else {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = getString(R.string.invalid_email_message)
            }
        }
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        textInputEditText.removeTextChangedListener(textWatcher)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        textInputEditText.addTextChangedListener(textWatcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

fun AppCompatActivity.hideKeyboard(view: View) {
    val imm = this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextInputEditText.listenChanges(block: (text: String) -> Unit) {
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            block.invoke(s.toString())
        }
    })
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