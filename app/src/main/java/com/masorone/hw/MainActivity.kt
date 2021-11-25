package com.masorone.hw

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var agreementTextView: TextView
    private lateinit var icImageView: ImageView

    private companion object {
        const val URL =
            "https://zavistnik.com/wp-content/uploads/2020/03/Android-kursy-zastavka.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initImageView()
    }

    private fun initImageView() {
        icImageView = findViewById(R.id.iconImageView)

        Picasso.get().load("https://images4.alphacoders.com/109/1095230.jpg").centerCrop()
            .resize(720, 1280)
            .placeholder(android.R.drawable.ic_media_pause)
            .error(android.R.drawable.ic_dialog_alert)
            .into(icImageView)
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

fun ImageView.loadPicasso(url: String) {
    Picasso.get().load(url).centerCrop()
        .resize(720, 1280)
        .placeholder(android.R.drawable.ic_media_pause)
        .error(android.R.drawable.ic_dialog_alert)
        .into(this)
}

fun TextView.setColor(@ColorRes colorResId: Int, theme: Resources.Theme? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(resources.getColor(colorResId, theme))
    } else {
        setTextColor(resources.getColor(colorResId))
    }
}