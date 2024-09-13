package com.example.cravequest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cravequest.databinding.ActivityMainBinding
import com.example.cravequest.ui.SignInActivity
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = supportFragmentManager

        val paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding())
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(binding.frameLayout.id, paperOnboardingFragment)
        fragmentTransaction.commit()

        paperOnboardingFragment.setOnRightOutListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        addLoginSignupText()
    }

    private fun getDataForOnboarding(): ArrayList<PaperOnboardingPage> {
        val source = PaperOnboardingPage(
            "Welcome", "Your Gateway to Deliciousness!", Color.parseColor("#ffb174"),
            R.drawable.ic_onboarding, R.drawable.ic_search
        )
        val source1 = PaperOnboardingPage(
            "How It Works", "Ordering food has never been this easy!", Color.parseColor("#22eaaa"),
            R.drawable.ic_onboarding2, R.drawable.ic_logo
        )

        val elements = ArrayList<PaperOnboardingPage>()
        elements.add(source)
        elements.add(source1)

        return elements
    }

    private fun addLoginSignupText() {
        val textView = TextView(this).apply {
            text = "Slide right to login/signup"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
            bottomMargin = 50 // Adjust as needed
        }

        binding.frameLayout.addView(textView, layoutParams)
    }
}
