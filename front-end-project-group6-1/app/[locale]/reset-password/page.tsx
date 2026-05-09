import { Suspense } from "react";
import ResetPasswordForm from "@components/forgotPassword/ResetPasswordForm";

export default function ResetPasswordPage() {
  return (
    <main className="flex flex-col items-center justify-center min-h-screen bg-gray-50 px-4">
      <Suspense fallback={<p className="text-gray-500">Loading…</p>}>
        <ResetPasswordForm />
      </Suspense>
    </main>
  );
}
