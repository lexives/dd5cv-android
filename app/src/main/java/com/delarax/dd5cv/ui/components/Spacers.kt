package com.delarax.dd5cv.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.delarax.dd5cv.ui.theme.Dimens

object HorizontalSpacer {
    @Composable
    fun None() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.none))
    }
    @Composable
    fun XXSmall() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.xxs))
    }
    @Composable
    fun XSmall() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.xs))
    }
    @Composable
    fun Small() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.sm))
    }
    @Composable
    fun Medium() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.md))
    }
    @Composable
    fun Large() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.lg))
    }
    @Composable
    fun XLarge() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.xl))
    }
    @Composable
    fun XXLarge() {
        Spacer(modifier = Modifier.height(Dimens.Spacing.xxl))
    }
}

object VerticalSpacer {
    @Composable
    fun None() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.none))
    }
    @Composable
    fun XXSmall() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.xxs))
    }
    @Composable
    fun XSmall() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.xs))
    }
    @Composable
    fun Small() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.sm))
    }
    @Composable
    fun Medium() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.md))
    }
    @Composable
    fun Large() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.lg))
    }
    @Composable
    fun XLarge() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.xl))
    }
    @Composable
    fun XXLarge() {
        Spacer(modifier = Modifier.width(Dimens.Spacing.xxl))
    }
}