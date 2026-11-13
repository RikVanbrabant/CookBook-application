// In de browser gaan alle API-calls via /api (Next.js-rewrite naar de backend),
// zodat de authToken-cookie op hetzelfde domein als de frontend staat.
// Op de server (server components) wordt de backend rechtstreeks aangesproken.
export const API_BASE =
  typeof window === "undefined"
    ? process.env.BACKEND_URL || "http://localhost:3000"
    : "/api";
