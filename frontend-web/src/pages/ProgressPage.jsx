import { useEffect, useState, useCallback } from "react";
import CheckInAPI from "../apis/CheckInAPI";
import UserActivityCalendar from "../components/practiceprogress/UserActivityCalendar";
import PracticeProgressLineChart from "../components/practiceprogress/PracticeProgressLineChart";
import Button from "../components/ui/Button";
import { today, daysAgo, formatShort } from "../utils/dates";
import { groupCheckInsByPractice, buildActivityData } from "../utils/checkins";
import PageHeader from "../components/ui/PageHeader";
import Card from "../components/ui/Card";
import Input from "../components/ui/Input";

const presets = [
  { key: "7d", number: "7", unit: "days", days: 7 },
  { key: "30d", number: "30", unit: "days", days: 30 },
  { key: "90d", number: "90", unit: "days", days: 90 },
  { key: "1y", number: "1", unit: "year", days: 365 },
  { key: "all", number: "All", unit: "time" },
];

const rangeLabels = {
  "7d": "Last 7 days",
  "30d": "Last 30 days",
  "90d": "Last 90 days",
  "1y": "Last year",
  all: "All time",
};

function ProgressPage() {
  const [progressPerPractice, setProgressPerPractice] = useState({});
  const [contribution, setContribution] = useState([]);
  const [startDate, setStartDate] = useState(daysAgo(30));
  const [endDate, setEndDate] = useState(today());
  const [activePreset, setActivePreset] = useState("30d");
  const [customOpen, setCustomOpen] = useState(false);
  const [customStart, setCustomStart] = useState(daysAgo(30));
  const [customEnd, setCustomEnd] = useState(today());
  const [error, setError] = useState("");

  const todayStr = today();
  const mockData = [
    { date: todayStr, streakValue: 3, mood: "AWESOME", practice: { name: "Running" } },
    { date: daysAgo(1), streakValue: 2, mood: "GOOD", practice: { name: "Running" } },
    { date: daysAgo(2), streakValue: 1, mood: "OKAY", practice: { name: "Running" } },
    { date: daysAgo(3), streakValue: 1, mood: "GOOD", practice: { name: "Running" } },
    { date: daysAgo(5), streakValue: 1, mood: "BAD", practice: { name: "Running" } },
    { date: daysAgo(7), streakValue: 1, mood: "GOOD", practice: { name: "Meditation" } },
    { date: daysAgo(8), streakValue: 1, mood: "OKAY", practice: { name: "Meditation" } },
    { date: daysAgo(10), streakValue: 1, mood: "AWESOME", practice: { name: "Meditation" } },
    { date: daysAgo(14), streakValue: 1, mood: "GOOD", practice: { name: "Running" } },
    { date: daysAgo(20), streakValue: 1, mood: "OKAY", practice: { name: "Reading" } },
  ];

  const fetchWithRange = useCallback((start, end) => {
    CheckInAPI.getAll(start || undefined, end || undefined)
      .then((data) => {
        setProgressPerPractice(groupCheckInsByPractice(data));
        setContribution(buildActivityData(data));
      })
      .catch(() => {
        setProgressPerPractice(groupCheckInsByPractice(mockData));
        setContribution(buildActivityData(mockData));
      });
  }, []);

  useEffect(() => { fetchWithRange(startDate, endDate); }, [fetchWithRange, startDate, endDate]);

  const applyPreset = (preset) => {
    setActivePreset(preset.key);
    setCustomOpen(false);
    if (preset.key === "all") {
      setStartDate("");
      setEndDate("");
    } else {
      setStartDate(daysAgo(preset.days));
      setEndDate(today());
    }
  };

  const applyCustomRange = () => {
    if (!customStart || !customEnd) return;
    const start = new Date(customStart + "T00:00:00");
    const end = new Date(customEnd + "T23:59:59");
    if (start >= end) return;
    setActivePreset("custom");
    setStartDate(customStart);
    setEndDate(customEnd);
    setCustomOpen(false);
    fetchWithRange(customStart, customEnd);
  };

  const dayCount =
    activePreset !== "all" && startDate && endDate
      ? Math.round(
          (new Date(endDate + "T23:59:59") - new Date(startDate + "T00:00:00")) /
            (1000 * 60 * 60 * 24)
        )
      : 0;

  const barWidth =
    activePreset === "all" ? 100 : Math.min(100, (dayCount / 365) * 100 + 10);

  const rangeLabel = rangeLabels[activePreset] || "Custom range";

  if (error)
    return (
      <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
        <p className="text-solen-mood-awful">{error}</p>
      </main>
    );

  return (
    <main className="max-w-[1280px] mx-auto px-[var(--gutter)] py-[var(--space-xl)]">
      <div className="flex items-center justify-between mb-[var(--space-lg)] flex-wrap gap-4 animate-[fade-in_0.5s_ease_both]">
        <PageHeader eyebrow="Your growth" title="Progress" className="mb-0" />
        <div className="flex flex-wrap gap-2">
          {presets.map((p) => (
            <button
              key={p.key}
              type="button"
              className={`px-3.5 py-2 border rounded-[8px] cursor-pointer transition-all duration-200 text-center ${
                activePreset === p.key
                  ? "border-solen-accent bg-solen-accent text-[var(--color-solen-surface)]"
                  : "border-solen-border bg-solen-surface text-solen-muted hover:bg-solen-surface-soft hover:text-solen-fg"
              }`}
              onClick={() => applyPreset(p)}
            >
              <span className="block font-display text-[0.85rem] leading-none mb-0.5">
                {p.number}
              </span>
              <span className="block font-mono text-[0.55rem] tracking-[0.1em] uppercase opacity-70 leading-tight">
                {p.unit}
              </span>
            </button>
          ))}
        </div>
      </div>

      <div className="border-t border-solen-border mb-[var(--space-lg)]" />

      {/* Range visualization + custom range */}
      <Card className="mb-[var(--space-lg)] animate-[fade-in_0.5s_ease_0.1s_both]">
        <div className="flex items-start gap-4 mb-[var(--space-lg)]">
          <div className="w-9 h-9 rounded-[8px] border border-solen-border flex items-center justify-center flex-shrink-0 mt-0.5">
            <svg
              className="w-[18px] h-[18px] text-solen-muted"
              viewBox="0 0 18 18"
              fill="none"
              stroke="currentColor"
              strokeWidth="1.3"
            >
              <rect x="1.5" y="3" width="15" height="13.5" rx="2" />
              <path d="M1.5 8h15M5 1.5v3m8-3v3" strokeLinecap="round" />
            </svg>
          </div>
          <div className="flex-1 min-w-0">
            <h2 className="font-display text-[1rem] font-[400] mb-1">
              Select range
            </h2>
            <span className="block font-mono text-[0.7rem] tracking-[0.08em] uppercase text-solen-muted">
              {rangeLabel}
            </span>
          </div>
        </div>

        <div className="relative h-9 mb-[var(--space-md)]">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full h-px bg-solen-border" />
          </div>
          <div className="absolute inset-0 flex items-center">
            <div
              className="h-1.5 rounded-full bg-solen-accent transition-all duration-500"
              style={{ width: barWidth + "%", marginLeft: 0 }}
            />
          </div>
          <div className="absolute -top-0.5 flex items-center justify-between w-full px-0">
            <span className="w-2.5 h-2.5 rounded-full border-2 border-solen-accent bg-solen-surface" />
            <span className="w-2.5 h-2.5 rounded-full border-2 border-solen-accent bg-solen-surface" />
          </div>
        </div>

        <div className="flex items-center justify-between mb-[var(--space-md)]">
          <div className="flex items-center gap-3">
            <span className="font-mono text-[0.75rem] text-solen-muted">
              {activePreset === "all"
                ? "Since first"
                : formatShort(startDate) || "—"}
            </span>
            <span className="font-mono text-[0.6rem] text-solen-muted opacity-50">
              →
            </span>
            <span className="font-mono text-[0.75rem] text-solen-muted">
              {activePreset === "all" ? "today" : formatShort(endDate) || "—"}
            </span>
          </div>
          <span className="font-mono text-[0.6rem] tracking-[0.05em] uppercase text-solen-muted opacity-60">
            {dayCount} day{dayCount !== 1 ? "s" : ""}
          </span>
        </div>

        <div className="border-t border-solen-border pt-[var(--space-md)]">
          <button
            type="button"
            className="flex items-center gap-2 w-full font-body text-[0.85rem] text-solen-muted cursor-pointer transition-colors duration-200 hover:text-solen-fg bg-transparent border-none text-left"
            onClick={() => setCustomOpen(!customOpen)}
          >
            <svg
              className={`w-3.5 h-3.5 transition-transform duration-300 ${
                customOpen ? "rotate-90" : ""
              }`}
              viewBox="0 0 14 14"
            >
              <path
                d="M5 3l4 4-4 4"
                stroke="currentColor"
                strokeWidth="1.5"
                fill="none"
                strokeLinecap="round"
              />
            </svg>
            Custom range
          </button>
          {customOpen && (
            <div className="flex flex-wrap items-end gap-4 pt-[var(--space-md)] animate-[fade-in_0.25s_ease_both]">
              <label className="flex flex-col gap-1.5">
                <span className="font-mono text-[0.65rem] tracking-[0.08em] uppercase text-solen-muted">
                  From
                </span>
                <Input type="date" value={customStart} onChange={(e) => setCustomStart(e.target.value)} />
              </label>
              <label className="flex flex-col gap-1.5">
                <span className="font-mono text-[0.65rem] tracking-[0.08em] uppercase text-solen-muted">
                  To
                </span>
                <Input type="date" value={customEnd} onChange={(e) => setCustomEnd(e.target.value)} />
              </label>
              <Button variant="primary" size="sm" onClick={applyCustomRange}>
                Apply
              </Button>
            </div>
          )}
        </div>
      </Card>

      {/* Activity calendar */}
      <div className="mb-[var(--space-lg)] animate-[fade-in_0.5s_ease_0.2s_both] border-t border-solen-border pt-[var(--space-lg)]">
        <div className="flex items-center justify-between mb-[var(--space-md)]">
          <h2 className="font-display text-[1.2rem] font-[400]">Activity</h2>
          <span className="font-mono text-[0.7rem] tracking-[0.05em] uppercase text-solen-muted">
            This year
          </span>
        </div>
        <Card>
          <UserActivityCalendar data={contribution} />
        </Card>
      </div>

      {/* Per-practice trends */}
      <div className="mb-[var(--space-lg)] animate-[fade-in_0.5s_ease_0.3s_both] border-t border-solen-border pt-[var(--space-lg)]">
        <h2 className="font-display text-[1.2rem] font-[400] mb-[var(--space-md)]">
          Per-practice trends
        </h2>
        <div className="flex flex-col gap-[var(--space-md)]">
          {Object.entries(progressPerPractice).map(([name, practiceProgress]) => {
            const isAll = activePreset === "all";
            const totalDays = dayCount + 1;
            const pct =
              totalDays > 0
                ? Math.round((practiceProgress.length / totalDays) * 100)
                : 0;
              return (
                <Card key={name}>
                <div className="flex items-center justify-between mb-[var(--space-lg)]">
                  <h3 className="font-display text-[1rem] font-[400]">
                    {name}
                  </h3>
                  <span className="font-mono text-[0.8rem] text-solen-muted">
                    {practiceProgress.length} check-in{practiceProgress.length !== 1 ? "s" : ""}{!isAll && pct > 0 ? ` (${pct}%)` : ""}
                  </span>
                </div>
                <div className="space-y-[var(--space-sm)]">
                  <div>
                    <span className="font-mono text-[0.65rem] tracking-[0.08em] uppercase text-solen-muted">
                      Streak
                    </span>
                    <PracticeProgressLineChart data={practiceProgress} metric="streak" />
                  </div>
                  <div>
                    <span className="font-mono text-[0.65rem] tracking-[0.08em] uppercase text-solen-muted">
                      Mood
                    </span>
                    <PracticeProgressLineChart data={practiceProgress} metric="mood" />
                  </div>
                </div>
                </Card>
              );
            })}
          </div>
        </div>
    </main>
  );
}

export default ProgressPage;
