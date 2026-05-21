import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

function HabitProgressBarChart({ data }) {
  const chartData = data.map((p) => ({
    date: p.date,
    value: p.streakValue || 0,
  }));

  return (
    <div className="chart-container" style={{ padding: 0, border: "none" }}>
      <ResponsiveContainer width="100%" height={160}>
        <BarChart data={chartData} margin={{ top: 0, right: 0, bottom: 0, left: 0 }}>
          <XAxis dataKey="date" hide />
          <YAxis hide />
          <Tooltip
            contentStyle={{
              background: "var(--color-solen-surface)",
              border: "1px solid var(--color-solen-border)",
              borderRadius: "var(--radius)",
              fontSize: "0.8rem",
            }}
          />
          <Bar dataKey="value" fill="oklch(72% 0.10 78)" radius={[3, 3, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export default HabitProgressBarChart;
