import { Ingredient } from "@types";

const getHeaders = (token?: string): HeadersInit => {
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Cookie"] = `authToken=${token}`;
  }
  return headers;
};

const getAllIngredients = async (
    token?: string,
): Promise<Ingredient[]> => {
    const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/ingredients`,
        {
            method: "GET",
            headers: getHeaders(token),
            cache: "no-store",
            credentials: "include",   
        }
    );

    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw new Error(error?.message || "Failed to fetch ingredients.");
    }

    return response.json();
}

const IngredientService = {
    getAllIngredients,
}

export default IngredientService;