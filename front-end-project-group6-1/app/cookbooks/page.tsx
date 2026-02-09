import Header from "@components/header";
import CookbooksOverviewTable from "@components/Cookbook/CookbooksOverviewTable";
import { Cookbook } from "@types";
import CookbookService from "@services/CookbookService";

const getData = async (): Promise<Cookbook[]> => {
  const cookbooks = await CookbookService.getAllCookbooks();
  return cookbooks;
};

export default async function Home() {
  const cookbooksData = await getData();

  return (
    <>
      <Header />
      <main className="p-6 min-h-screen flex flex-col items-center">
        <section>
          <CookbooksOverviewTable cookbooks={cookbooksData} />
        </section>
      </main>
    </>
  );
}
