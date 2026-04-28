import Header from "@components/header";
import UserRegisterForm from "@components/login/UserRegusterForm";
import { getTranslations } from "next-intl/server";
import Link from "next/link";

const RegisterPage = async () => {
  const t = await getTranslations("Register");

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <h1 className="text-4xl font-bold text-gray-800 text-center pb-6">
          {t("title")}
        </h1>
        <section className="bg-gray-50 flex flex-col pt-6 items-center w-full">
          <UserRegisterForm />
          
          <div className="mt-6 text-sm text-gray-600">
            Do you already have an account?{" "}
            <Link href="/login" className="text-blue-600 font-semibold hover:underline">
              Log in here
            </Link>
          </div>
        </section>
      </main>
    </>
  );
};

export default RegisterPage;