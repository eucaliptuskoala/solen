import { useState, useCallback } from "react";
import MoodIcon from "../MoodIcon";
import Badge from "../ui/Badge";

function InspireCardNavigator({ entries }) {
  const [index, setIndex] = useState(0);

  const go = useCallback(
    (dir) => {
      setIndex((prev) => (prev + dir + entries.length) % entries.length);
    },
    [entries.length]
  );

  if (entries.length === 0) return null;

  const entry = entries[index];
  const userName = entry.user?.name || "Anonymous";
  const initial = userName.charAt(0).toUpperCase();
  const categoryName = entry.practice?.categoryName;
  const practiceName = entry.practice?.name;
  const date = entry.date
    ? new Date(entry.date + "T00:00:00").toLocaleDateString("en-US", {
        month: "short",
        day: "numeric",
        year: "numeric",
      })
    : "";

  return (
    <>
      <div className="bg-solen-surface border border-solen-border rounded-[8px] p-[var(--space-xl)] max-w-[640px] mx-auto text-center transition-all duration-300" key={index}>
        <div className="flex items-center justify-between mb-[var(--space-lg)]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-solen-surface-soft border border-solen-border flex items-center justify-center font-mono text-[0.7rem] text-solen-muted">{initial}</div>
            <div>
              <div className="font-body text-[0.85rem] font-medium">{userName}</div>
              <div className="flex items-center gap-[2px] mt-[2px]">
                {categoryName && <Badge>{categoryName}</Badge>}
                {practiceName && <Badge>{practiceName}</Badge>}
              </div>
            </div>
          </div>
          {entry.mood && <MoodIcon mood={entry.mood} size={24} />}
        </div>
        <div className="text-[1.05rem] leading-[1.7]">{entry.content}</div>
        <div className="flex items-center justify-center pt-0 mt-[var(--space-lg)] border-none">
          <span className="font-mono text-[0.7rem] text-solen-muted">{date}</span>
        </div>
      </div>

      <div className="flex items-center justify-center gap-[var(--space-lg)] mt-[var(--space-xl)]">
        <button className="w-12 h-12 border border-solen-border rounded-full bg-solen-surface cursor-pointer flex items-center justify-center transition-all duration-200 text-solen-fg hover:border-solen-accent hover:bg-solen-accent-subtle" onClick={() => go(-1)}>
          <svg viewBox="0 0 16 16" width="16" height="16">
            <path d="M10 3L5 8l5 5" stroke="currentColor" strokeWidth="1.5" fill="none" strokeLinecap="round" />
          </svg>
        </button>
        <span className="font-mono text-[0.8rem] text-solen-muted">
          {index + 1} / {entries.length}
        </span>
        <button className="w-12 h-12 border border-solen-border rounded-full bg-solen-surface cursor-pointer flex items-center justify-center transition-all duration-200 text-solen-fg hover:border-solen-accent hover:bg-solen-accent-subtle" onClick={() => go(1)}>
          <svg viewBox="0 0 16 16" width="16" height="16">
            <path d="M6 3l5 5-5 5" stroke="currentColor" strokeWidth="1.5" fill="none" strokeLinecap="round" />
          </svg>
        </button>
      </div>
    </>
  );
}

export default InspireCardNavigator;
