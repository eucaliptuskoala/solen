function PageHeader({ eyebrow, title, className = "" }) {
  return (
    <div className={`mb-[var(--space-lg)] animate-[fade-in_0.5s_ease_both] ${className}`}>
      <span className="font-mono text-xs tracking-[0.05em] uppercase text-solen-muted block mb-[var(--space-sm)]">
        {eyebrow}
      </span>
      <h1 className="font-display text-[length:var(--fs-heading)] leading-[1.15] tracking-[-0.015em] font-normal">{title}</h1>
    </div>
  );
}

export default PageHeader;
