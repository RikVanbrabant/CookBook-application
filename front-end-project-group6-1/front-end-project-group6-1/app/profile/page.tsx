"use client";

import Header from "@components/header";
import { useEffect, useState } from "react";
import UserService from "@services/UserService";
import { Profile } from "@types";
import { useAuth } from "hooks/useAuth";

const ProfilePage = () => {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await UserService.getProfile();
        setProfile(data);
      } catch (err) {
        console.error(err);
        setError(
          "Could not load profile. Check backend connection (Port 3000?)."
        );
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchProfile();
    } else {
      setLoading(false);
    }
  }, [user]);

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <h1 className="text-4xl font-bold text-gray-800 pb-8">My Profile</h1>

        {loading && <p className="text-gray-600">Loading profile...</p>}

        {!user && !loading && (
          <p className="text-red-500">
            You must be logged in to view this page.
          </p>
        )}

        {error && <p className="text-red-500">{error}</p>}

        {profile && (
          <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-lg border border-gray-100">
            <div className="flex flex-col space-y-6">
              <div>
                <h2 className="text-sm font-semibold text-gray-500 uppercase tracking-wide">
                  Full Name
                </h2>
                <p className="text-2xl font-medium text-gray-900 mt-1">
                  {profile.fullName}
                </p>
              </div>

              <div className="h-px bg-gray-100" />

              <div>
                <h2 className="text-sm font-semibold text-gray-500 uppercase tracking-wide">
                  About Me
                </h2>
                <p className="text-lg text-gray-700 mt-2 leading-relaxed">
                  {profile.about}
                </p>
              </div>
            </div>
          </div>
        )}
      </main>
    </>
  );
};

export default ProfilePage;
