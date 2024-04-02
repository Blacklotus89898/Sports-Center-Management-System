import React from 'react';
import { useAtomValue } from 'jotai';
import { currentThemeAtom } from '../utils/jotai';

const ThemeProvider = ({ children }) => {
    const currentTheme = useAtomValue(currentThemeAtom);

    // Set the daisy-ui data-theme here
    document.documentElement.setAttribute('data-theme', currentTheme);
    document.documentElement.classList.add('theme-transition');

    return <>{children}</>;
};

export default ThemeProvider;