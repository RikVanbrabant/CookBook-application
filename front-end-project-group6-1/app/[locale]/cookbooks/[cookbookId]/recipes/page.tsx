import Header from "@components/header";
import RecipesOverviewTable from "@components/Recipe/RecipesOverviewTable";
import { Cookbook, Recipe } from "@types";
import CookbookService from "@services/CookbookService";
import { cookies } from "next/headers";

type PageProps = {
  params: Promise<{
    cookbookId: string;
  }>;
};

const getData = async (
  cookbookId: string,
  token: string | undefined
): Promise<{
  cookbook: Cookbook;
  recipes: Recipe[];
  allCookbooks: Cookbook[] | null;
}> => {
  try {
    const [cookbook, recipes, allCookbooks] = await Promise.all([
      CookbookService.getCookbookById(cookbookId, token),
      CookbookService.getRecipesByCookbookId(cookbookId, token),
      CookbookService.getAllCookbooks(token),
    ]);
    return { cookbook, recipes, allCookbooks };
  } catch (error) {
    console.error("Error fetching data:", error);
    return { cookbook: null as any, recipes: [], allCookbooks: [] };
  }
};

const CookbookRecipesPage = async ({ params }: PageProps) => {
  const { cookbookId } = await params;

  const cookieStore = await cookies();
  const token = cookieStore.get("authToken")?.value;

  const { cookbook, recipes } = await getData(cookbookId, token);

  if (!cookbook) {
    return (
      <>
        <Header />
        <main className="p-6 min-h-screen flex flex-col items-center justify-center bg-gray-50">
          <h1 className="text-2xl text-red-600">Cookbook not found.</h1>
          <p className="text-gray-500 mt-2">
            You may be unauthorized or the cookbook does not exist.
          </p>
        </main>
      </>
    );
  }

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <section className="w-full max-w-6xl">
          <RecipesOverviewTable initialRecipes={recipes} cookbook={cookbook} />
        </section>
      </main>
    </>
  );
};

export default CookbookRecipesPage;
