import Image from "next/image";
import styles from "../styles/page.module.css";
import Header from "@components/header";
import SearchComponent from "@components/search/searchComponent"
import { cookies } from "next/headers";
import RecipeService from "@services/RecipeService";
import IngredientService from "@services/IngredientService";
import { Ingredient, Recipe } from "@types";

type PageProps = { 
  recipes: Recipe[],
  ingredients: Ingredient[],
}

const getData = async ():Promise<{ recipes: Recipe[]; ingredients: Ingredient[] }> => {
  const cookieStore = await cookies();
  const token = cookieStore.get("authToken")?.value;

  try {
    const recipes = await RecipeService.getAllRecipes(token);
    const ingredients = await IngredientService.getAllIngredients(token);
    console.log(recipes)
    console.log(ingredients)
    return { recipes, ingredients };
  } catch (error) {
    console.error("Failed to fetch recipes and/or ingredients:", error);
    return { recipes: [], ingredients: []};
  }
};

export default async function SearchRecipesPage() {
  const { recipes, ingredients } = await getData();
  
  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-start">
        
        {recipes.length > 0 && ingredients.length > 0 && (
          <SearchComponent recipes={recipes} ingredients={ingredients}/>
        )}
        
        {recipes.length <= 0 && ingredients.length <= 0 && (
          <div className="text-center w-full mt-10">
            <h1 className="text-4xl font-bold text-gray-800">401: Unauthorized</h1>
            <p className="pt-6 text-red-700 font-semibold border-red-200">You must be logged in to view this page.</p>
          </div>
        )}
      </main>
    </>
  );
}
