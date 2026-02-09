export type Recipe = {
  id: number;
  title: string;
  prepTime: string;
  cookTime: string;
};
export type User = {
  id?: number;
  username?: string;
  password?: string;
  token?: string;
};

export type Cookbook = {
  id: number;
  title: string;
  numOfRecipes: number;
  recipes: Recipe[];
};

export type StatusMessage = {
  message: string;
  type: "error" | "success";
};
