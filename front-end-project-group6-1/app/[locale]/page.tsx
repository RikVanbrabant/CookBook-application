import Header from "@components/header";
import { getTranslations } from "next-intl/server";

export default async function Home() {
  const t = await getTranslations("Home");

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center">
        <h1 className="text-4xl font-bold text-gray-800 pb-6">{t("title")}</h1>
        <p>{t("p1")}</p>
        <p>{t("p2")}</p>
        <p>{t("p3")}</p>
      </main>
    </>
  );
}
