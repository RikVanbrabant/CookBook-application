import Header from "@components/header";
import RecipesOverviewTable from "@components/Recipe/RecipesOverviewTable";
import { Cookbook, Recipe } from "@types";
import CookbookService from "@services/CookbookService";

type PageProps = {
  params: {
    cookbookId: string;
  };
};

const getData = async (
  cookbookId: string
): Promise<{
  cookbook: Cookbook;
  recipes: Recipe[];
  allCookbooks: Cookbook[];
}> => {
  try {
    const [cookbook, recipes, allCookbooks] = await Promise.all([
      CookbookService.getCookbookById(cookbookId),
      CookbookService.getRecipesByCookbookId(cookbookId),
      CookbookService.getAllCookbooks(),
    ]);
    return { cookbook, recipes, allCookbooks };
  } catch (error) {
    console.error("Error fetching data:", error);
    return { cookbook: null as any, recipes: [], allCookbooks: [] };
  }
};

const CookbookRecipesPage = async ({ params }: PageProps) => {
  const { cookbook, recipes, allCookbooks } = await getData(params.cookbookId);

  if (!cookbook) {
    return (
      <>
        <Header />
        <main className="p-6 min-h-screen flex flex-col items-center justify-center bg-gray-50">
          <h1 className="text-2xl text-red-600">Cookbook not found.</h1>
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
