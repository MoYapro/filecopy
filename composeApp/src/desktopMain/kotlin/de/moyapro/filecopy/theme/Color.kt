package de.moyapro.filecopy.theme

import androidx.compose.ui.graphics.Color

// Catppuccin Macchiato Palette
val Rosewater = Color(0xFFF4DBD6)
val Flamingo = Color(0xFFF0C6C6)
val Pink = Color(0xFFF5BDE6)
val Mauve = Color(0xFFC6A0F6)
val Red = Color(0xFFED8796)
val Maroon = Color(0xFFEE99A0)
val Peach = Color(0xFFEE99A0) // Using Maroon for Peach as they are close in Macchiato
val Yellow = Color(0xFFEED49F)
val Green = Color(0xFFA6DA95)
val Teal = Color(0xFF8BD5CA)
val Sky = Color(0xFF91D7E3)
val Sapphire = Color(0xFF7DC4E4)
val Blue = Color(0xFF8AADF4)
val Lavender = Color(0xFFB7BDF8)
val Text = Color(0xFFCAD3F5)
val Subtext1 = Color(0xFFB8C0E0)
val Subtext0 = Color(0xFFA5ADCB)
val Overlay2 = Color(0xFF939AB7)
val Overlay1 = Color(0xFF8087A2)
val Overlay0 = Color(0xFF6E738D)
val Surface2 = Color(0xFF5B6078)
val Surface1 = Color(0xFF494D64)
val Surface0 = Color(0xFF363A4F)
val Base = Color(0xFF24273A)
val Mantle = Color(0xFF1E202E)
val Crust = Color(0xFF181926)

// Light Theme (Mapping Macchiato to Material 3 Light roles)
// Catppuccin is primarily a dark theme palette. For a "light" version
// using Macchiato, we invert the usage of background/surface colors,
// and pick lighter accent colors. This might not be a perfect "light"
// experience in the traditional sense, but it adheres to the Macchiato palette.
val md_theme_light_primary = Blue // Or Mauve, Pink, Green depending on desired primary accent
val md_theme_light_onPrimary = Crust
val md_theme_light_primaryContainer = Sky // A lighter accent
val md_theme_light_onPrimaryContainer = Base
val md_theme_light_secondary = Lavender
val md_theme_light_onSecondary = Crust
val md_theme_light_secondaryContainer = Rosewater // A lighter accent
val md_theme_light_onSecondaryContainer = Base
val md_theme_light_tertiary = Green
val md_theme_light_onTertiary = Crust
val md_theme_light_tertiaryContainer = Yellow // A lighter accent
val md_theme_light_onTertiaryContainer = Base
val md_theme_light_error = Red
val md_theme_light_errorContainer = Flamingo
val md_theme_light_onError = Crust
val md_theme_light_onErrorContainer = Base
val md_theme_light_background = Text // Lighter Catppuccin base for background
val md_theme_light_onBackground = Crust // Darker text for light background
val md_theme_light_surface = Text // Lighter Catppuccin base for surface
val md_theme_light_onSurface = Crust // Darker text for light surface
val md_theme_light_surfaceVariant = Subtext0
val md_theme_light_onSurfaceVariant = Crust
val md_theme_light_outline = Overlay1
val md_theme_light_inverseOnSurface = Crust // Inverse of onSurface (for dark theme elements on light background)
val md_theme_light_inverseSurface = Base // Inverse of surface (for dark theme elements on light background)
val md_theme_light_inversePrimary = Blue // Inverse of primary for dark theme
val md_theme_light_shadow = Color(0xFF000000) // Standard black shadow
val md_theme_light_surfaceTint = Blue
val md_theme_light_outlineVariant = Overlay0
val md_theme_light_scrim = Color(0xFF000000)

// Dark Theme (Mapping Macchiato to Material 3 Dark roles)
val md_theme_dark_primary = Blue
val md_theme_dark_onPrimary = Crust
val md_theme_dark_primaryContainer = Sapphire
val md_theme_dark_onPrimaryContainer = Text
val md_theme_dark_secondary = Lavender
val md_theme_dark_onSecondary = Crust
val md_theme_dark_secondaryContainer = Mauve
val md_theme_dark_onSecondaryContainer = Text
val md_theme_dark_tertiary = Green
val md_theme_dark_onTertiary = Crust
val md_theme_dark_tertiaryContainer = Teal
val md_theme_dark_onTertiaryContainer = Text
val md_theme_dark_error = Red
val md_theme_dark_errorContainer = Maroon
val md_theme_dark_onError = Crust
val md_theme_dark_onErrorContainer = Text
val md_theme_dark_background = Base // Main background color
val md_theme_dark_onBackground = Text // Text color on main background
val md_theme_dark_surface = Mantle // Slightly lighter surface than background
val md_theme_dark_onSurface = Text // Text color on surface
val md_theme_dark_surfaceVariant = Surface0 // A slightly darker variant for cards/elements
val md_theme_dark_onSurfaceVariant = Subtext1
val md_theme_dark_outline = Overlay0
val md_theme_dark_inverseOnSurface = Base // Inverse of onSurface (for light theme elements on dark background)
val md_theme_dark_inverseSurface = Text // Inverse of surface (for light theme elements on dark background)
val md_theme_dark_inversePrimary = Blue // Inverse of primary for light theme
val md_theme_dark_shadow = Color(0xFF000000) // Standard black shadow
val md_theme_dark_surfaceTint = Blue
val md_theme_dark_outlineVariant = Surface1
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Blue // You can pick any of the accent colors as the seed. Blue is a good general choice for Catppuccin.
