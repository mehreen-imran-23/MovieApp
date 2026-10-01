package com.example.movieapp.feature.afterSucessProfile

import com.example.movieapp.feature.profile.ProfileEvent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movieapp.R
import com.example.movieapp.databinding.SignupSuccessBinding
import com.example.movieapp.feature.Activitiy.HomeActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: SignupSuccessBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        binding = SignupSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyInsets()

        binding.continueButton.setOnClickListener {
            viewModel.onIntent(
                ProfileIntent.ContinueClicked(
                    name = binding.nameInput.text?.toString().orEmpty(),
                    phoneNumber = binding.phoneInput.text?.toString().orEmpty(),
                    city = binding.cityInput.text?.toString().orEmpty(),
                )
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        render(state)
                    }
                }

                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            ProfileEvent.NavigateToHome -> {
                                val intent = Intent(
                                    this@ProfileActivity,
                                    HomeActivity::class.java,
                                ).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }

                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun render(state: ProfileUiState) {
        binding.nameLayout.error = state.name.errorMsg?.let {
            getString(it)
        }

        binding.phoneLayout.error = state.phoneNumber.errorMsg?.let {
            getString(it)
        }

        binding.cityLayout.error = state.city.errorMsg?.let {
            getString(it)
        }

        binding.nameInput.isEnabled = !state.isLoading
        binding.phoneInput.isEnabled = !state.isLoading
        binding.cityInput.isEnabled = !state.isLoading
        binding.continueButton.isEnabled = !state.isLoading

        binding.continueButton.setText(
            if (state.isLoading) {
                R.string.profile_saving
            } else {
                R.string.continue_btn
            }
        )

        binding.profileError.text = state.errorMsg?.let {
            getString(it)
        }.orEmpty()

        binding.profileError.visibility = if (state.errorMsg != null) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    private fun applyInsets() {
        val left = binding.root.paddingLeft
        val top = binding.root.paddingTop
        val right = binding.root.paddingRight
        val bottom = binding.root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val safeInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout() or
                        WindowInsetsCompat.Type.ime()
            )

            view.updatePadding(
                left = left + safeInsets.left,
                top = top + safeInsets.top,
                right = right + safeInsets.right,
                bottom = bottom + safeInsets.bottom,
            )

            insets
        }

        ViewCompat.requestApplyInsets(binding.root)
    }
}