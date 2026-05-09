"use client";

import classNames from "classnames";
import { useRouter } from "next/navigation";
import React, { useState } from "react";
import UserService from "@services/UserService";
import { StatusMessage } from "@types";
import { useAuth } from "../../hooks/useAuth";
import { useTranslations } from "next-intl";

const UserLoginForm: React.FC = () => {
  const t = useTranslations("Login");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [nameError, setNameError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const router = useRouter();
  const { login } = useAuth();

  const clearErrors = () => {
    setNameError(null);
    setPasswordError(null);
    setStatusMessages([]);
  };

  const validate = (): boolean => {
    let result = true;
    if (!name || name.trim() === "") {
      setNameError(t("error_required_name"));
      result = false;
    }
    if (!password || password.trim() === "") {
      setPasswordError(t("error_required_password"));
      result = false;
    }
    return result;
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    clearErrors();

    if (!validate()) return;

    setIsLoading(true);
    const user = { username: name, password };

    try {
      const loggedInUser = await UserService.login(user);
      setStatusMessages([{ message: t("success"), type: "success" }]);
      login(loggedInUser);
      setTimeout(() => {
        router.push("/");
      }, 1500);
    } catch (error) {
      setStatusMessages([
        {
          message: (error as Error).message || t("error_generic"),
          type: "error",
        },
      ]);
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-lg border border-gray-100">
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-gray-800">{t("welcome")}</h2>
        <p className="text-sm text-gray-500 mt-2">{t("subtitle")}</p>
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
            htmlFor="nameInput"
            className="block mb-2 text-sm font-semibold text-gray-700"
          >
            {t("username")}
          </label>
          <input
            id="nameInput"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className={classNames(
              "block w-full px-4 py-3 text-gray-900 bg-gray-50 border rounded-lg focus:ring-2 focus:outline-none transition-all duration-200",
              {
                "border-red-300 focus:ring-red-200 focus:border-red-500":
                  nameError,
                "border-gray-300 focus:ring-blue-100 focus:border-blue-500":
                  !nameError,
              },
            )}
            placeholder="e.g. mario"
          />
          {nameError && (
            <p className="mt-1 text-xs text-red-600 font-medium">{nameError}</p>
          )}
        </div>

        <div>
          {/* Only change from original: added the flex row with "Forgot password?" link */}
          <div className="flex justify-between items-center mb-2">
            <label
              htmlFor="passwordInput"
              className="text-sm font-semibold text-gray-700"
            >
              {t("password")}
            </label>
            <a
              href="/forgot-password"
              className="text-xs text-blue-600 hover:underline"
            >
              Forgot password?
            </a>
          </div>
          <input
            id="passwordInput"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className={classNames(
              "block w-full px-4 py-3 text-gray-900 bg-gray-50 border rounded-lg focus:ring-2 focus:outline-none transition-all duration-200",
              {
                "border-red-300 focus:ring-red-200 focus:border-red-500":
                  passwordError,
                "border-gray-300 focus:ring-blue-100 focus:border-blue-500":
                  !passwordError,
              },
            )}
            placeholder="e.g. mario123"
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
          {isLoading ? (
            <span className="flex items-center justify-center gap-2">
              <svg
                className="animate-spin h-5 w-5 text-white"
                viewBox="0 0 24 24"
              >
                <circle
                  className="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  strokeWidth="4"
                />
                <path
                  className="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                />
              </svg>
              {t("signing_in")}
            </span>
          ) : (
            t("button")
          )}
        </button>
      </form>
    </div>
  );
};

export default UserLoginForm;
