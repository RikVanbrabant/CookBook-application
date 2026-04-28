"use client";
import { Cookbook } from "@types";
import { useRouter } from "next/navigation";
import React from "react";

type Props = {
  cookbooks: Array<Cookbook>;
  isMineFilter: boolean;
};

const CookbooksOverviewTable: React.FC<Props> = ({
  cookbooks,
  isMineFilter,
}: Props) => {
  const router = useRouter();

  const handleFilterChange = (mine: boolean) => {
    if (mine) {
      router.push("/cookbooks?mine=true");
    } else {
      router.push("/cookbooks");
    }
  };

  return (
    <>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-4xl font-bold text-gray-800">Cookbooks</h1>
        <div className="flex bg-gray-200 rounded-lg p-1">
          <button
            onClick={() => handleFilterChange(false)}
            className={`px-4 py-2 rounded-md text-sm font-medium transition-all ${
              !isMineFilter
                ? "bg-white text-gray-900 shadow-sm"
                : "text-gray-600 hover:text-gray-900"
            }`}
          >
            All Cookbooks
          </button>
          <button
            onClick={() => handleFilterChange(true)}
            className={`px-4 py-2 rounded-md text-sm font-medium transition-all ${
              isMineFilter
                ? "bg-white text-gray-900 shadow-sm"
                : "text-gray-600 hover:text-gray-900"
            }`}
          >
            My Cookbooks
          </button>
        </div>
      </div>

      {cookbooks && cookbooks.length > 0 ? (
      <div className="bg-white rounded-lg shadow-md">
        <table className="w-full text-left">
          <thead className="border-b border-gray-200">
            <tr>
              <th scope="col" className="px-6 py-4 font-semibold text-gray-700">
                Title
              </th>
              <th scope="col" className="px-6 py-4 font-semibold text-gray-700">
                Number of recipes
              </th>
            </tr>
          </thead>

          <tbody>
            {cookbooks.map((cookbook, index) => (
              <tr
                key={index}
                onClick={() => router.push(`/cookbooks/${cookbook.id}/recipes`)}
                className="bg-white border-b border-gray-200 cursor-pointer hover:bg-gray-50"
              >
                <td className="px-6 py-4 font-medium text-gray-900">
                  {cookbook.title}
                </td>
                <td className="px-6 py-4">{cookbook.numOfRecipes}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      ) : (
        <div className="text-center py-10 text-gray-500">
          No cookbooks found.
        </div>
      )}
    </>
  );
};

export default CookbooksOverviewTable;
