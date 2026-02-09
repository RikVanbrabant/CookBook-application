"use client";
import { Cookbook } from "@types";
import { useRouter } from "next/navigation";
import React from "react";

type Props = {
  cookbooks: Array<Cookbook>;
};

const CookbooksOverviewTable: React.FC<Props> = ({ cookbooks }: Props) => {
  const router = useRouter();

  return (
    <>
      {cookbooks && (
        <table className="w-full border border-gray-200 text-sm text-left rtl:text-right text-white-500 text-black">
          <thead className="text-xs border-b border-gray-200 text-black-700 uppercase bg-white-50 bg-white-700">
            <tr>
              <th scope="col" className="px-6 py-3">
                Title
              </th>
              <th scope="col" className="px-6 py-3">
                Number of recipes
              </th>
            </tr>
          </thead>

          <tbody>
            {cookbooks.map((cookbook, index) => (
              <tr
                key={index}
                onClick={() => router.push(`/cookbooks/${cookbook.id}/recipes`)}
                className="bg-white border-b border-gray"
              >
                <td className="px-6 py-4">{cookbook.title}</td>
                <td className="px-6 py-4">{cookbook.numOfRecipes}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  );
};

export default CookbooksOverviewTable;
