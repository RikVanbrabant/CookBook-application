"use client";
import { useState } from "react";
import useSWR from "swr";
import { Cookbook, Recipe } from "@types";
import CookbookService from "@services/CookbookService";
import RecipeService from "@services/RecipeService";
import AddRecipeModal from "./Add_recipe";
import { useRouter } from "next/navigation";
import { useAuth } from "../../hooks/useAuth";
import EditRecipeModal from "./EditRecipeModal";
import DeleteRecipeModal from "./deleteRecipe";

const fetchCookbookRecipes = (cookbookId: string) =>
  CookbookService.getRecipesByCookbookId(cookbookId);

const fetchAllRecipes = () => RecipeService.getAllRecipes();
const fetchMyRecipes = () => RecipeService.getAllRecipes(undefined, true);

interface RecipesOverviewTableProps {
  initialRecipes: Recipe[];
  cookbook?: Cookbook;
  isMineFilter?: boolean;
}

const RecipesOverviewTable: React.FC<RecipesOverviewTableProps> = ({
  initialRecipes,
  cookbook,
  isMineFilter = false,
}) => {
  const router = useRouter();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { user } = useAuth();
  const [editingRecipe, setEditingRecipe] = useState<Recipe | null>(null);
  const [deletingRecipe, setDeletingRecipe] = useState<Recipe | null>(null);

  let swrKey: string | null = "all-recipes";
  // FIX 1: Check for lowercase "admin" (matching your Java backend)
  const isAdmin = user && user.role === "admin";
  let fetcher = fetchAllRecipes;

  if (cookbook) {
    swrKey = cookbook.id.toString();
    fetcher = () => fetchCookbookRecipes(cookbook.id.toString());
  } else if (isMineFilter) {
    swrKey = "my-recipes";
    fetcher = fetchMyRecipes;
  }

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
  const handleRecipeUpdated = () => {
    setEditingRecipe(null);
    mutate();
  };
  const handleRecipeDeleted = () => {
    setDeletingRecipe(null);
    mutate();
  };

  const isOwner = cookbook && user && user.username === cookbook.owner;

  const handleFilterChange = (mine: boolean) => {
    if (mine) {
      router.push("/recipes?mine=true");
    } else {
      router.push("/recipes");
    }
  };

  if (error) return;
  if (!recipes) return <div>Loading...</div>;

  return (
    <>
      <div className="mb-6 flex justify-between items-end">
        {cookbook ? (
          <>
            <div>
              <h1 className="text-4xl font-bold text-gray-800">
                {cookbook.title}
              </h1>
              <p className="text-gray-600 mt-1">
                {recipes.length} {recipes.length === 1 ? "recipe" : "recipes"}{" "}
                in this collection
              </p>
            </div>
            {/* Note: Only the Owner sees the Add button. If Admins should too, add "|| isAdmin" here */}
            {user && user.username === cookbook.owner && (
              <button
                onClick={() => setIsModalOpen(true)}
                className="bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition-colors font-medium whitespace-nowrap"
              >
                + Add Recipe
              </button>
            )}
          </>
        ) : (
          <div className="w-full flex justify-end">
            <div className="flex bg-gray-200 rounded-lg p-1">
              <button
                onClick={() => handleFilterChange(false)}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-all ${
                  !isMineFilter
                    ? "bg-white text-gray-900 shadow-sm"
                    : "text-gray-600 hover:text-gray-900"
                }`}
              >
                All Recipes
              </button>
              <button
                onClick={() => handleFilterChange(true)}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-all ${
                  isMineFilter
                    ? "bg-white text-gray-900 shadow-sm"
                    : "text-gray-600 hover:text-gray-900"
                }`}
              >
                My Recipes
              </button>
            </div>
          </div>
        )}
      </div>

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
              {/* FIX 2: Only show Modify column for Owner (Admins can't edit in your code) */}
              {isOwner && (
                <th className="px-6 py-4 font-semibold text-gray-700">
                  Modify
                </th>
              )}
              {/* FIX 3: Show Delete column for Owner OR Admin */}
              {(isOwner || isAdmin) && (
                <th className="px-6 py-4 font-semibold text-gray-700">
                  Delete
                </th>
              )}
            </tr>
          </thead>
          <tbody>
            {recipes.length > 0 ? (
              recipes.map((recipe) => (
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

                  {isOwner && (
                    <td className="px-6 py-4">
                      <button
                        onClick={() => setEditingRecipe(recipe)}
                        className="text-blue-600 hover:text-blue-800 font-medium"
                      >
                        Edit
                      </button>
                    </td>
                  )}

                  {/* FIX 4: Consolidate Delete Logic into one cell */}
                  {(isOwner || isAdmin) && (
                    <td className="px-6 py-4">
                      {isOwner ? (
                        <button
                          onClick={() => setDeletingRecipe(recipe)}
                          className="text-blue-600 hover:text-blue-800 font-medium"
                        >
                          Remove
                        </button>
                      ) : (
                        <button
                          onClick={() => setDeletingRecipe(recipe)}
                          className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600"
                        >
                          Delete (Admin)
                        </button>
                      )}
                    </td>
                  )}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5} className="px-6 py-8 text-center text-gray-500">
                  No recipes found.
                </td>
              </tr>
            )}
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
      {editingRecipe && (
        <EditRecipeModal
          isOpen={!!editingRecipe}
          onClose={() => setEditingRecipe(null)}
          recipe={editingRecipe}
          onRecipeUpdated={handleRecipeUpdated}
        />
      )}
      {deletingRecipe && (
        <DeleteRecipeModal
          isOpen={!!deletingRecipe}
          onClose={() => setDeletingRecipe(null)}
          recipe={deletingRecipe}
          onRecipeDeleted={handleRecipeDeleted}
        />
      )}
    </>
  );
};

export default RecipesOverviewTable;
