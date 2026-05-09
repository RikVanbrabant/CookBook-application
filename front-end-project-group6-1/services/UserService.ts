import { User, Profile } from "@types";

const login = async (user: User): Promise<User> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/login`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
      credentials: "include",
    },
  );
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || `Authentication failed.`);
  }
  return response.json();
};

const signup = async (user: User): Promise<User> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/signup`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    },
  );
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || `Registration failed.`);
  }
  return response.json();
};

const logout = async (): Promise<void> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/logout`,
    { method: "POST", credentials: "include" },
  );
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || `Logout failed. Check server logs.`);
  }
};

const getProfile = async (): Promise<Profile> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/profile`,
    {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    },
  );
  if (!response.ok) throw new Error("Failed to fetch profile");
  return response.json();
};

const forgotPassword = async (email: string): Promise<void> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/forgot-password`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email }),
    },
  );
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "Something went wrong.");
  }
};

const resetPassword = async (
  token: string,
  newPassword: string,
): Promise<void> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/users/reset-password`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token, newPassword }),
    },
  );
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "Invalid or expired reset link.");
  }
};

const UserService = {
  login,
  signup,
  logout,
  getProfile,
  forgotPassword,
  resetPassword,
};
export default UserService;
