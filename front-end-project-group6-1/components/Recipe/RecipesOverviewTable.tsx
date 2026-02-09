"use client";
import { useState } from "react";
import useSWR from "swr";
import { Cookbook, Recipe } from "@types";
import CookbookService from "@services/CookbookService";
import RecipeService from "@services/RecipeService";
import AddRecipeModal from "./Add_recipe";

const fetchCookbookRecipes = (cookbookId: string) =>
  CookbookService.getRecipesByCookbookId(cookbookId);
const fetchAllRecipes = () => RecipeService.getAllRecipes();

interface RecipesOverviewTableProps {
  initialRecipes: Recipe[];
  cookbook?: Cookbook;
}

const RecipesOverviewTable: React.FC<RecipesOverviewTableProps> = ({
  initialRecipes,
  cookbook,
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const swrKey = cookbook ? cookbook.id.toString() : "all-recipes";
  const fetcher = cookbook ? fetchCookbookRecipes : fetchAllRecipes;

  const {
    data: recipes,
    error,
    mutate,
  } = useSWR(swrKey, fetcher, {
    fallbackData: initialRecipes,
  });

  const handleRecipeAdded = () => {
    setIsModalOpen(false);
    mutate();
  };

  if (error) return <div>Failed to load recipes.</div>;
  if (!recipes) return <div>Loading...</div>;

  return (
    <>
      {cookbook && (
        <div className="mb-6 flex justify-between items-center">
          <div>
            <h1 className="text-4xl font-bold text-gray-800">
              {cookbook.title}
            </h1>
            <p className="text-gray-600 mt-1">
              {recipes.length} {recipes.length === 1 ? "recipe" : "recipes"} in
              this collection
            </p>
          </div>
          <button
            onClick={() => setIsModalOpen(true)}
            className="bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition-colors font-medium whitespace-nowrap"
          >
            + Add Recipe
          </button>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md">
        <table className="w-full text-left">
          <thead className="border-b border-gray-200">
            <tr>
              <th scope="col" className="px-6 py-4 font-semibold text-gray-700">
                Title
              </th>
              <th scope="col" className="px-6 py-4 font-semibold text-gray-700">
                Prep Time
              </th>
              <th scope="col" className="px-6 py-4 font-semibold text-gray-700">
                Cook Time
              </th>
            </tr>
          </thead>
          <tbody>
            {recipes.map((recipe) => (
              <tr
                key={recipe.id}
                className="border-b border-gray-200 hover:bg-gray-50 last:border-b-0"
              >
                <td className="px-6 py-4 font-medium text-gray-900">
                  {recipe.title}
                </td>
                <td className="px-6 py-4 text-gray-600">
                  {recipe.prepTime} min
                </td>
                <td className="px-6 py-4 text-gray-600">
                  {recipe.cookTime} min
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {isModalOpen && cookbook && (
        <AddRecipeModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          cookbookId={cookbook.id.toString()}
          onRecipeAdded={handleRecipeAdded}
        />
      )}
    </>
  );
};

export default RecipesOverviewTable;
