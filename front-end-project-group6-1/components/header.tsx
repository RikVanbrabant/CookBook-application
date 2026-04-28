"use client";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useAuth } from "../hooks/useAuth";
import { useTranslations } from "next-intl";
import Language from "./language";

const Header: React.FC = () => {
  const pathname = usePathname();
  const router = useRouter();
  const { user, logout } = useAuth();
  const t = useTranslations("Header");

  const handleLogout = async () => {
    try {
      await logout();
      router.push("/");
    } catch (error) {
      console.error("Logout failed", error);
    }
  };

  return (
    <header className="bg-gradient-to-r from-red-500 to-red-600 shadow-lg">
      <div className="container mx-auto px-6">
        <div className="flex items-center justify-between py-4">
          <div className="flex items-center space-x-3">
            <div className="flex flex-col">
              <Link href="/" className="text-2xl font-bold text-white">
                CookBook
              </Link>
              <p className="text-sm text-red-100">{t("slogan")}</p>
            </div>
          </div>

          <nav className="flex items-center space-x-8">
            <Link
              href="/"
              className="text-white hover:text-red-100 font-medium text-lg"
            >
              {t("home")}
            </Link>
            <Link
              href="/cookbooks"
              className="text-white hover:text-red-100 font-medium text-lg"
            >
              {t("cookbooks")}
            </Link>
            <Link
              href="/recipes"
              className="text-white hover:text-red-100 font-medium text-lg"
            >
              {t("recipes")}
            </Link>
            <Link href="/search">
              <img
                src="/noun-magnifying-glass-7604252.png"
                alt="Search"
                className="w-8 h-8"
              />
            </Link>

            <Language />
          </nav>

          <div className="flex items-center space-x-4">
            {!user ? (
              <Link
                href="/login"
                className="px-6 py-2 bg-white text-red-600 font-semibold rounded-lg"
              >
                {t("login")}
              </Link>
            ) : (
              <div className="flex items-center space-x-4">
                <Link
                  href="/profile"
                  className="text-white font-semibold hover:text-red-100 underline decoration-red-400 underline-offset-4"
                >
                  {user.username}
                </Link>
                <button
                  onClick={handleLogout}
                  className="px-5 py-2 bg-white/20 text-white rounded-lg"
                >
                  {t("logout")}
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
