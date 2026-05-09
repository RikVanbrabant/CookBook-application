"use client";

import classNames from "classnames";
import { useRouter, useSearchParams } from "next/navigation";
import React, { useState } from "react";
import UserService from "@services/UserService";
import { StatusMessage } from "@types";

const ResetPasswordForm: React.FC = () => {
  const searchParams = useSearchParams();
  const token = searchParams.get("token") ?? "";
  const router = useRouter();

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const validate = (): boolean => {
    if (!newPassword || newPassword.length < 8) {
      setPasswordError("Password must be at least 8 characters.");
      return false;
    }
    if (newPassword !== confirmPassword) {
      setPasswordError("Passwords do not match.");
      return false;
    }
    return true;
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setPasswordError(null);
    setStatusMessages([]);

    if (!token) {
      setStatusMessages([
        { message: "Reset link is missing or invalid.", type: "error" },
      ]);
      return;
    }
    if (!validate()) return;

    setIsLoading(true);
    try {
      await UserService.resetPassword(token, newPassword);
      setStatusMessages([
        { message: "Password updated! Redirecting to login…", type: "success" },
      ]);
      setTimeout(() => router.push("/login"), 2000);
    } catch (error) {
      setStatusMessages([
        {
          message: (error as Error).message || "Invalid or expired reset link.",
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
        <h2 className="text-3xl font-bold text-gray-800">Reset Password</h2>
        <p className="text-sm text-gray-500 mt-2">
          Enter your new password below.
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
            htmlFor="newPassword"
            className="block mb-2 text-sm font-semibold text-gray-700"
          >
            New Password
          </label>
          <input
            id="newPassword"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            className={classNames(
              "block w-full px-4 py-3 text-gray-900 bg-gray-50 border rounded-lg focus:ring-2 focus:outline-none transition-all duration-200",
              {
                "border-red-300 focus:ring-red-200 focus:border-red-500":
                  passwordError,
                "border-gray-300 focus:ring-blue-100 focus:border-blue-500":
                  !passwordError,
              },
            )}
            placeholder="Minimum 8 characters"
          />
        </div>

        <div>
          <label
            htmlFor="confirmPassword"
            className="block mb-2 text-sm font-semibold text-gray-700"
          >
            Confirm Password
          </label>
          <input
            id="confirmPassword"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className={classNames(
              "block w-full px-4 py-3 text-gray-900 bg-gray-50 border rounded-lg focus:ring-2 focus:outline-none transition-all duration-200",
              {
                "border-red-300 focus:ring-red-200 focus:border-red-500":
                  passwordError,
                "border-gray-300 focus:ring-blue-100 focus:border-blue-500":
                  !passwordError,
              },
            )}
            placeholder="Repeat your new password"
          />
          {passwordError && (
            <p className="mt-1 text-xs text-red-600 font-medium">
              {passwordError}
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
          {isLoading ? "Updating…" : "Set New Password"}
        </button>
      </form>
    </div>
  );
};

export default ResetPasswordForm;
