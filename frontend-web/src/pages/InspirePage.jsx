import { useState, useEffect, useCallback } from "react";
import CheckInAPI from "../apis/CheckInAPI";
import InspireCard from "../components/inspire/InspireCard";
import InspireCardNavigator from "../components/inspire/InspireCardNavigator";
import PageHeader from "../components/ui/PageHeader";

function InspirePage() {
  const [entries, setEntries] = useState([]);
  const [view, setView] = useState("feed");

  const fetchEntries = useCallback(() => {
    CheckInAPI.getFyp()
      .then(setEntries)
      .catch((err) => console.error("Failed to fetch entries", err));
  }, []);

  useEffect(() => { fetchEntries(); }, [fetchEntries]);

  const handleToggleLike = useCallback(async (id) => {
    const result = await CheckInAPI.toggleLike(id);
    setEntries((prev) =>
      prev.map((e) =>
        e.id === id ? { ...e, likeCount: result.likeCount, isLikedByCurrentUser: result.liked } : e
      )
    );
    return result;
  }, []);

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <div className="flex items-center justify-between mb-[var(--space-lg)] flex-wrap gap-4 animate-[fade-in_0.5s_ease_both]">
        <PageHeader eyebrow="Community" title="Inspire" className="mb-0" />
        <div className="flex border border-solen-border rounded-[8px] overflow-hidden">
          <button
            className={`px-4 py-2 border-none font-body text-[0.8rem] cursor-pointer transition-all duration-150 [&:not(:last-child)]:border-r [&:not(:last-child)]:border-solen-border ${view === "feed" ? "bg-solen-accent text-[var(--color-solen-surface)]" : "bg-solen-surface text-solen-muted hover:text-solen-fg"}`}
            onClick={() => setView("feed")}
          >
            Feed
          </button>
          <button
            className={`px-4 py-2 border-none font-body text-[0.8rem] cursor-pointer transition-all duration-150 [&:not(:last-child)]:border-r [&:not(:last-child)]:border-solen-border ${view === "card" ? "bg-solen-accent text-[var(--color-solen-surface)]" : "bg-solen-surface text-solen-muted hover:text-solen-fg"}`}
            onClick={() => setView("card")}
          >
            Card
          </button>
        </div>
      </div>

      <div className="border-t border-solen-border mb-[var(--space-md)]" />

      {entries.length === 0 ? (
        <div className="text-center py-[var(--space-2xl)] text-solen-muted">
          <p className="font-display text-[1.15rem] mb-[var(--space-sm)] text-solen-fg">No stories yet</p>
          <div className="font-body text-[0.875rem] leading-[1.5]">
            Be the first to share a public check-in.
          </div>
        </div>
      ) : view === "feed" ? (
        <div className="grid gap-4 md:grid-cols-2 animate-[fade-in_0.5s_ease_0.1s_both]">
          {entries.map((entry) => (
            <InspireCard key={entry.id} entry={entry} onToggleLike={handleToggleLike} />
          ))}
        </div>
      ) : (
        <div className="animate-[fade-in_0.5s_ease_0.1s_both]">
          <InspireCardNavigator entries={entries} onToggleLike={handleToggleLike} />
        </div>
      )}
    </main>
  );
}

export default InspirePage;
