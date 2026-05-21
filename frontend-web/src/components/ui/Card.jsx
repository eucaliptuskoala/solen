function Card({ className = "", children, ...props }) {
  return (
    <div className={`bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-lg)] ${className}`} {...props}>
      {children}
    </div>
  );
}

export default Card;
