import { Recipe } from "@types";

const getAllRecipes = async (): Promise<Recipe[]> => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/recipes`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    cache: "no-store",
    credentials: "include",
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "No recipes found");
  }

  return response.json();
};
const addRecipeToCookbook = async (
  cookbookId: string,
  recipe: Omit<Recipe, "id">
): Promise<Recipe> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/cookbooks/${cookbookId}/recipes`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(recipe),
      credentials: "include",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "Failed to add recipe");
  }

  return response.json();
};
const RecipeService = {
  getAllRecipes,
  addRecipeToCookbook,
};

export default RecipeService;
