import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const MOOD_LABEL = { 0: "Awful", 1: "Bad", 2: "Okay", 3: "Good", 4: "Awesome" };

function formatDateLabel(dateStr) {
  if (!dateStr) return "";
  const d = new Date(dateStr + "T00:00:00");
  if (isNaN(d.getTime())) return dateStr;
  return d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
}

function CustomTooltip({ active, payload, metric }) {
  if (!active || !payload || payload.length === 0) return null;
  const entry = payload[0].payload;
  return (
    <div
      style={{
        background: "var(--color-solen-surface)",
        border: "1px solid var(--color-solen-border)",
        borderRadius: "var(--radius-solen)",
        fontSize: "0.8rem",
        padding: "6px 10px",
        lineHeight: 1.5,
      }}
    >
      <div style={{ fontWeight: 500, marginBottom: 2 }}>
        {formatDateLabel(entry.date)}
      </div>
      {metric === "streak" && (
        <div>Streak: {entry.streakValue ?? 0}</div>
      )}
      {metric === "mood" && entry.mood && (
        <div>Mood: {MOOD_LABEL[entry.moodValue] ?? entry.mood}</div>
      )}
      {metric === "mood" && !entry.mood && (
        <div style={{ color: "var(--color-solen-muted)" }}>Mood: —</div>
      )}
    </div>
  );
}

function PracticeProgressLineChart({ data, metric }) {
  const isMood = metric === "mood";

  if (!data || data.length === 0) {
    return (
      <div className="flex items-center justify-center h-[120px] text-solen-muted text-[0.75rem]">
        No data
      </div>
    );
  }

  return (
    <div style={{ height: 120 }}>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data} margin={{ top: 4, right: 4, bottom: 4, left: 4 }}>
          <XAxis dataKey="date" hide />
          <YAxis hide domain={isMood ? [0, 4] : [0, "auto"]} />
          <Tooltip
            content={<CustomTooltip metric={metric} />}
            cursor={{ stroke: "var(--color-solen-border)", strokeDasharray: "3 3" }}
          />
          <Line
            type="monotone"
            dataKey={isMood ? "moodValue" : "streakValue"}
            stroke={isMood ? "oklch(72% 0.12 80)" : "oklch(72% 0.10 78)"}
            strokeWidth={2}
            dot={false}
            activeDot={{ r: 4, fill: isMood ? "oklch(78% 0.18 75)" : "oklch(68% 0.16 76)" }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export default PracticeProgressLineChart;
