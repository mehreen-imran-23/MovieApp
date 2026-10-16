package com.example.movieapp.unit_testing

import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.AuthRepository
import com.example.movieapp.data.repository.AuthResult
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthEvent
import com.example.movieapp.feature.auth.AuthIntent
import com.example.movieapp.feature.auth.AuthUiState
import com.example.movieapp.feature.auth.AuthViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: AuthRepository
    private lateinit var preferences: LocalPreferences
    private lateinit var viewModel: AuthViewModel
    private val genres = setOf(12, 35)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher) //test dispatcher for viewmodel coroutines
        repository = mockk()
        preferences = mockk(relaxed = true)

        every { preferences.selectedGenreIds } returns flowOf(genres)
        viewModel = AuthViewModel(AuthEnum.SignUp, repository, preferences)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain() //reset main dispatcher
    }

    private fun enterDetails(
        email: String = "mehreen@gmail.com", password: String = "Mehreen123!"
    ) {
        viewModel.onIntent(AuthIntent.EmailChanged(email))
        viewModel.onIntent(AuthIntent.PasswordChanged(password))
    }

    private fun TestScope.collectEvents(): MutableList<AuthEvent> {
        val events = mutableListOf<AuthEvent>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { events.add(it) } // save events to check navigation
        }

        return events
    }

    @Test
    fun `form is empty at start`() {
        assertEquals(AuthUiState(), viewModel.uiState.value)
    }

    @Test
    fun `empty fields show errors`() {
        viewModel.onIntent(AuthIntent.AuthClicked)

        assertEquals(
            R.string.email_req,
            viewModel.uiState.value.email.errorMsg
        )
        assertEquals(
            R.string.pass_req,
            viewModel.uiState.value.password.errorMsg
        )

        coVerify(exactly = 0) {
            repository.signUp(any(), any(), any())
        }
    }

    @Test
    fun `email with only spaces is invalid`() {
        enterDetails(email = "   ")
        viewModel.onIntent(AuthIntent.AuthClicked)

        assertEquals(
            R.string.email_req,
            viewModel.uiState.value.email.errorMsg
        )
        assertEquals("", viewModel.uiState.value.email.input)
    }

    @Test
    fun `wrong email shows error`() {
        val wrongEmails = listOf(
            "mehreen", "mehreen@", "@gmail.com",
            "mehreen@gmail", "mehreen name@gmail.com"
        )

        wrongEmails.forEach { email ->
            enterDetails(email = email)
            viewModel.onIntent(AuthIntent.AuthClicked)

            assertEquals(
                "failed for email: $email", R.string.invalid_email,
                viewModel.uiState.value.email.errorMsg
            )
        }
        coVerify(exactly = 0) {
            repository.signUp(any(), any(), any())
        }
    }

    @Test
    fun `wrong password shows error`() {
        val wrongPasswords = listOf(
            "abcd123", "abcd123!", "12345",
            "Abcd123"
        )
        wrongPasswords.forEach { password ->
            enterDetails(password = password)
            viewModel.onIntent(AuthIntent.AuthClicked)

            assertEquals(
                "Failed for password: $password",
                R.string.valid_pass,
                viewModel.uiState.value.password.errorMsg
            )
        }
        coVerify(exactly = 0) {
            repository.signUp(any(), any(), any())
        }
    }

    @Test
    fun `errors clear after typing`() {
        viewModel.onIntent(AuthIntent.AuthClicked)
        viewModel.onIntent(AuthIntent.EmailChanged("mehreen@gmail.com"))

        assertNull(viewModel.uiState.value.email.errorMsg)
        assertEquals(
            R.string.pass_req,
            viewModel.uiState.value.password.errorMsg
        )

        viewModel.onIntent(AuthIntent.PasswordChanged("Mehreen123!"))
        assertNull(viewModel.uiState.value.password.errorMsg)
    }

    @Test
    fun `signup works`() = runTest(dispatcher) {
        coEvery {
            repository.signUp(any(), any(), any())
        } returns AuthResult.Success(42L, "mehreen@gmail.com")

        val events = collectEvents()

        enterDetails(email = "  mehreen@gmail.com  ")
        viewModel.onIntent(AuthIntent.AuthClicked)
        advanceUntilIdle() //wait for signup and session saving to finish

        coVerify(exactly = 1) {
            repository.signUp(
                "mehreen@gmail.com",
                "Mehreen123!",
                genres
            )
        }

        coVerify(exactly = 1) {
            preferences.saveUserSession(42L)
        }
        coVerify(exactly = 0) {
            repository.signIn(any(), any())
        }
        assertEquals(
            listOf(AuthEvent.NavigateToProfile),
            events
        )
        assertEquals(AuthUiState(), viewModel.uiState.value)
    }

    @Test
    fun `existing email shows error`() = runTest(dispatcher) {
        coEvery {
            repository.signUp(any(), any(), any())
        } returns AuthResult.EmailAlreadyExists

        enterDetails()
        viewModel.onIntent(AuthIntent.AuthClicked)
        advanceUntilIdle()

        assertEquals(
            R.string.email_already_exists,
            viewModel.uiState.value.AuthError
        )
        assertEquals(
            "mehreen@gmail.com",
            viewModel.uiState.value.email.input
        )
        assertEquals(
            "Mehreen123!",
            viewModel.uiState.value.password.input
        )
        assertFalse(viewModel.uiState.value.button.isLoading)

        coVerify(exactly = 0) {
            preferences.saveUserSession(any())
        }

        viewModel.onIntent(AuthIntent.EmailChanged("new@gmail.com"))
        assertNull(viewModel.uiState.value.AuthError)
    }

    @Test
    fun `database error is handled`() = runTest(dispatcher) {
        coEvery {
            repository.signUp(any(), any(), any())
        } throws IllegalStateException("Database error")

        enterDetails()
        viewModel.onIntent(AuthIntent.AuthClicked)
        advanceUntilIdle()

        assertEquals(
            R.string.auth_failed,
            viewModel.uiState.value.AuthError
        )
        assertFalse(viewModel.uiState.value.button.isLoading)

        coVerify(exactly = 0) {
            preferences.saveUserSession(any())
        }
    }

    @Test
    fun `session error is handled`() = runTest(dispatcher) {
        coEvery {
            repository.signUp(any(), any(), any())
        } returns AuthResult.Success(42L, "mehreen@gmail.com")

        coEvery {
            preferences.saveUserSession(any())
        } throws IllegalStateException("Storage error")

        val events = collectEvents()

        enterDetails()
        viewModel.onIntent(AuthIntent.AuthClicked)
        advanceUntilIdle()

        assertEquals(R.string.auth_failed, viewModel.uiState.value.AuthError)
        assertFalse(viewModel.uiState.value.button.isLoading)
        assertTrue(events.isEmpty()) //here it should not navigate if session was not saved
    }

    @Test
    fun `signup is called only once`() = runTest(dispatcher) {
        val result = CompletableDeferred<AuthResult>() // keep signup pending for now

        coEvery {
            repository.signUp(any(), any(), any())
        } coAnswers
                { result.await() }

        val events = collectEvents()

        enterDetails()
        viewModel.onIntent(AuthIntent.AuthClicked)
        runCurrent() //start signup but don't finish it yet

        assertTrue(viewModel.uiState.value.button.isLoading)

        viewModel.onIntent(AuthIntent.AuthClicked)
        viewModel.onIntent(
            AuthIntent.EmailChanged("new@gmail.com")
        )
        viewModel.onIntent(
            AuthIntent.PasswordChanged("NewPass123!")
        )
        viewModel.onIntent(AuthIntent.SignInClicked)

        assertEquals(
            "mehreen@gmail.com",
            viewModel.uiState.value.email.input
        )
        assertEquals(
            "Mehreen123!",
            viewModel.uiState.value.password.input
        )
        assertTrue(events.isEmpty())

        result.complete(AuthResult.EmailAlreadyExists) // finish the pending signup
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.signUp(any(), any(), any())
        }

        assertFalse(viewModel.uiState.value.button.isLoading)
    }

    @Test
    fun `sign in button opens sign in`() = runTest(dispatcher) {
        val events = collectEvents()

        enterDetails()
        viewModel.onIntent(AuthIntent.SignInClicked)
        advanceUntilIdle()

        assertEquals(AuthUiState(), viewModel.uiState.value)
        assertEquals(listOf(AuthEvent.NavigateToSignIn), events)
    }

    @Test
    fun `skip button opens home`() = runTest(dispatcher) {
        val events = collectEvents()

        viewModel.onIntent(AuthIntent.SkipClicked)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            preferences.saveGuest()
        }
        coVerify(exactly = 0) {
            repository.signUp(any(), any(), any())
        }
        assertEquals(
            listOf(AuthEvent.NavigateToHome),
            events
        )
        assertFalse(viewModel.uiState.value.button.isLoading)
    }
}
