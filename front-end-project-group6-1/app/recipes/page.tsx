import Header from "@components/header";
import RecipesOverviewTable from "@components/Recipe/RecipesOverviewTable";
import { Recipe } from "@types";
import RecipeService from "@services/RecipeService";

const getData = async (): Promise<Recipe[]> => {
  try {
    const recipes = await RecipeService.getAllRecipes();
    return recipes;
  } catch (error) {
    console.error("Failed to fetch recipes:", error);
    return [];
  }
};

export default async function RecipesPage() {
  const recipesData = await getData();

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center bg-gray-50">
        <section className="w-full max-w-6xl">
          <div className="pb-10">
            <h1 className="text-4xl font-bold text-gray-800">All Recipes</h1>
            <p className="text-gray-600 mt-1">
              A collection of all available recipes
            </p>
          </div>
          <RecipesOverviewTable initialRecipes={recipesData} />
        </section>
      </main>
    </>
  );
}
