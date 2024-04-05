import React from "react";

import { useSetAtom } from 'jotai';
import { currentThemeAtom } from '../utils/jotai';

export default function Themes() {
    const setCurrentTheme = useSetAtom(currentThemeAtom);

    return (
        <>
            <div className="p-4">
                <h1 className="text-2xl font-bold">Themes</h1>
                <div className="flex flex-wrap gap-4">
                    <button className="btn btn-primary" onClick={() => setCurrentTheme('light')}>Light</button>
                    <button className="btn btn-primary" onClick={() => setCurrentTheme('dark')}>Dark</button>        
                </div>
            </div>
        </>
    );
}