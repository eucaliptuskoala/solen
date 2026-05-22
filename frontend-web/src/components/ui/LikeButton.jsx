import { useState } from "react";

function LikeButton({ initialLiked, initialCount, onToggle }) {
  const [liked, setLiked] = useState(initialLiked);
  const [count, setCount] = useState(initialCount);
  const [animating, setAnimating] = useState(false);

  const handleClick = async () => {
    if (animating) return;
    setAnimating(true);

    setLiked((prev) => !prev);
    setCount((prev) => (liked ? prev - 1 : prev + 1));

    try {
      const result = await onToggle();
      setLiked(result.liked);
      setCount(result.likeCount);
    } catch {
      setLiked(initialLiked);
      setCount(initialCount);
    } finally {
      setAnimating(false);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={animating}
      className={`inline-flex items-center gap-[6px] border-none bg-transparent cursor-pointer py-1 px-2 rounded-[6px] transition-all duration-150 hover:bg-solen-accent/5 ${
        animating ? "opacity-60" : ""
      }`}
      aria-label={liked ? "Unlike" : "Like"}
    >
      <svg
        width="16"
        height="16"
        viewBox="0 0 24 24"
        fill={liked ? "currentColor" : "none"}
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
        className={`transition-colors duration-150 ${
          liked ? "text-solen-accent" : "text-solen-muted"
        }`}
      >
        <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
      </svg>
      <span
        className={`font-mono text-[0.75rem] leading-none transition-colors duration-150 ${
          liked ? "text-solen-accent" : "text-solen-muted"
        }`}
      >
        {count}
      </span>
    </button>
  );
}

export default LikeButton;
