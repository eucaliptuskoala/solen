const MOOD_MAP = { AWFUL: 0, BAD: 1, OKAY: 2, GOOD: 3, AWESOME: 4 };

function moodToValue(mood) {
  return mood != null ? MOOD_MAP[mood] : undefined;
}

export function groupCheckInsByPractice(checkIns) {
  const perPractices = {};
  checkIns.forEach((ci) => {
    const name = ci.practice?.name || "Unknown";
    if (!perPractices[name]) perPractices[name] = [];
    perPractices[name].push({
      date: ci.date,
      streakValue: ci.streakValue,
      mood: ci.mood,
      moodValue: moodToValue(ci.mood),
    });
  });
  Object.values(perPractices).forEach((hp) =>
    hp.sort((a, b) => new Date(a.date) - new Date(b.date))
  );
  return perPractices;
}

export function buildActivityData(checkIns) {
  const activity = {};
  checkIns.forEach((ci) => {
    activity[ci.date] = (activity[ci.date] || 0) + 1;
  });
  return Object.entries(activity).map(([day, value]) => ({ day, value }));
}
