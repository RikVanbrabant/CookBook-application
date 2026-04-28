"use client";
import { useState, useEffect } from "react";
import { Recipe } from "@types";
import RecipeService from "@services/RecipeService";
import { useAuth } from "../../hooks/useAuth";

interface EditRecipeModalProps {
  isOpen: boolean;
  onClose: () => void;
  recipe: Recipe;
  onRecipeUpdated: () => void;
}

const EditRecipeModal: React.FC<EditRecipeModalProps> = ({
  isOpen,
  onClose,
  recipe,
  onRecipeUpdated,
}) => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    title: "",
    prepTime: 0,
    cookTime: 0,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (isOpen && recipe) {
      setFormData({
        title: recipe.title,
        prepTime: parseInt(recipe.prepTime) || 0,
        cookTime: parseInt(recipe.cookTime) || 0,
      });
      setError(null);
    }
  }, [isOpen, recipe]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await RecipeService.updateRecipe(recipe.id, formData, user?.token);
      onRecipeUpdated();
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to update");
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h2 className="text-2xl font-bold mb-4">Edit Recipe</h2>
        {error && <div className="text-red-600 mb-4">{error}</div>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Title</label>
            <input
              className="w-full border p-2 rounded"
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium">Prep Time</label>
              <input
                type="number"
                className="w-full border p-2 rounded"
                value={formData.prepTime}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    prepTime: parseInt(e.target.value),
                  })
                }
              />
            </div>
            <div>
              <label className="block text-sm font-medium">Cook Time</label>
              <input
                type="number"
                className="w-full border p-2 rounded"
                value={formData.cookTime}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    cookTime: parseInt(e.target.value),
                  })
                }
              />
            </div>
          </div>
          <div className="flex justify-end gap-2 mt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-gray-600"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              {loading ? "Saving..." : "Save Changes"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditRecipeModal;
