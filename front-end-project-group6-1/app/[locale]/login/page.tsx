import Header from "@components/header";
import UserLoginForm from "@components/login/UserLoginForm";
import { getTranslations } from "next-intl/server";
import Link from "next/link";

const LoginPage = async () => {
  const t = await getTranslations("Login");

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <h1 className="text-4xl font-bold text-gray-800 text-center pb-6">
          {t("title")}
        </h1>
        <section className="bg-gray-50 flex flex-col pt-6 items-center">
          <UserLoginForm />
          
          <div className="mt-6 text-sm text-gray-600">
            No account?{" "}
            <Link href="/register" className="text-blue-600 font-semibold hover:underline">
              Register here
            </Link>
          </div>
        </section>
      </main>
    </>
  );
};

export default LoginPage;