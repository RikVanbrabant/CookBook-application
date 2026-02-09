import Image from "next/image";
import styles from "../styles/page.module.css";
import Header from "@components/header";

export default function Home() {
  return (
    <html>
      <body>
        <Header />
        <main className="p-6 min-h-screen flex flex-col items-center"></main>
      </body>
    </html>
  );
}
