package com.example.movieapp.feature.Onboarding1

sealed interface Onboarding1Intent
{
    data object Next : Onboarding1Intent
    data object Skip : Onboarding1Intent
}