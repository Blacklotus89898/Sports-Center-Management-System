import React, { useState, useRef, useEffect } from "react";
import useFetch from "../../../api/useFetch";
import Modal from "../../Modal";

import visaLogo from "../../../components/DashboardComponents/ProfileComponents/VISA.png";
import amexLogo from "../../../components/DashboardComponents/ProfileComponents/AMEX.png";
import masterLogo from "../../../components/DashboardComponents/ProfileComponents/MASTER.png";
import CVVLogo from "../../../components/DashboardComponents/ProfileComponents/CVV.png";
import questionLogo from "../../../components/DashboardComponents/ProfileComponents/QUESTION.png";

export default function Profile() {
    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    // add category states
    const [addCardNumber, setAddCardNumber] = useState("");
    const [addExpiryMonth, setAddExpiryMonth] = useState("");
    const [addExpiryYear, setAddExpiryYear] = useState("");
    const [addSecurityCode, setAddSecurityCode] = useState("");

    const [fetching, setFetching] = useState(false);
    const [paymentMethod, setPaymentMethod] = useState([]);

    // update states
    const [success, setSuccess] = useState(false);      // update success
    const [currentFocus, setCurrentFocus] = useState(""); // current category being updated

    const [hasPaymentMethod, setHasPaymentMethod] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    // At the beginning of your Profile component function, add:
    const tooltipRef = useRef(null);
    const [showCVVTooltip, setShowCVVTooltip] = useState(false);

    // Add this useEffect hook inside your Profile component
    useEffect(() => {
        // Function to hide the tooltip
        function handleClickOutside(event) {
            if (tooltipRef.current && !tooltipRef.current.contains(event.target)) {
                setShowCVVTooltip(false);
            }
        }

        // Bind the event listener
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            // Unbind the event listener on clean up
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    useEffect(() => {
        fetchData(
            `${API_URL}/payment-method`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            },
            (data) => {
                setPaymentMethod(data.paymentMethod);
            }
        );
    }, []);

    async function addPaymentMethod() {
        setErrorMessage('');
        setFetching(true);
        try {
            await fetchData(`${API_URL}/paymentMethod`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    // Include all necessary fields your API expects
                    cardNumber: addCardNumber,
                    expiryMonth: addExpiryMonth,
                    expiryYear: addExpiryYear,
                    securityCode: addSecurityCode,
                })
            }, (newPaymentMethod) => {
                setFetching(false);
                if (newPaymentMethod) {
                    setPaymentMethod([...paymentMethod, newPaymentMethod]);
                    setAddCardNumber('');
                    setAddExpiryMonth('');
                    setAddExpiryYear('');
                    setAddSecurityCode('');
                    reset();
                    setHasPaymentMethod(true);
                }
            });
        } catch (error) {
            setFetching(false);
            setErrorMessage(error.response?.data?.message || 'An error occurred');
        }

        console.log(addCardNumber, addExpiryMonth, addExpiryYear, addSecurityCode);
    }
    

    async function updatePaymentMethod(paymentMethod) {
        setErrorMessage('');
        setFetching(true);
        try {
            await fetchData(`${API_URL}/paymentMethod/${paymentMethod.paymentId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    cardNumber: paymentMethod.cardNumber,
                    expiryMonth: paymentMethod.expiryMonth,
                    expiryYear: paymentMethod.expiryYear,
                    securityCode: paymentMethod.securityCode,
                })
            }, (updatedPaymentMethod) => {
                if (updatedPaymentMethod) {
                    setPaymentMethod(paymentMethod.map((p) => p.paymentId === updatePaymentMethod.paymentId ? updatePaymentMethod : p));
                    setSuccess(true);
                }
            });
        }
        catch (error) {
            setFetching(false);
            setErrorMessage(error.response?.data?.message || 'An error occurred');
        }

        console.log(paymentMethod.cardNumber, paymentMethod.expiryMonth, paymentMethod.expiryYear, paymentMethod.securityCode);
    }

    function FormatAddContent() {
        return (
            <>
                <div className="w-full min-h-screen bg-white flex justify-center items-center p-8">
                    <div className="w-full max-w-xl">
                        <h1 className="text-xl font-bold mb-4">Payment Method</h1>
                        <form onSubmit={handleSubmit} className="space-y-6">
                            <div>
                                <label className="text-gray-700 font-semibold block mb-2">
                                    Please enter your card number. Do not use spaces.
                                </label>
                                <div className="flex justify-between items-center">
                                    <input
                                        type="text"
                                        name="cardNumber"
                                        setValue={setAddCardNumber}
                                        className="w-full rounded-md border-gray-300"
                                        placeholder="1234 5678 9012 3456"
                                        textfield={true}
                                    />
                                    <span className="ml-4 flex items-center">
                                        <img src={visaLogo} alt="Visa" className="h-3 mr-2" />
                                        <img src={amexLogo} alt="Visa" className="h-3 mr-2" />
                                        <img src={masterLogo} alt="Visa" className="h-3 mr-2" />
                                    </span>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div>
                                    <label className="text-gray-700 font-semibold block mb-2">
                                        Expiry Date
                                    </label>
                                    <div className="flex gap-2">
                                        <input
                                            type="text"
                                            name="expiryMonth"
                                            setValue={setAddExpiryMonth}
                                            className="rounded-md border-gray-300 w-20"
                                            placeholder="MM"
                                            textfield={true}
                                        />
                                        <input
                                            type="text"
                                            name="expiryYear"
                                            setValue={setAddExpiryYear}
                                            className="rounded-md border-gray-300 w-32"
                                            placeholder="YYYY"
                                            textfield={true}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div>
                                <label className="text-gray-700 font-semibold block mb-2">
                                    CVV
                                </label>
                                <div className="flex items-center">
                                    <input
                                        type="text"
                                        name="securityCode"
                                        setValue={setAddSecurityCode}
                                        className="rounded-md border-gray-300 w-16"
                                        placeholder="123"
                                        textfield={true}
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setShowCVVTooltip(true)}
                                        className="ml-2"
                                    >
                                        <img src={questionLogo} alt="?" className="h-4 mr-2" />
                                    </button>
                                    {showCVVTooltip && (
                                        <div
                                            ref={tooltipRef}
                                            className="absolute p-1 bg-white border rounded shadow-lg"
                                        >
                                            <img src={CVVLogo} alt="CVV Information" />
                                        </div>
                                    )}
                                </div>
                            </div>
                            <div className="py-2" />
                            {errorMessage && <div className="text-red-500">{errorMessage}</div>}
                            <div className="text-right">
                                <button
                                    className="btn btn-primary"
                                    onClick={() => {addPaymentMethod(); setCurrentFocus('')}}
                                >
                                    Create
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </>
        );
    }

    function FormatUpdateContent(paymentMethod) {
        const [updateCardNumber, setUpdateCardNumber] = useState(paymentMethod.cardNumber);
        const [updateExpiryMonth, setUpdateExpiryMonth] = useState(paymentMethod.expiryMonth);
        const [updateExpiryYear, setUpdateExpiryYear] = useState(paymentMethod.expiryYear);
        const [updateSecurityCode, setUpdateSecurityCode] = useState(paymentMethod.securityCode);

        return (
            <>
                <div className="w-full min-h-screen bg-white flex justify-center items-center p-8">
                    <div className="w-full max-w-xl">
                        <h1 className="text-xl font-bold mb-4">Payment Method</h1>
                        <form onSubmit={handleSubmit} className="space-y-6">
                            <div>
                                <label className="text-gray-700 font-semibold block mb-2">
                                    Please enter your card number. Do not use spaces.
                                </label>
                                <div className="flex justify-between items-center">
                                    <input
                                        type="text"
                                        name="cardNumber"
                                        value={paymentMethod.cardNumber}
                                        setValue={setUpdateCardNumber}
                                        className="w-full rounded-md border-gray-300"
                                        placeholder="1234 5678 9012 3456"
                                        textfield={true}
                                    />
                                    <span className="ml-4 flex items-center">
                                        <img src={visaLogo} alt="Visa" className="h-3 mr-2" />
                                        <img src={amexLogo} alt="Visa" className="h-3 mr-2" />
                                        <img src={masterLogo} alt="Visa" className="h-3 mr-2" />
                                    </span>
                                </div>
                            </div>
                            <div className="flex gap-4">
                                <div>
                                    <label className="text-gray-700 font-semibold block mb-2">
                                        Expiry Date
                                    </label>
                                    <div className="flex gap-2">
                                        <input
                                            type="text"
                                            name="expiryMonth"
                                            value={paymentMethod.expiryMonth}
                                            setValue={setUpdateExpiryMonth}
                                            className="rounded-md border-gray-300 w-20"
                                            placeholder="MM"
                                            textfield={true}
                                        />
                                        <input
                                            type="text"
                                            name="expiryYear"
                                            value={paymentMethod.expiryYear}
                                            setValue={setUpdateExpiryYear}
                                            className="rounded-md border-gray-300 w-32"
                                            placeholder="YYYY"
                                            textfield={true}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div>
                                <label className="text-gray-700 font-semibold block mb-2">
                                    CVV
                                </label>
                                <div className="flex items-center">
                                    <input
                                        type="text"
                                        name="securityCode"
                                        value={paymentMethod.securityCode}
                                        setValue={setUpdateSecurityCode}
                                        className="rounded-md border-gray-300 w-16"
                                        placeholder="123"
                                        textfield={true}
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setShowCVVTooltip(true)}
                                        className="ml-2"
                                    >
                                        <img src={questionLogo} alt="?" className="h-4 mr-2" />
                                    </button>
                                    {showCVVTooltip && (
                                        <div
                                            ref={tooltipRef}
                                            className="absolute p-1 bg-white border rounded shadow-lg"
                                        >
                                            <img src={CVVLogo} alt="CVV Information" />
                                        </div>
                                    )}
                                </div>
                            </div>
                            <div className="py-2" />
                            {errorMessage && <div className="text-red-500">{errorMessage}</div>}
                            <div className="text-right">
                                <button
                                    className="btn btn-primary"
                                    onClick={() => {
                                        updatePaymentMethod({
                                            cardNumber: updateCardNumber,
                                            expiryMonth: updateExpiryMonth,
                                            expiryYear: updateExpiryYear,
                                            securityCode: updateSecurityCode
                                        });
                                        setCurrentFocus(category.className);
                                    }}
                                >
                                    Update
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </>
        );
    }
    return (
        <>
            {/* fetching loading */}
            {fetching &&
                <div className="flex w-full justify-center content-center">
                    <span className="loading loading-ring loading-lg" />
                </div>
            }
    
            {/* payment method */}
            {FormatUpdateContent(paymentMethod)}

            {/* add item modal */}
            {FormatAddContent()}
        </>
    );
}
