import { Recipe } from "@types";

const getHeaders = (token?: string): HeadersInit => {
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Cookie"] = `authToken=${token}`;
  }
  return headers;
};

const getAllRecipes = async (
  token?: string,
  mine: boolean = false
): Promise<Recipe[]> => {
  const query = mine ? "?mine=true" : "";

  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/recipes${query}`,
    {
      method: "GET",
      headers: getHeaders(token),
      cache: "no-store",
      credentials: "include",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "No recipes found");
  }

  return response.json();
};

const addRecipeToCookbook = async (
  cookbookId: string,
  recipe: Omit<Recipe, "id">,
  token?: string
): Promise<Recipe> => {
  console.log("Sending recipe:", recipe);
  console.log("Sending JSON:", JSON.stringify(recipe));
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/cookbooks/${cookbookId}/recipes`,
    {
      method: "POST",
      headers: getHeaders(token),
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

const updateRecipe = async (
  id: number,
  recipe: { title: string; prepTime: number; cookTime: number },
  token?: string
): Promise<Recipe> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/recipes/${id}`,
    {
      method: "PUT",
      headers: getHeaders(token),
      body: JSON.stringify(recipe),
      credentials: "include",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "Failed to update recipe");
  }

  return response.json();
};

const deleteRecipeFromCookbook = async (
  recipeId: number,
  token?: string
): Promise<void> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/recipes/${recipeId}`,
    {
      method: "DELETE",
      headers: getHeaders(token),
      credentials: "include",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "Failed to delete recipe");
  }
};

const RecipeService = {
  getAllRecipes,
  addRecipeToCookbook,
  updateRecipe,
  deleteRecipeFromCookbook,
};

export default RecipeService;
