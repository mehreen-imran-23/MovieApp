package com.example.movieapp.feature.Activitiy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movieapp.databinding.SignupSuccessBinding
import com.example.movieapp.feature.afterSucessProfile.ProfileViewModel
import com.example.movieapp.feature.afterSucessProfile.profileBind
import com.example.movieapp.feature.profile.ProfileEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        val binding = SignupSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileBind(
            binding = binding,
            viewModel = viewModel,
            lifecycleOwner = this,
        )
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        ProfileEvent.NavigateToHome -> {
                            openHome()
                        }
                    }
                }
            }
        }
    }

    private fun openHome() {
        val intent = Intent(
            this,
            HomeActivity::class.java,
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
    }
}