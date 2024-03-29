package com.cezma.app.utiles

sealed class ViewState {
    object Loading : ViewState()
    object Success : ViewState()
    data class Error(val message: String) : ViewState()
    object NoConnection : ViewState()
    object Empty : ViewState()
    object LastPage : ViewState()
}
