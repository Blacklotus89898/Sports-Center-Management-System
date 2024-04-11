import React, { useState } from "react";
import { useAtom } from "jotai";
import { currentThemeAtom } from "../../../utils/jotai";

function ThemeItemComponent({ theme }) {
    const [selectedTheme, setSelectedTheme] = useState("");
    const [currentTheme, setCurrentTheme] = useAtom(currentThemeAtom);
    const currentlySelectedTheme = selectedTheme === currentTheme;

    if (selectedTheme === "") {
        setSelectedTheme(theme.name);
    }

    return (
        <div 
            data-theme={selectedTheme} 
            className={`flex w-full rounded-lg p-2 space-x-2 items-center justify-center hover:cursor-pointer ${currentlySelectedTheme ? "border-2 border-primary" : "border-transparent"}`}
            onClick={() => setCurrentTheme(selectedTheme)}
        >
            {/* theme name */}
            <div>{theme.name}</div>
        </div>
    );
}

export default function Theme() {
    const themes = ["light", "dark"];
    
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 justify-center">
            {themes.map((theme) => (
                <ThemeItemComponent key={theme} theme={{ name: theme }} />
            ))}
        </div>
    );
}