"use client";

import React from "react";
import { useRouter, usePathname, useSearchParams } from "next/navigation";
import { useLocale } from "next-intl";

const Language: React.FC = () => {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const locale = useLocale();

  const handleLanguageChange = (event: { target: { value: string } }) => {
    const newLocale = event.target.value;
    const newPath = pathname.replace(`/${locale}`, `/${newLocale}`);
    const queryString = searchParams.toString();
    const finalPath = queryString ? `${newPath}?${queryString}` : newPath;

    router.push(finalPath);
  };

  return (
    <div className="ml-4">
      <select
        id="language"
        className="bg-transparent text-white border border-white/30 rounded px-2 py-1 text-sm focus:outline-none focus:bg-red-600 cursor-pointer"
        value={locale}
        onChange={handleLanguageChange}
      >
        <option value="en" className="text-black">
          EN
        </option>
        <option value="nl" className="text-black">
          NL
        </option>
      </select>
    </div>
  );
};

export default Language;
