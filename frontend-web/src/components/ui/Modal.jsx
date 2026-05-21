import { useEffect } from "react";

function Modal({ isOpen, onClose, children, maxWidth = "500px" }) {
  useEffect(() => {
    if (!isOpen) return;
    const handleKey = (e) => { if (e.key === "Escape") onClose(); };
    document.addEventListener("keydown", handleKey);
    return () => document.removeEventListener("keydown", handleKey);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/20 flex items-center justify-center z-200" onClick={onClose}>
      <div
        className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)] w-[90%] shadow-[0_16px_48px_rgb(0_0_0_/_0.08)]"
        style={{ maxWidth }}
        onClick={(e) => e.stopPropagation()}
      >
        {children}
      </div>
    </div>
  );
}

export default Modal;
