export type UrlValidationResult = {
  allowed: boolean;
  reason: string;
};

const validateUrl = async (url: string): Promise<UrlValidationResult> => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/security/url-validate`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ url }),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error?.message || "URL validation failed");
  }

  return response.json();
};

const SecurityService = { validateUrl };
export default SecurityService;