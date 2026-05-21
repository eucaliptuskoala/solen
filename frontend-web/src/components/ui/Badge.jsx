function Badge({ children }) {
  return (
    <span className="inline-flex items-center px-[10px] py-[2px] font-mono text-[0.7rem] tracking-[0.03em] rounded-full border border-solen-border text-solen-muted bg-solen-surface">
      {children}
    </span>
  );
}

export default Badge;
