export function groupCheckInsByHabit(checkIns) {
  const perHabits = {};
  checkIns.forEach((ci) => {
    const name = ci.habit?.name || "Unknown";
    if (!perHabits[name]) perHabits[name] = [];
    perHabits[name].push({ date: ci.date, streakValue: ci.streakValue });
  });
  Object.values(perHabits).forEach((hp) =>
    hp.sort((a, b) => new Date(a.date) - new Date(b.date))
  );
  return perHabits;
}

export function buildActivityData(checkIns) {
  const activity = {};
  checkIns.forEach((ci) => {
    activity[ci.date] = (activity[ci.date] || 0) + 1;
  });
  return Object.entries(activity).map(([day, value]) => ({ day, value }));
}
