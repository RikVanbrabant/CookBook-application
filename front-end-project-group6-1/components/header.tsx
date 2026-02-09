"use client";
import Link from "next/link";
import { useEffect, useState } from "react";
import { usePathname, useRouter } from "next/navigation";

const Header: React.FC = () => {
  const [loggedInUser, setLoggedInUser] = useState<{ username: string } | null>(
    null
  );
  const pathname = usePathname();
  const router = useRouter();

  useEffect(() => {
    const userStr = sessionStorage.getItem("loggedInUser");
    if (userStr) {
      const user = JSON.parse(userStr);
      setLoggedInUser(user);
    }
  }, [pathname]);

  const handleLogout = () => {
    sessionStorage.removeItem("loggedInUser");
    setLoggedInUser(null);
    router.push("/");
  };

  return (
    <header className="bg-gradient-to-r from-red-500 to-red-600 shadow-lg">
      <div className="container mx-auto px-6">
        <div className="flex items-center justify-between py-4">
          {/* Logo Section */}
          <div className="flex items-center space-x-3">
            <div className="w-14 h-14 bg-white rounded-xl flex items-center justify-center shadow-md hover:scale-105 transition-transform">
              <img
                src="https://cdn-icons-png.flaticon.com/512/7984/7984225.png"
                alt="Logo"
                className="w-9 h-9"
              />
            </div>
            <div className="flex flex-col">
              <Link
                href="/"
                className="text-2xl font-bold text-white hover:text-gray-100 transition-colors"
              >
                CookBook
              </Link>
              <p className="text-sm text-red-100">Culinary Collection</p>
            </div>
          </div>

          {/* Navigation */}
          <nav className="flex items-center space-x-8">
            <Link
              href="/"
              className={`text-white hover:text-red-100 font-medium text-lg transition-colors pb-1 border-b-2 ${
                pathname === "/" ? "border-white" : "border-transparent"
              }`}
            >
              Home
            </Link>
            <Link
              href="/cookbooks"
              className={`text-white hover:text-red-100 font-medium text-lg transition-colors pb-1 border-b-2 ${
                pathname === "/cookbooks"
                  ? "border-white"
                  : "border-transparent"
              }`}
            >
              Cookbooks
            </Link>
            <Link
              href="/recipes"
              className={`text-white hover:text-red-100 font-medium text-lg transition-colors pb-1 border-b-2 ${
                pathname === "/recipes" ? "border-white" : "border-transparent"
              }`}
            >
              All Recipes
            </Link>
          </nav>

          {/* User Section */}
          <div className="flex items-center space-x-4">
            {!loggedInUser ? (
              <Link
                href="/login"
                className="px-6 py-2 bg-white text-red-600 font-semibold rounded-lg hover:bg-red-50 transition-colors shadow-md"
              >
                Login
              </Link>
            ) : (
              <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-3 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-lg">
                  <div className="flex flex-col text-right">
                    <span className="text-white font-semibold">
                      {loggedInUser.username}
                    </span>
                    <span className="text-red-100 text-xs">Home Chef</span>
                  </div>
                  <div className="w-10 h-10 bg-white rounded-full flex items-center justify-center text-red-600 font-bold shadow-md">
                    {loggedInUser.username.charAt(0).toUpperCase()}
                  </div>
                </div>
                <button
                  onClick={handleLogout}
                  className="px-5 py-2 bg-white/20 backdrop-blur-sm text-white font-medium rounded-lg hover:bg-white/30 transition-colors"
                >
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
