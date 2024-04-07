import { atom } from 'jotai';
import { atomWithStorage } from 'jotai/utils'

// docs: https://jotai.org/

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