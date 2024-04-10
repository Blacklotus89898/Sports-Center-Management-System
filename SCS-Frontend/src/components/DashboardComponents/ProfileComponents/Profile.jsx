import React, { useState, useEffect } from 'react';

import { useAtom } from 'jotai';
import { currentUserAtom } from '../../../utils/jotai';

import useFetch from '../../../api/useFetch';

const noImageUrl = 'https://static.vecteezy.com/system/resources/previews/002/534/006/original/social-media-chatting-online-blank-profile-picture-head-and-body-icon-people-standing-icon-grey-background-free-vector.jpg';

import { FiMinus } from 'react-icons/fi';
import AddUpdateInputFieldComponent from '../AddUpdateInputFieldComponent';
import { getUserRole } from '../../../utils/auth';

export default function Profile() {
    const [currentFocus, setCurrentFocus] = useState("");
    const [currentUser, setCurrentUser] = useAtom(currentUserAtom);
    const [paymentInfo, setPaymentInfo] = useState({});

    const [updateImage, setUpdateImage] = useState("");
    const [updateName, setUpdateName] = useState();
    const [updateEmail, setUpdateEmail] = useState();
    const [updatePassword, setUpdatePassword] = useState();

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    async function savePaymentInfo() {
        let user = JSON.parse(localStorage.getItem("currentUser"));
        
        let cardNumber = ""
        if (Number(paymentInfo.cardNumber)) {
            cardNumber = paymentInfo.cardNumber;
        } else {
            cardNumber = Number(paymentInfo.cardNumber.replace(/\D/g, '').substring(0, 16));
        }

        
        let expiryMonth = Number(paymentInfo.expiryMonth);
        let expiryYear = Number(paymentInfo.expiryYear);
        let securityCode = Number(paymentInfo.securityCode);

        let payment = {
            "cardNumber": cardNumber,
            "expiryMonth": expiryMonth,
            "expiryYear": expiryYear,
            "securityCode": securityCode,
            "accountId": currentUser.id
        }

        if (paymentInfo.paymentId === -1) {
            // create payment method (http://localhost:8080/paymentMethod)
            fetchData(`${API_URL}/paymentMethod`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payment)
            }, (data) => {
                if (data && data.id) {
                    setPaymentInfo(data);
                }
            });
        } else {
            // http://localhost:8080/paymentMethod/{paymentId}
            fetchData(`${API_URL}/paymentMethod/${paymentInfo.paymentId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payment)
            }, (data) => {
                if (data && data.id) {
                    setPaymentInfo(data);
                }
            });
        }
    }

    async function updateProfile() {
        let user = JSON.parse(localStorage.getItem("currentUser"));

        let updatedUser = {
            "id": user.id,
            "name": updateName,
            "email": updateEmail,
            "password": updatePassword,
            "creationDate": user.creationDate,
            "image": updateImage
        }

        let url = ""

        if (user.role === "CUSTOMER") {
            url = "http://localhost:8080/customers";
        } else if (user.role === "INSTRUCTOR") {
            url = "http://localhost:8080/instructors";
        } else if (user.role === "OWNER") {
            url = "http://localhost:8080/owner";
        }

        fetchData(`${url}/${user.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedUser)
        }, (data) => {
            if (data && data.id) {
                setCurrentUser(data);

                setUpdateName(updatedUser.name);
                setUpdateEmail(updatedUser.email);
                setUpdatePassword(updatedUser.password);
                setUpdateImage(updatedUser.image);

                data.role = user.role;
                localStorage.setItem("currentUser", JSON.stringify(data));
            } else {
                setUpdateName(user.name);
                setUpdateEmail(user.email);
                setUpdatePassword(user.password);
                setUpdateImage(user.image);
            }
        });
    }


    useEffect(() => {
        // get payment info of currentUser (http://localhost:8080/customers/{accountId}/paymentMethod)
        let user = JSON.parse(localStorage.getItem("currentUser"))

        if (user.role === "CUSTOMER") {
            fetchData(`${API_URL}/customers/${user.id}/paymentMethod`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                }, (data) => {
                    if (data) {
                        setPaymentInfo(data);
                    } else {
                        setPaymentInfo({
                            "cardNumber": -1,
                            "expiryMonth": -1,
                            "expiryYear": -1,
                            "securityCode": -1,
                            "paymentId": -1,
                            "customer": user
                        });
                    }
                }
            );
        } else {
            setPaymentInfo({
                "cardNumber": -1,
                "expiryMonth": -1,
                "expiryYear": -1,
                "securityCode": -1,
                "paymentId": -1,
                "customer": user
            });
        }

        // fetch user data
        let url = ""
        if (user.role === "CUSTOMER") {
            url = "http://localhost:8080/customers";
        } else if (user.role === "INSTRUCTOR") {
            url = "http://localhost:8080/instructors";
        } else if (user.role === "OWNER") {
            url = "http://localhost:8080/owner";
        }

        fetchData(`${url}/${user.id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
            }, (data) => {
                if (data && data.id) {
                    setCurrentUser(data);
                    setUpdateImage(data.image);
                    setUpdateName(data.name);
                    setUpdateEmail(data.email);
                    setUpdatePassword(data.password);
                    
                    data.role = user.role;
                    localStorage.setItem("currentUser", JSON.stringify(data));
                }
            }
        );
    }, [])

    function ProfileCard() {
        return (
            <div className='w-full md:w-1/2 flex flex-col md:flex-row'>
                <div className='w-full'>
                    {/* image */}
                    <div className='flex flex-row w-full justify-evenly'>
                        <div className="relative flex justify-center items-center w-24 aspect-square">
                            <img
                                loading="lazy"
                                src={updateImage ? `data:image/jpeg;base64,${updateImage}` : noImageUrl}
                                alt="Uploaded Image"
                                className="absolute inset-0 object-cover aspect-square rounded-xl"
                            />
                            <div className="absolute top-0 right-0">
                                <button 
                                    className="aspect-square bg-error text-base-200 rounded-full m-1"
                                    onClick={() => setUpdateImage("")}
                                >
                                    <FiMinus />
                                </button>
                            </div>
                        </div>

                        <div className="flex w-1/2 items-center">
                            <input
                                type="file"
                                className="file-input"
                                accept="image/*"
                                onChange={(e) => {
                                    const file = e.target.files[0];
                                    const reader = new FileReader();
                                    reader.onloadend = () => {
                                        setUpdateImage(reader.result.split(',')[1]);
                                    };
                                    reader.readAsDataURL(file);
                                }}
                            />
                        </div>
                    </div>
                    {/* id - creationDate */}

                    <div className="py-2" />

                    {/* name */}
                    <AddUpdateInputFieldComponent id={"name"} title={"Name"} value={updateName} setValue={setUpdateName} />

                    <div className="py-2" />

                    {/* email */}
                    <AddUpdateInputFieldComponent id={"email"} title={"Email"} value={updateEmail} setValue={setUpdateEmail} />

                    <div className="py-2" />

                    {/* password */}
                    <AddUpdateInputFieldComponent id={"password"} title={"Password"} value={updatePassword} setValue={setUpdatePassword} type={"password"} />

                    {/* error message */}
                    {(error && currentFocus === "profile") && <div className='pt-5 text-error text-center'>{data?.errors?.toString()}</div>}

                    <div className="flex w-full pt-5 justify-center">
                    <button className="btn btn-primary" onClick={() => {
                        updateProfile()
                        setCurrentFocus("profile")
                    }}>
                        Update Profile
                    </button>
                </div>
                </div>
            </div>
        )
    }
    
    function ProfilePayment() {
        return (
            <div className='w-full md:w-1/2'>
                <div className="card shadow-xl">
                    <div className="form-control">
                        <label className="label">
                            <span className="label-text">Card Number</span>
                        </label>
                        <input type="text" placeholder="1234 5678 9012 3456" className="input input-bordered" value={paymentInfo.cardNumber === -1 || !paymentInfo.cardNumber ? "" : paymentInfo.cardNumber} onChange={(e) => {
                            const input = e.target.value.replace(/\D/g, '').substring(0, 16);
                            const cardNumber = input.replace(/(.{4})/g, '$1 ').trim();
                            setPaymentInfo(prevState => ({ ...prevState, cardNumber }));
                        }} />
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
                        <div className="form-control">
                            <label className="label">
                            <span className="label-text">Expiry Date</span>
                            </label>
                            <div className='flex flex-row'>
                                <input 
                                    type="text" 
                                    placeholder="MM"
                                    className="input input-bordered w-1/2 bg-base-100" 
                                    value={paymentInfo.expiryMonth === -1 ? "" : paymentInfo.expiryMonth} 
                                    onChange={(e) => {
                                        setPaymentInfo(prevState => ({ ...prevState, expiryMonth: e.target.value.substring(0, 2) }));
                                    }} 
                                />
                                <div className='flex items-center text-lg px-2'>/</div>
                                <input 
                                    type="text" 
                                    placeholder="YY"
                                    className="input input-bordered w-1/2 bg-base-100" 
                                    value={paymentInfo.expiryYear === -1 ? "" : paymentInfo.expiryYear} 
                                    onChange={(e) => {
                                        setPaymentInfo(prevState => ({ ...prevState, expiryYear: e.target.value.substring(0, 2) }));
                                    }} 
                                />
                            </div>
                        </div>
                        <div className="form-control">
                            <label className="label">
                            <span className="label-text">CVV</span>
                            </label>
                            <input type="number" placeholder="123" className="input input-bordered" value={paymentInfo.securityCode == -1 ? "" : paymentInfo.securityCode} onChange={(e) => {
                                const input = e.target.value.replace(/\D/g, '').substring(0, 3);
                                const securityCode = input.trim();
                                setPaymentInfo(prevState => ({ ...prevState, securityCode }));
                            }} />
                        </div>
                    </div>
                </div>

                {/* error message */}
                {(error && currentFocus === "payment") && <div className='pt-5 text-error text-center'>{data?.errors?.toString()}</div>}

                <div className="flex w-full pt-5 justify-center">
                    <button className="btn btn-primary" onClick={() => {
                        savePaymentInfo();
                        setCurrentFocus("payment")
                    }}>
                        Update Payment Info
                    </button>
                </div>
            </div>
        )
    }

    return (
        <div className='flex flex-col w-full items-center'>
            {ProfileCard()}

            <div className="py-5" />

            {getUserRole() === 'CUSTOMER' && ProfilePayment()}
        </div>
    )
}