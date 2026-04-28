"use client";
import React, { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { useTranslations } from "next-intl";
import CookbookService from "@services/CookbookService";
import { Cookbook } from "@types";
import CookbooksOverviewTable from "@components/Cookbook/CookbooksOverviewTable";
import Header from "@components/header";
import AddCookbookModal from "@components/Cookbook/AddCookbookModal";

export default function CookbooksPage() {
  const t = useTranslations("Cookbooks");
  const searchParams = useSearchParams();
  const isMine = searchParams.get("mine") === "true";

  const [cookbooks, setCookbooks] = useState<Cookbook[] | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchCookbooks = async () => {
    setIsLoading(true);
    try {
      const data = await CookbookService.getAllCookbooks(undefined, isMine);
      setCookbooks(data);
    } catch (error) {
      console.error("Error fetching cookbooks:", error);
      setCookbooks(null);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCookbooks();
  }, [isMine]);

  if (isLoading) {
    return (
      <>
        <Header />
        <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
          <p className="text-gray-500 mt-10">Loading...</p>
        </main>
      </>
    );
  }

  if (!cookbooks) {
    return (
      <>
        <Header />
        <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
          <div className="text-center mt-10">
            <h1 className="text-4xl font-bold text-gray-800">
              {t("unauthorized")}
            </h1>
            <p className="pt-6 text-red-700 font-semibold">{t("must_login")}</p>
          </div>
        </main>
      </>
    );
  }

  return (
    <>
      <Header />
      <main className="container mx-auto p-6 min-h-screen">
        <div className="flex justify-end mb-2">
          <button
            onClick={() => setIsModalOpen(true)}
            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 font-bold shadow-sm transition"
          >
            + New Cookbook
          </button>
        </div>

        <CookbooksOverviewTable cookbooks={cookbooks} isMineFilter={isMine} />

        <AddCookbookModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onCookbookAdded={fetchCookbooks}
        />
      </main>
    </>
  );
}
