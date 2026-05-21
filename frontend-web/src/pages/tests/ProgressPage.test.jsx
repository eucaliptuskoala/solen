import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, expect, vi, afterEach } from "vitest";
import ProgressPage from "../ProgressPage";
import CheckInAPI from "../../apis/CheckInAPI";

vi.mock("../../apis/CheckInAPI", () => ({
  default: {
    getAll: vi.fn(),
  },
}));

describe("ProgressPage", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it("renders progress after data loads", async () => {
    CheckInAPI.getAll.mockResolvedValue([
      { date: "2026-01-01", streakValue: 1, practice: { name: "Running" } },
      { date: "2026-01-02", streakValue: 1, practice: { name: "Running" } },
    ]);

    render(<ProgressPage />);

    await waitFor(() => {
      expect(screen.getByText("Progress"));
      expect(screen.getByText("Running"));
    });
  });

  it("shows error on API failure", async () => {
    CheckInAPI.getAll.mockRejectedValue(new Error());

    render(<ProgressPage />);

    await waitFor(() => {
      expect(screen.getByText(/failed to load progress/i));
    });
  });
});
