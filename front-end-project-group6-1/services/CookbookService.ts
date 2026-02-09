import { Cookbook, Recipe } from "@types";

const getAllCookbooks = async (): Promise<Cookbook[]> => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/cookbooks`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    cache: "no-store",
    credentials: "include",
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "No cookbooks found");
  }

  return response.json();
};

const getCookbookById = async (id: string): Promise<Cookbook> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/cookbooks/${id}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-store",
      credentials: "include",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || `No cookbook with id ${id} found`);
  }

  return response.json();
};

const getRecipesByCookbookId = async (
  cookbookId: string
): Promise<Recipe[]> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/cookbooks/${cookbookId}/recipes`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-store",
    }
  );

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(
      error?.message ||
        `No cookbook or recipes found for cookbook with id ${cookbookId}`
    );
  }

  return response.json();
};

const CookbookService = {
  getAllCookbooks,
  getCookbookById,
  getRecipesByCookbookId,
};

export default CookbookService;
