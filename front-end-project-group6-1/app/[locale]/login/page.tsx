import Header from "@components/header";
import UserLoginForm from "@components/login/UserLoginForm";
import { getTranslations } from "next-intl/server";

const LoginPage = async () => {
  const t = await getTranslations("Login");

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <h1 className="text-4xl font-bold text-gray-800 text-center pb-6">
          {t("title")}
        </h1>
        <section className="bg-gray-50 flex flex-col pt-6">
          <UserLoginForm />
        </section>
      </main>
    </>
  );
};

export default LoginPage;
