"use client";

import classNames from "classnames";
import { useRouter } from "next/navigation";
import React, { useState } from "react";
import UserService from "@services/UserService";
import { StatusMessage } from "@types";
import { useTranslations } from "next-intl";

const UserRegisterForm: React.FC = () => {
  const t = useTranslations("Register");
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    firstName: "",
    lastName: "",
    email: "",
  });

  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validate = (): boolean => {
    if (Object.values(formData).some((val) => !val.trim())) {
      setStatusMessages([{ type: "error", message: t("error_all_required") }]);
      return false;
    }
    return true;
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setStatusMessages([]);

    if (!validate()) return;

    try {
      await UserService.signup(formData);
      setStatusMessages([
        {
          message: t("success"),
          type: "success",
        },
      ]);
      setTimeout(() => {
        router.push("/login");
      }, 2000);
    } catch (error) {
      setStatusMessages([
        {
          message: (error as Error).message || t("error_generic"),
          type: "error",
        },
      ]);
    }
  };

  return (
    <div className="max-w-sm m-auto mt-10">
      {statusMessages.length > 0 && (
        <ul className="mb-4">
          {statusMessages.map(({ message, type }, index) => (
            <li
              key={index}
              className={classNames({
                "text-red-600": type === "error",
                "text-green-600": type === "success",
              })}
            >
              {message}
            </li>
          ))}
        </ul>
      )}
      <form onSubmit={handleSubmit} className="space-y-4">
        {[
          { label: t("firstName"), name: "firstName", type: "text" },
          { label: t("lastName"), name: "lastName", type: "text" },
          { label: t("email"), name: "email", type: "email" },
          { label: t("username"), name: "username", type: "text" },
          { label: t("password"), name: "password", type: "password" },
        ].map((field) => (
          <div key={field.name}>
            <label
              htmlFor={field.name}
              className="block mb-1 text-sm font-medium"
            >
              {field.label}:
            </label>
            <input
              id={field.name}
              name={field.name}
              type={field.type}
              value={formData[field.name as keyof typeof formData]}
              onChange={handleChange}
              className="border border-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
            />
          </div>
        ))}

        <button
          className="w-full text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          type="submit"
        >
          {t("button")}
        </button>
      </form>
    </div>
  );
};

export default UserRegisterForm;
