"use client";
import RecipeService from "@services/RecipeService";
import { Recipe } from "@types";
import { useAuth } from "hooks/useAuth";
import { useEffect, useState } from "react";

interface deleteRecipeModalProps {
    isOpen: boolean;
    onClose: () => void;
    recipe: Recipe;
    onRecipeDeleted: () => void;
}

const DeleteRecipeModal: React.FC<deleteRecipeModalProps> = ({
    isOpen,
    onClose,
    recipe,
    onRecipeDeleted,
}) => {
    const { user } = useAuth();
    const [formData, setFormData] = useState({
    title: "",
    message: "",
  });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (isOpen && recipe) {
          setFormData({
            title: recipe.title,
            message: "Are you sure you want to permanently delete this recipe?"
          });
          setError(null);
        }
    }, [isOpen, recipe]);

      const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        try {
            await RecipeService.deleteRecipeFromCookbook(recipe.id, user?.token);
            onRecipeDeleted();
            onClose();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Failed to delete");
        } finally {
        setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
     <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h2 className="text-2xl font-bold mb-4">{formData.title}</h2>
        {error && <div className="text-red-600 mb-4">{error}</div>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label >
              {formData.message}
            </label>
          </div>
          <div className="flex justify-end gap-2 mt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-gray-600"
            >
              No
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              {loading ? "Deleting..." : "Yes"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default DeleteRecipeModal;