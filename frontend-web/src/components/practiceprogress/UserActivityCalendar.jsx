import { ResponsiveCalendar } from "@nivo/calendar";

function UserActivityCalendar({ data, startDate, endDate }) {
  return (
    <div className="h-[220px]">
      <ResponsiveCalendar
        data={data}
        from={startDate || undefined}
        to={endDate || undefined}
        emptyColor="oklch(95% 0.012 85)"
        colors={["oklch(88% 0.03 85)", "oklch(80% 0.06 80)", "oklch(72% 0.10 78)", "oklch(64% 0.14 76)", "oklch(68% 0.16 75)"]}
        margin={{ top: 20, right: 20, bottom: 20, left: 20 }}
        yearSpacing={40}
        monthBorderColor="#ffffff"
        dayBorderWidth={2}
        dayBorderColor="#ffffff"
        legends={[
          {
            anchor: "bottom-right",
            direction: "row",
            translateY: 36,
            itemCount: 5,
            itemWidth: 34,
            itemHeight: 14,
            itemsSpacing: 4,
            itemDirection: "right-to-left",
          },
        ]}
      />
    </div>
  );
}

export default UserActivityCalendar;
