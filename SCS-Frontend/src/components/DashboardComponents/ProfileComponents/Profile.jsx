import React, { useState, useRef, useEffect } from "react";
import visaLogo from "../../../components/DashboardComponents/ProfileComponents/VISA.png";
import amexLogo from "../../../components/DashboardComponents/ProfileComponents/AMEX.png";
import masterLogo from "../../../components/DashboardComponents/ProfileComponents/MASTER.png";
import CVVLogo from "../../../components/DashboardComponents/ProfileComponents/CVV.png";
import questionLogo from "../../../components/DashboardComponents/ProfileComponents/QUESTION.png";

export default function Profile() {
  const [paymentInfo, setPaymentInfo] = useState({
    cardNumber: "",
    expiryMonth: "",
    expiryYear: "",
    cvv: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPaymentInfo({
      ...paymentInfo,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(paymentInfo);
    // Process payment info here or send to backend
  };

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

  return (
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
                value={paymentInfo.cardNumber}
                onChange={handleInputChange}
                className="w-full rounded-md border-gray-300"
                placeholder="1234 5678 9012 3456"
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
                  value={paymentInfo.expiryMonth}
                  onChange={handleInputChange}
                  className="rounded-md border-gray-300 w-20"
                  placeholder="MM"
                />
                <input
                  type="text"
                  name="expiryYear"
                  value={paymentInfo.expiryYear}
                  onChange={handleInputChange}
                  className="rounded-md border-gray-300 w-32"
                  placeholder="YYYY"
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
                name="cvv"
                value={paymentInfo.cvv}
                onChange={handleInputChange}
                className="rounded-md border-gray-300 w-16"
                placeholder="123"
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
          <div className="text-right">
            <button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
              Update Payment Method
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
