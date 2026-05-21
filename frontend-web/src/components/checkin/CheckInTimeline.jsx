import { useState, useMemo } from "react";
import CheckInTimelineEntry from "./CheckInTimelineEntry";
import { isThisWeek, isThisMonth } from "../../utils/dates";
import Button from "../ui/Button";
import PageHeader from "../ui/PageHeader";

const filters = [
  { key: "all", label: "All" },
  { key: "this-week", label: "This week" },
  { key: "this-month", label: "This month" },
];

function CheckInTimeline({ entries, onEdit, onDelete, onCreate }) {
  const [activeFilter, setActiveFilter] = useState("all");

  const filtered = useMemo(() => {
    if (activeFilter === "this-week") return entries.filter((e) => isThisWeek(e.date));
    if (activeFilter === "this-month") return entries.filter((e) => isThisMonth(e.date));
    return entries;
  }, [entries, activeFilter]);

  const sorted = [...filtered].sort((a, b) => new Date(b.date) - new Date(a.date));

  return (
    <>
      <div className="flex items-center justify-between mb-[var(--space-xl)] flex-wrap gap-[var(--space-md)] animate-[fade-in_0.5s_ease_both]">
        <PageHeader eyebrow="Your journal" title="Check-Ins" className="mb-0" />
        <div className="flex gap-[var(--space-sm)] flex-wrap">
          {filters.map((f) => (
            <button
              key={f.key}
              className={`px-4 py-[6px] border border-solen-border rounded-full text-[0.8rem] font-body cursor-pointer transition-all duration-150 ${activeFilter === f.key ? "bg-solen-accent text-[var(--color-solen-surface)]" : "bg-solen-surface text-solen-muted hover:border-solen-accent-dim"}`}
              onClick={() => setActiveFilter(f.key)}
            >
              {f.label}
            </button>
          ))}
        </div>
        <Button variant="secondary" onClick={onCreate}>+ New check-in</Button>
      </div>

      {sorted.length === 0 ? (
        <div className="text-center py-24 text-solen-muted">
          <p className="font-body text-[length:var(--fs-body)] leading-relaxed text-solen-muted mb-[var(--space-md)]">No check-ins yet</p>
          <p className="font-body text-sm leading-relaxed text-solen-muted">
            Your daily reflections will appear here once you start checking in.
          </p>
        </div>
      ) : (
        <div className="timeline-line animate-[fade-in_0.5s_ease_0.1s_both]">
          {sorted.map((entry) => (
            <CheckInTimelineEntry
              key={entry.id}
              entry={entry}
              onEdit={onEdit}
              onDelete={onDelete}
            />
          ))}
        </div>
      )}
    </>
  );
}

export default CheckInTimeline;
