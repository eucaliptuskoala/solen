import { useMemo } from "react";

const CELL_SIZE = 14;
const CELL_GAP = 3;
const DAY_LABELS = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
const MONTH_LABELS = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function getDaysInRange(from, to) {
  const days = [];
  let c = new Date(from);
  while (c <= to) {
    days.push(new Date(c));
    c.setDate(c.getDate() + 1);
  }
  return days;
}

function buildWeeks(days, startDay = 1) {
  const weeks = [];
  let week = [];
  const first = days[0];
  const pad = (first.getDay() - startDay + 7) % 7;
  for (let i = 0; i < pad; i++) week.push(null);
  for (const d of days) {
    week.push(d);
    if (week.length === 7) {
      weeks.push(week);
      week = [];
    }
  }
  if (week.length > 0) {
    while (week.length < 7) week.push(null);
    weeks.push(week);
  }
  return weeks;
}

function getMonths(weeks) {
  const months = [];
  for (let wi = 0; wi < weeks.length; wi++) {
    const week = weeks[wi];
    const dates = week.filter(Boolean);
    if (dates.length === 0) continue;
    const mid = dates[Math.floor(dates.length / 2)];
    const m = mid.getMonth();
    const y = mid.getFullYear();
    if (!months.length || months[months.length - 1].month !== m || months[months.length - 1].year !== y) {
      months.push({ month: m, year: y, weekIndex: wi });
    }
  }
  return months;
}

const COLORS = [
  "oklch(97.5% 0.010 85)",
  "oklch(84% 0.06 82)",
  "oklch(72% 0.11 79)",
  "oklch(60% 0.15 77)",
  "oklch(48% 0.18 74)",
  "oklch(36% 0.20 72)",
];

function pickColor(value, maxValue) {
  if (value <= 0 || maxValue <= 0) return COLORS[0];
  const idx = Math.min(Math.floor((value / maxValue) * (COLORS.length - 1)), COLORS.length - 1);
  return COLORS[idx] || COLORS[0];
}

function UserActivityCalendar({ data }) {
  const { lookup, weeks, months, maxValue, days } = useMemo(() => {
    if (!data || data.length === 0) return { lookup: {}, weeks: [], months: [], maxValue: 0, days: [] };

    const lookup = {};
    let max = 1;
    for (const d of data) {
      lookup[d.day] = d.value;
      if (d.value > max) max = d.value;
    }

    const now = new Date();
    const year = now.getFullYear();
    const rangeStart = new Date(year, 0, 1);
    const rangeEnd = now;
    const dayList = getDaysInRange(rangeStart, rangeEnd);
    const weeks = buildWeeks(dayList);
    const months = getMonths(weeks);

    return { lookup, weeks, months, maxValue: max, days: dayList };
  }, [data]);

  if (!data || data.length === 0 || days.length === 0) {
    return (
      <div className="flex items-center justify-center h-[220px] text-solen-muted text-[0.85rem]">
        No activity in this range
      </div>
    );
  }

  const totalWeeks = weeks.length;
  const svgWidth = 40 + totalWeeks * (CELL_SIZE + CELL_GAP);
  const svgHeight = 40 + 7 * (CELL_SIZE + CELL_GAP);

  return (
    <div style={{ height: 220, width: "100%", overflowX: "auto" }}>
      <svg width={svgWidth} height={svgHeight}>
        {months.map((m, i) => (
          <text
            key={i}
            x={40 + m.weekIndex * (CELL_SIZE + CELL_GAP) + (CELL_SIZE + CELL_GAP) * 2}
            y={14}
            fontSize={11}
            fill="oklch(60% 0.02 85)"
            textAnchor="middle"
          >
            {MONTH_LABELS[m.month]}
          </text>
        ))}
        {DAY_LABELS.map((label, i) =>
          label ? (
            <text
              key={i}
              x={36}
              y={30 + i * (CELL_SIZE + CELL_GAP) + CELL_SIZE / 2 + 3}
              fontSize={10}
              fill="oklch(60% 0.02 85)"
              textAnchor="end"
            >
              {label}
            </text>
          ) : null
        )}
        {weeks.map((week, wi) =>
          week.map((date, di) => {
            if (!date) return null;
            const key = date.toISOString().slice(0, 10);
            const value = lookup[key] || 0;
            return (
              <rect
                key={key}
                x={40 + wi * (CELL_SIZE + CELL_GAP)}
                y={30 + di * (CELL_SIZE + CELL_GAP)}
                width={CELL_SIZE}
                height={CELL_SIZE}
                rx={3}
                ry={3}
                fill={value > 0 ? pickColor(value, maxValue) : "oklch(91% 0.012 85)"}
              />
            );
          })
        )}
        <g transform={`translate(40, ${30 + 7 * (CELL_SIZE + CELL_GAP) + 8})`}>
          {COLORS.map((c, i) => (
            <rect key={i} x={i * 22} y={0} width={14} height={14} rx={2} ry={2} fill={c} />
          ))}
          <text x={COLORS.length * 22 + 4} y={11} fontSize={10} fill="oklch(60% 0.02 85)">
            more
          </text>
        </g>
      </svg>
    </div>
  );
}

export default UserActivityCalendar;
