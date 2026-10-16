package com.example.movieapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.ui.theme.Divider
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextStyles

@Composable
fun SocialSignInRow(
    @DrawableRes facebookIcon: Int,
    @DrawableRes googleIcon: Int,
    @DrawableRes appleIcon: Int,
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Divider,
            )

            Text(
                text = stringResource(R.string.or),
                style = TextStyles.AuthBody,
                color = Text2,
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Divider,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            SocialButton(
                icon = facebookIcon,
                description = stringResource(R.string.facebook),
                onClick = onFacebookClick,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )

            SocialButton(
                icon = googleIcon,
                description = stringResource(R.string.google),
                onClick = onGoogleClick,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )

            SocialButton(
                icon = appleIcon,
                description = stringResource(R.string.apple),
                onClick = onAppleClick,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SocialButton(
    @DrawableRes icon: Int,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 51.dp),
        shape = RoundedCornerShape(16.dp),
        color = MovieSurface,
        contentColor = MovieWhite,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = description,
                tint = MovieWhite,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}