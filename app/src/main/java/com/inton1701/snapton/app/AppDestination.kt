package com.inton1701.snapton.app

enum class AppDestination(
    val label: String,
    val isPrimary: Boolean = false,
) {
    Home("Home"),
    Library("Library"),
    Scan("Scan", isPrimary = true),
    Tools("Tools"),
    Settings("Settings"),
}
