/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                'purple-dark': '#8C7CFA',
                'purple-light': '#BAA8FA',
            },
        },
    },
    daisyui: {
        themes: [
            {
                light: {
                    "primary": "#A688FA",   // Purple
                    "secondary": "#EFEFEF", // Light Gray
                    "accent": "#000000",    // Black
                    "neutral": "#FFFFFF",   // White
                    "base-100": "#F5F5F5",  // Lighter Gray
                    "base-200": "#DFDFDF",  // Lighter Gray
                    "info": "#2196F3",      // Blue
                    "success": "#4CAF50",   // Green
                    "error": "#F44336",     // Red
                    "warning": "#FF9800",   // Orange
                },
                dark: {
                    "primary": "#A688FA",   // Purple
                    "secondary": "#282828", // Dark Gray
                    "accent": "#FFFFFF",    // White
                    "neutral": "#121212",   // Very Dark Gray
                    "base-100": "#161616",  // Even Darker Gray
                    "base-200": "#292929",  // Black
                    "info": "#BBDEFB",      // Light Blue
                    "success": "#C8E6C9",   // Light Green
                    "error": "#FFCDD2",     // Light Red
                    "warning": "#FFCC80",   // Light Orange
                },
            },
        ],
    },
    plugins: [
        require("daisyui")
    ],
}