import React from 'react';
import { useAtomValue } from 'jotai';
import { currentThemeAtom } from '../utils/jotai';

const ThemeProvider = ({ children }) => {
    const currentTheme = useAtomValue(currentThemeAtom);

    // Set the daisy-ui data-theme here
    document.documentElement.setAttribute('data-theme', currentTheme);
    document.documentElement.classList.add('theme-transition');
    document.documentElement.setAttribute('color-scheme', 'dark');

    if (currentTheme === 'dark') {
        document.documentElement.classList.add('dark:[color-scheme:dark]');
    } else {
        document.documentElement.classList.remove('dark:[color-scheme:dark]');
    }

    return <>{children}</>;
};

export default ThemeProvider;