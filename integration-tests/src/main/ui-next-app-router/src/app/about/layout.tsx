import type { Metadata } from "next";
import {ReactNode} from "react";

export const metadata: Metadata = {
    title: "About",
    description: "About page",
};

export default function AboutLayout({children,}: Readonly<{children: ReactNode}>) {
    return children;
}
