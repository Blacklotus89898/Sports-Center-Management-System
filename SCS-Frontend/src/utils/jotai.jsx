import { atom } from 'jotai';
import { atomWithStorage } from 'jotai/utils'

// docs: https://jotai.org/

export const currentThemeAtom = atomWithStorage('currentTheme', 'light');

export function isUserLoggedIn() {
    return true;
}