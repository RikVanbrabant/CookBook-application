import { API_BASE } from "./apiBase";
import { Cookbook, Recipe } from "@types";

const getHeaders = (token?: string): HeadersInit => {
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Cookie"] = `authToken=${token}`;
  }
  return headers;
};

const getAllCookbooks = async (
  token?: string,
  mine: boolean = false
): Promise<Cookbook[] | null> => {
  const query = mine ? "?mine=true" : "";

  const response = await fetch(
    `${API_BASE}/cookbooks${query}`,
    {
      method: "GET",
      headers: getHeaders(token),
      cache: "no-store",
      credentials: "include",
    }
  );

  if (!response.ok) {
    return null;
  }

  return await response.json();
};

const getCookbookById = async (
  id: string,
  token?: string
): Promise<Cookbook> => {
  const response = await fetch(
    `${API_BASE}/cookbooks/${id}`,
    {
      method: "GET",
      headers: getHeaders(token),
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
  cookbookId: string,
  token?: string
): Promise<Recipe[]> => {
  const response = await fetch(
    `${API_BASE}/cookbooks/${cookbookId}/recipes`,
    {
      method: "GET",
      headers: getHeaders(token),
      cache: "no-store",
      credentials: "include",
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
const createCookbook = async (title: string): Promise<Cookbook> => {
  const response = await fetch(`${API_BASE}/cookbooks`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ title }),
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Failed to create cookbook");
  }

  return response.json();
};

const CookbookService = {
  getAllCookbooks,
  getCookbookById,
  getRecipesByCookbookId,
  createCookbook,
};

export default CookbookService;
