import { useState, useCallback } from "react";

export default function useToast() {
  const [toast, setToast] = useState(null);

  const showToast = useCallback((msg) => {
    setToast(msg);
    setTimeout(() => setToast(null), 2500);
  }, []);

  return { toast, showToast };
}
