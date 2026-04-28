"use client";
import Header from "@components/header";
import UserService from "@services/UserService";
import { Profile } from "@types";
import { useEffect, useState } from "react";
import { useAuth } from "hooks/useAuth";

export default function ProfilePage() {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [error, setError] = useState<string>("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await UserService.getProfile();
        setProfile(data);
      } catch (err) {
        setError("Failed to load profile");
        console.error(err);
      }
    };

    fetchProfile();
  }, []);

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <div className="bg-white shadow-md rounded-lg p-8 max-w-lg w-full mt-10">
          <h1 className="text-3xl font-bold text-red-600 mb-6 text-center">
            User Profile
          </h1>

          {error && <p className="text-red-500 text-center">{error}</p>}

          {!profile && !error && (
            <p className="text-center text-gray-500">Loading...</p>
          )}

          {profile && (
            <div className="space-y-4">
              <div>
                <label className="block text-gray-700 font-bold mb-1">
                  Full Name:
                </label>
                <p className="text-lg border-b pb-2">{profile.fullName}</p>
              </div>

              <div>
                <label className="block text-gray-700 font-bold mb-1">
                  About Me:
                </label>
                <p className="text-lg text-gray-600 italic border-b pb-2">
                  {profile.about || "No description provided."}
                </p>
              </div>
            </div>
          )}
        </div>
      </main>
    </>
  );
}
