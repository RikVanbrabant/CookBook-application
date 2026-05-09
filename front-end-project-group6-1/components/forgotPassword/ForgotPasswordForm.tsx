"use client";

import classNames from "classnames";
import React, { useState } from "react";
import UserService from "@services/UserService";
import { StatusMessage } from "@types";

const ForgotPasswordForm: React.FC = () => {
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState<string | null>(null);
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const validate = (): boolean => {
    if (!email || email.trim() === "") {
      setEmailError("Email is required.");
      return false;
    }
    if (!/\S+@\S+\.\S+/.test(email)) {
      setEmailError("Please enter a valid email address.");
      return false;
    }
    return true;
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setEmailError(null);
    setStatusMessages([]);
    if (!validate()) return;

    setIsLoading(true);
    try {
      await UserService.forgotPassword(email);
      setStatusMessages([
        {
          message:
            "If this email is registered, a reset link has been sent. Check your inbox.",
          type: "success",
        },
      ]);
      setEmail("");
    } catch (error) {
      setStatusMessages([
        {
          message: (error as Error).message || "Something went wrong.",
          type: "error",
        },
      ]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-lg border border-gray-100">
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-gray-800">Forgot Password</h2>
        <p className="text-sm text-gray-500 mt-2">
          Enter your email and we will send you a reset link.
        </p>
      </div>

      {statusMessages.length > 0 && (
        <div className="mb-6 space-y-2">
          {statusMessages.map(({ message, type }, index) => (
            <div
              key={index}
              className={classNames(
                "p-3 text-sm rounded-md border text-center font-medium",
                {
                  "bg-red-50 text-red-700 border-red-200": type === "error",
                  "bg-green-50 text-green-700 border-green-200":
                    type === "success",
                },
              )}
            >
              {message}
            </div>
          ))}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label
            htmlFor="emailInput"
            className="block mb-2 text-sm font-semibold text-gray-700"
          >
            Email address
          </label>
          <input
            id="emailInput"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className={classNames(
              "block w-full px-4 py-3 text-gray-900 bg-gray-50 border rounded-lg focus:ring-2 focus:outline-none transition-all duration-200",
              {
                "border-red-300 focus:ring-red-200 focus:border-red-500":
                  emailError,
                "border-gray-300 focus:ring-blue-100 focus:border-blue-500":
                  !emailError,
              },
            )}
            placeholder="e.g. mario@example.com"
          />
          {emailError && (
            <p className="mt-1 text-xs text-red-600 font-medium">
              {emailError}
            </p>
          )}
        </div>

        <button
          type="submit"
          disabled={isLoading}
          className={classNames(
            "w-full text-white font-bold rounded-lg text-sm px-5 py-3 text-center shadow-md transition-all duration-200",
            {
              "bg-blue-600 hover:bg-blue-700 hover:shadow-lg transform hover:-translate-y-0.5":
                !isLoading,
              "bg-blue-400 cursor-not-allowed": isLoading,
            },
          )}
        >
          {isLoading ? "Sending..." : "Send Reset Link"}
        </button>

        <p className="text-center text-sm text-gray-500">
          Remember your password?{" "}
          <a
            href="/login"
            className="text-blue-600 font-semibold hover:underline"
          >
            Back to login
          </a>
        </p>
      </form>
    </div>
  );
};

export default ForgotPasswordForm;
