import React from "react";

import { currentThemeAtom } from "../../utils/jotai";
import { useAtom } from "jotai";

import scsLogo from "../../../public/scs.svg";
import scsLogoDark from "../../../public/logo-dark.svg";
import scsLogoLight from "../../../public/logo-light.svg";

export default function Logo() {
    const [currentTheme, ] = useAtom(currentThemeAtom);

    return (
        <div className="flex w-1/3 min-w-1/3 text-lg justify-center md:justify-start">
            <img className="w-6 aspect-square" src={currentTheme === "dark" ? scsLogoDark : scsLogoLight}></img>
            <div className="px-1" />
            <div>SCS</div>
        </div>
    );
}