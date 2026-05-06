"use client";

import Header from "@components/header";
import SecurityService, { UrlValidationResult } from "@services/SecurityService";
import { useState } from "react";

export default function UrlValidatorPage() {
  const [url, setUrl] = useState("");
  const [result, setResult] = useState<UrlValidationResult | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const submit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const validationResult = await SecurityService.validateUrl(url);
      setResult(validationResult);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Header />
      <main className="max-w-2xl mx-auto p-8">
        <h1 className="text-3xl font-bold text-gray-800">SSRF URL Validator</h1>
        <p className="mt-2 text-gray-600">
          This page mimics the protection we would use before downloading an external avatar or recipe image.
        </p>

        <form onSubmit={submit} className="mt-6 space-y-4">
          <label className="block font-semibold text-gray-700" htmlFor="url">
            URL to validate
          </label>
          <input
            id="url"
            type="url"
            value={url}
            onChange={(event) => setUrl(event.target.value)}
            placeholder="https://example.com/image.png"
            className="w-full border rounded-lg p-3"
            required
          />
          <button
            type="submit"
            disabled={loading}
            className="px-5 py-2 rounded-lg bg-red-600 text-white disabled:opacity-60"
          >
            {loading ? "Checking..." : "Validate URL"}
          </button>
        </form>

        {result && (
          <div className={`mt-6 rounded-lg border p-4 ${result.allowed ? "border-green-300 bg-green-50" : "border-red-300 bg-red-50"}`}>
            <p className="font-semibold">{result.allowed ? "ALLOW" : "REJECT"}</p>
            <p>{result.reason}</p>
          </div>
        )}

        {error && <p className="mt-6 text-red-700 font-semibold">{error}</p>}
      </main>
    </>
  );
}