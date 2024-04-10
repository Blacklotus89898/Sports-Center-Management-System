import React from "react";
import { PageProvider } from "../providers/PageProvider";
import Customer from "../components/Customer/Customer";

export default function NoPage() {
    return (
        <PageProvider>
            <div className="flex-grow bg-base-100 text-center justify-center content-center">
                <h2 className="mt-6 text-3xl font-extrabold text-primary">
                    404 - Page Not Found
                </h2>
                <p className="mt-2 text-sm text-primary">
                    The page you are looking for does not exist.
                </p>
                <Customer></Customer>
            </div>         
        </PageProvider>
    );
}