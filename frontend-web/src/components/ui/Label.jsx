function Label({ htmlFor, children }) {
  return (
    <label htmlFor={htmlFor} className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted mb-[var(--space-sm)]">
      {children}
    </label>
  );
}

export default Label;
