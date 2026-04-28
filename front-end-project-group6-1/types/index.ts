export type Recipe = {
  id: number;
  title: string;
  prepTime: string;
  cookTime: string;
  ingredients: Ingredient[];
};

export type User = {
  firstName?: string;
  lastName?: string;
  fullname?: string;
  email?: string;
  username?: string;
  password?: string;
  role?: string;
  token?: string;
};

export type Cookbook = {
  id: number;
  title: string;
  numOfRecipes: number;
  recipes: Recipe[];
  owner: string;
};
export type Profile = {
  id: number;
  fullName: string;
  about: string;
};
export type AuthContextType = {
  user: User | null;
  login: (userData: User) => void;
  logout: () => void;
};

export type StatusMessage = {
  message: string;
  type: "error" | "success";
};

export type Ingredient = {
  id: number;
  name: string;
};
