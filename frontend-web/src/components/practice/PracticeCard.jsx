import Badge from "../ui/Badge";

function PracticeCard({ practice, onDone, onDelete }) {
  const disabled = practice.checkedInToday;

  return (
    <div className={`bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-lg)] transition-all duration-200 hover:border-solen-accent-dim${disabled ? " opacity-65" : ""}`}>
      <div className="flex items-start justify-between gap-2 mb-2">
        <div>
          <div className="font-display text-[1.1rem] leading-tight">{practice.name}</div>
          <div className="flex items-center gap-2 flex-wrap mt-2">
            {practice.categoryName && <Badge>{practice.categoryName}</Badge>}
            <span className="inline-flex items-center gap-1 font-mono text-[0.75rem] text-solen-accent">
              <svg viewBox="0 0 16 16" width="14" height="14">
                <circle cx="8" cy="8" r="6" fill="oklch(68% 0.16 75 / 0.2)" />
                <circle cx="8" cy="8" r="3" fill="var(--color-solen-accent)" />
              </svg>
              {practice.streak} days
            </span>
          </div>
        </div>
        <div className="flex items-center gap-1">
          <button
            className={`flex items-center justify-center w-10 h-10 rounded-full border-2 border-solen-border bg-transparent cursor-pointer transition-all duration-250 shrink-0 hover:border-solen-accent hover:bg-solen-accent-subtle disabled:opacity-35 disabled:cursor-not-allowed disabled:hover:border-solen-border disabled:hover:bg-transparent${disabled ? " border-solen-accent bg-solen-accent" : ""}`}
            onClick={() => onDone(practice.id)}
            title={disabled ? "Already checked in today" : "Mark done for today"}
            disabled={disabled}
          >
            <svg viewBox="0 0 16 16" width="14" height="14" stroke={disabled ? "var(--color-solen-surface)" : "var(--color-solen-muted)"} strokeWidth="2" fill="none" strokeLinecap="round">
              <path d="M3 8l3 4 7-7" />
            </svg>
          </button>
          <button
            className="w-8 h-8 flex items-center justify-center border-none bg-transparent cursor-pointer rounded-[4px] text-solen-muted transition-all duration-150 hover:bg-solen-surface-soft hover:text-solen-fg"
            onClick={() => onDelete(practice)}
            title="Delete practice"
          >
            &bull;&bull;&bull;
          </button>
        </div>
      </div>
      {practice.description && (
        <div className="font-body text-[0.875rem] leading-[1.5] text-solen-muted">
          {practice.description}
        </div>
      )}
    </div>
  );
}

export default PracticeCard;
