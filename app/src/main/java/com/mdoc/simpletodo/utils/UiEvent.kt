package com.mdoc.simpletodo.utils

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(val msg: String, val action: String? = null): UiEvent()
}
