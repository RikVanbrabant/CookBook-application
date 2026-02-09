"use client";
import { User, StatusMessage } from "@types";
import UserService from "@services/UserService";
import React, { useState } from "react";
import classNames from "classnames";
import { useRouter } from "next/navigation";

const UserLoginForm: React.FC = () => {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [nameError, setNameError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
  const router = useRouter();

  const validate = (): boolean => {
    let isValid = true;
    if (name.trim() === "") {
      setNameError("Username is required");
      isValid = false;
    } else {
      setNameError(null);
    }
    if (password.trim() === "") {
      setPasswordError("Password is required");
      isValid = false;
    } else {
      setPasswordError(null);
    }
    return isValid;
  };

  const handleSubmit = async (event: { preventDefault: () => void }) => {
    event.preventDefault();

    if (!validate()) {
      return;
    }
    const user = { username: name, password };
    try {
      const loggedInUser = await UserService.login(
        user.username,
        user.password
      );
      setStatusMessages([
        {
          message: "Login successful. Redirecting to homepage...",
          type: "success",
        },
      ]);
      sessionStorage.setItem(
        "loggedInUser",
        JSON.stringify({
          username: user.username,
          password: user.password,
        })
      );
      setTimeout(() => {
        router.push("/");
      }, 1000);
    } catch (error) {
      setStatusMessages([{ message: (error as Error).message, type: "error" }]);
    }
  };

  return (
    <div className="max-w-sm m-auto">
      {statusMessages && (
        <div className="row">
          <ul className="list-none mb-3 mx-auto">
            {statusMessages.map(({ message, type }, index) => (
              <li
                key={index}
                className={classNames({
                  "text-red-800": type === "error",
                  "text-green-800": type === "success",
                })}
              >
                {message}
              </li>
            ))}
          </ul>
        </div>
      )}
      <form onSubmit={handleSubmit}>
        <div>
          <div>
            <label
              htmlFor="nameInput"
              className="block mb-2 text-sm font-medium"
            >
              Username:
            </label>
          </div>
          <div className="block mb-2 text-sm font-medium">
            <input
              id="nameInput"
              type="text"
              value={name}
              onChange={(event) => setName(event.target.value)}
              className="border border-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue:500 block w-full p-2.5"
            />
          </div>
          <div>{nameError && <p className="text-red-500">{nameError}</p>}</div>
        </div>
        <div className="mt-2">
          <div>
            <label
              htmlFor="passwordInput"
              className="block mb-2 text-sm font-medium"
            >
              Password:
            </label>
          </div>
          <div className="block mb-2 text-sm font-medium">
            <input
              id="passwordInput"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              className="border border-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue:500 block w-full p-2.5"
            />
          </div>
          <div>
            {passwordError && <p className="text-red-500">{passwordError}</p>}
          </div>
        </div>
        <div className="row">
          <button
            className="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
            type="submit"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  );
};

export default UserLoginForm;
