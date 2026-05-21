import MoodIcon from "../MoodIcon";
import Button from "../ui/Button";

function InspireCard({ entry }) {
  const userName = entry.user?.name || "Anonymous";
  const initial = userName.charAt(0).toUpperCase();
  const categoryName = entry.habit?.categoryName;
  const date = entry.date ? new Date(entry.date + "T00:00:00").toLocaleDateString("en-US", { month: "short", day: "numeric", year: "numeric" }) : "";

  return (
    <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-lg)] transition-all duration-200 hover:border-solen-accent-dim">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-solen-surface-soft border border-solen-border flex items-center justify-center font-mono text-[0.7rem] text-solen-muted">{initial}</div>
          <div>
            <div className="font-body text-[0.85rem] font-medium">{userName}</div>
            <div className="flex items-center gap-[2px] mt-[2px]">
              {categoryName && <span className="inline-flex items-center px-[10px] py-[2px] font-mono text-[0.7rem] tracking-[0.03em] rounded-full border border-solen-border text-solen-muted bg-solen-surface">{categoryName}</span>}
            </div>
          </div>
        </div>
        {entry.mood && <span className="w-5 h-5"><MoodIcon mood={entry.mood} size={20} /></span>}
      </div>
      <div className="text-[0.95rem] leading-[1.7]">
        {entry.content}
      </div>
      <div className="flex items-center justify-between border-t border-solen-border mt-[var(--space-md)] pt-[var(--space-md)]">
        <span className="font-mono text-[0.7rem] text-solen-muted">{date}</span>
        <Button variant="ghost" className="hover:text-solen-accent">
          <svg viewBox="0 0 16 16" width="14" height="14">
            <path d="M8 3.5C6.5 1 3 1 3 4.5 3 7 8 11 8 11s5-4 5-6.5C13 1 9.5 1 8 3.5z" stroke="currentColor" strokeWidth="1.2" fill="none" />
          </svg>
        </Button>
      </div>
    </div>
  );
}

export default InspireCard;
