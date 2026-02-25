import Header from "@components/header";
import RecipesOverviewTable from "@components/Recipe/RecipesOverviewTable";
import { Recipe } from "@types";
import RecipeService from "@services/RecipeService";
import { cookies } from "next/headers";
import { useTranslations } from "next-intl";
import { getTranslations } from "next-intl/server";

type PageProps = {
  searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
};

const getData = async (mine: boolean): Promise<Recipe[]> => {
  const cookieStore = await cookies();
  const token = cookieStore.get("authToken")?.value;

  try {
    return await RecipeService.getAllRecipes(token, mine);
  } catch (error) {
    console.error("Failed to fetch recipes:", error);
    return [];
  }
};

export default async function RecipesPage({ searchParams }: PageProps) {
  const t = await getTranslations("Recipes");
  const resolvedSearchParams = await searchParams;
  const isMine = resolvedSearchParams?.mine === "true";

  const recipesData = await getData(isMine);

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        {recipesData.length <= 0 && !isMine && (
          <div className="mt-10">
            <h1 className="text-4xl font-bold text-gray-800">
              {t("unauthorized")}
            </h1>
            <p className="pt-6 text-red-700 font-semibold border-red-200">
              {t("must_login")}
            </p>
          </div>
        )}

        {recipesData.length <= 0 && isMine && (
          <p className="text-center py-10 text-gray-500">{t("no_recipes")}</p>
        )}

        {recipesData.length > 0 && (
          <section className="w-full max-w-6xl">
            <div>
              <h1 className="text-4xl font-bold text-gray-800">{t("title")}</h1>
              <p className="text-gray-600 mt-1">
                {isMine ? t("collection_mine") : t("collection_all")}
              </p>
              <RecipesOverviewTable
                initialRecipes={recipesData}
                isMineFilter={isMine}
              />
            </div>
          </section>
        )}
      </main>
    </>
  );
}
