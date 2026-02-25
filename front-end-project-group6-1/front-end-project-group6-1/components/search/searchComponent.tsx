"use client";
import { Recipe } from "@types";
import { Ingredient } from "@types";
import React, { useState } from "react";
import { useDebounce } from "../../hooks/useDebounce";

type Props = {
  recipes: Array<Recipe>;
  ingredients: Array<Ingredient>;
};

const SearchComponent: React.FC<Props> = ({ recipes, ingredients }: Props) => {
  const [selectedIngredients, setSelectedIngredient] = useState<
    Array<Ingredient>
  >([]);
  const [searchString, setSearchString] = useState("");

  const debouncedSearchString = useDebounce(searchString, 500);

  const handleIngredientSelect = (ingredient: Ingredient) => {
    if (selectedIngredients.some((i) => i.id === ingredient.id)) {
      setSelectedIngredient(
        selectedIngredients.filter((i) => i.id !== ingredient.id)
      );
    } else {
      setSelectedIngredient([...selectedIngredients, ingredient]);
    }
    setSearchString("");
  };

  const filteredRecipes =
    selectedIngredients.length === 0
      ? recipes
      : recipes.filter((r) =>
          selectedIngredients.every((selectedIngredient) =>
            r.ingredients.some(
              (recipeIngredient) =>
                recipeIngredient.id === selectedIngredient.id
            )
          )
        );

  const filteredIngredients =
    debouncedSearchString.length === 0
      ? ingredients
      : ingredients.filter((i) =>
          i.name.toLowerCase().includes(debouncedSearchString.toLowerCase())
        );

  return (
    <>
      <div className="ml-96">
        <h1 className="text-4xl font-bold text-gray-800">Search Recipes</h1>
        <p className="text-gray-500 m-2">
          Here you can search recipes based on their ingredients.
        </p>
      </div>

      <div className="flex w-full gap-8 mt-6 ml-96">
        <div className="w-1/6 border p-4 rounded-lg shadow-sm">
          <h2 className="font-semibold text-gray-700 mb-2">Ingredients</h2>
          <div className="flex items-center gap-2 mb-3">
            <img src="/search.png" alt="icon" width={20} />
            <input
              type="text"
              placeholder="ingredient..."
              value={searchString}
              onChange={(e) => setSearchString(e.target.value)}
              className="border p-1 rounded w-full"
            />
          </div>
          {filteredIngredients.map((ingredient) => {
            return (
              <button
                key={ingredient.id}
                className={
                  selectedIngredients.some((i) => i.id === ingredient.id)
                    ? "pl-1 block w-full text-left text-gray-900 font-semibold py-1 border border-blue-500 bg-blue-100 rounded-sm last:border-b-0"
                    : "pl-1 block w-full text-left text-gray-600 py-1 border-b last:border-b-0"
                }
                onClick={() => handleIngredientSelect(ingredient)}
              >
                {ingredient.name}
              </button>
            );
          })}
        </div>

        <div className="flex-1">
          <table className="w-6/12 text-left">
            <thead className="border-b border-gray-200">
              <tr>
                <th className="px-6 py-4 font-semibold text-gray-900">Title</th>
                <th className="px-6 py-4 font-semibold text-gray-900">
                  Prep time
                </th>
                <th className="px-6 py-4 font-semibold text-gray-900">
                  Cook time
                </th>
              </tr>
            </thead>

            <tbody>
              {filteredRecipes.map((recipe) => (
                <tr
                  key={recipe.id}
                  className="border-b border-gray-200 hover:bg-gray-50 last:border-b-0"
                >
                  <td className="px-6 py-4 font-medium text-gray-700">
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
      </div>
    </>
  );
};

export default SearchComponent;
