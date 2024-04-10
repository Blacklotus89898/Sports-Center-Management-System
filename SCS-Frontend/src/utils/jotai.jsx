import { atom } from 'jotai';
import { atomWithStorage } from 'jotai/utils'

// docs: https://jotai.org/

// if in local storage there is no currentTheme, set it to light
if (!localStorage.getItem('currentTheme')) {
    // If not, set it to 'light'
    localStorage.setItem('currentTheme', 'light');
}

// if in local storage there is no currentUser, set it to null
if (!localStorage.getItem('currentUser')) {
    // If not, set it to null
    localStorage.setItem('currentUser', JSON.stringify({
        "id": -1,
        "role": null,
        "name": null,
        "email": null,
        "password": null,
        "creationDate": null,
        "image": null
    }));
}

export const currentThemeAtom = atomWithStorage('currentTheme', 'light');
export const currentUserAtom = atomWithStorage('currentUser', {
    "id": -1,
    "role": null,
    "name": null,
    "email": null,
    "password": null,
    "creationDate": null,
    "image": null
});