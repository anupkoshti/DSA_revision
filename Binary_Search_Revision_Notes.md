# Binary Search on 1D Arrays — Revision Notes
*Strivers A2Z Sheet — for interview prep. Approach + reasoning + complexity + edge cases for each problem.*

---

## Core Idea (applies to every problem below)
Binary search only works when the array has some kind of **sorted / monotonic structure** — a property that lets you say "if the answer isn't here, it must be entirely on this side."
The universal skeleton:
1. Maintain a search space with `low` and `high` pointers.
2. Compute `mid` and check a condition against `nums[mid]`.
3. Based on the condition, **discard half the search space** — either move `low = mid+1` or `high = mid-1`.
4. Keep an `ans` variable (when the target might not be an exact match) to remember the best candidate found so far, updated *before* you discard that half.
5. Loop while `low <= high`.

Nearly every variant below (lower bound, upper bound, floor, ceil, first/last occurrence, search insert position) is really just "binary search with a different comparison condition" — once you internalize the skeleton, you're just swapping step 2 and 3.

**Always compute mid safely:** use `mid = low + (high - low) / 2` instead of `(low + high) / 2` to avoid integer overflow when low + high exceeds int range (good habit to state out loud in interviews even if the constraint doesn't demand it).

---

## 1. Search x in a Sorted Array

### Iterative Binary Search
- **Approach:** Initialize `low = 0`, `high = n-1`. While `low <= high`: compute `mid`. If `nums[mid] == target`, return `mid`. If `nums[mid] < target`, the entire left half (including mid) is too small — discard it by setting `low = mid+1`. If `nums[mid] > target`, the entire right half is too large — discard it by setting `high = mid-1`.
- **Why this works:** Because the array is sorted, once you know `nums[mid] < target`, you *know* every element to the left of mid is also `< target` (sorted order guarantees it) — so there's zero point checking them individually. That's the entire justification for throwing away half the array each step.
- **Time Complexity:** O(log N) — each iteration halves the search space, so it takes about log₂(N) iterations to shrink the space to 0 or find the target.
- **Space Complexity:** O(1) — only a few pointer variables, no extra data structures.
- **Edge cases:**
  - Target not present → loop ends when `low > high`, return -1.
  - Empty array → `high = -1` from the start, loop body never executes, returns -1 immediately.
  - Target at the very first or last index → still found correctly since mid eventually converges there.

### Recursive Binary Search
- **Approach:** Same comparisons as iterative, but instead of looping, you recurse into `search(nums, target, mid+1, high)` or `search(nums, target, low, mid-1)`.
- **Base case:** `low > high` → return -1 (search space exhausted, target isn't in the array).
- **Why recursion changes complexity:** The logic is identical to iterative — but each recursive call adds a stack frame that isn't popped until the call below it returns.
- **Time Complexity:** O(log N) — same halving logic as iterative.
- **Space Complexity:** O(log N) — due to the recursion call stack (one frame per halving step), unlike the iterative version's O(1).
- **Key takeaway:** Always mention this space trade-off in interviews — it's a common follow-up question ("can you do this without extra space?" → iterative).

---

## 2. Lower Bound
*Definition: the **first index** where `nums[index] >= x`. (If no such index exists, return `n`, the array length.)*

### Brute Force (Linear Search)
- **Approach:** Traverse left to right, return the index of the first element `>= x`. If you reach the end without finding one, return `n`.
- **Time Complexity:** O(N) — potentially scans the whole array.
- **Space Complexity:** O(1).

### Optimal (Binary Search)
- **Approach:** `low = 0`, `high = n-1`, `ans = n` (default, in case no valid lower bound exists). While `low <= high`: compute `mid`. If `nums[mid] >= x`, this index is a **valid candidate** — record `ans = mid`, but since we want the *smallest* such index, keep searching left: `high = mid-1`. If `nums[mid] < x`, this index (and everything left of it) is too small — discard the left half: `low = mid+1`.
- **Why record before discarding:** You only know `mid` is *a* valid answer, not necessarily *the* answer — there might be an even smaller valid index further left. So you save it as your current-best guess, then keep narrowing to see if you can do better.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - All elements `< x` → `ans` stays at the default `n` (no lower bound exists within the array).
  - All elements `>= x` → answer converges to index 0.
  - Duplicate elements equal to `x` → correctly returns the *first* occurrence, since on a match we keep searching left for an even earlier one.

---

## 3. Upper Bound
*Definition: the **first index** where `nums[index] > x` (strictly greater, not equal). (If no such index exists, return `n`.)*

### Brute Force (Linear Search)
- **Approach:** Traverse left to right, return the index of the first element `> x`. If none found, return `n`.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search)
- **Approach:** Same skeleton as lower bound, but the comparison flips to strict inequality. `low = 0`, `high = n-1`, `ans = n`. While `low <= high`: compute `mid`. If `nums[mid] > x`, it's a valid candidate — record `ans = mid` and search left (`high = mid-1`) for a possibly smaller valid index. If `nums[mid] <= x`, discard the left half — `low = mid+1`.
- **Why this differs from lower bound by just one symbol:** Lower bound asks "first index `>= x`", upper bound asks "first index `> x`" — the *only* change is whether equality counts as a match. This is why it's worth memorizing the pattern rather than the two functions separately.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - All elements `<= x` → upper bound doesn't exist, `ans` stays `n`.
  - Duplicates of `x` in the array → upper bound correctly skips *past* all of them, landing on the first element strictly greater.

---

## 4. Search Insert Position
*Given a sorted array and a target, return the index where it is found, or where it would be inserted to keep the array sorted.*

### Brute Force (Linear Search)
- **Approach:** Walk through the array. If the current element equals target, return its index. If the current element is greater than target, this is where target would need to be inserted — return this index. If you reach the end without either condition triggering, target belongs at the very end — return `n`.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search)
- **Key insight:** "Where should x be inserted to keep the array sorted" is *identical* to "what's the first index with a value `>= x`" — which is exactly the **lower bound** definition. So this problem doesn't need new logic at all; it's a direct reuse.
- **Approach:** Compute the lower bound of `x` using the exact same binary search from Problem 2. Return that value directly.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Key takeaway:** Recognizing when a "new" problem is secretly an old problem in disguise is a core interview skill — always ask "have I already solved something that fits this shape?" before writing new code.

---

## 5. Floor and Ceil in a Sorted Array
*Floor of x = largest element `<= x`. Ceil of x = smallest element `>= x`.*

### Brute Force (Linear Scan)
- **Approach:** Initialize `floor = -1`, `ceil = -1` (sentinel values meaning "not found"). Traverse the whole array once: whenever the current element is `<= x` **and** greater than the current floor, update floor. Whenever the current element is `>= x` **and** less than the current ceil (or ceil hasn't been set), update ceil.
- **Time Complexity:** O(N) — single full pass.
- **Space Complexity:** O(1).
- **Edge cases:**
  - `x` smaller than every element → no floor exists, stays -1.
  - `x` larger than every element → no ceil exists, stays -1.
  - `x` exactly matches an element in the array → that element becomes *both* the floor and the ceil.

### Optimal (Binary Search)
- **Floor:** `low = 0`, `high = n-1`, `ans = -1`. While `low <= high`: compute `mid`. If `nums[mid] <= x`, it's a valid floor candidate — record `ans = mid`, then search *right* (`low = mid+1`) to see if there's a larger valid element still `<= x`. If `nums[mid] > x`, discard the right half — `high = mid-1`.
- **Ceil:** This is precisely the **upper-bound-inclusive** definition — i.e., it's the *lower bound* of x (first element `>= x`). So ceil can be computed by directly reusing the lower bound logic from Problem 2.
- **Why floor searches right but lower bound searches left:** Floor wants the *largest* valid value, so after finding one candidate you keep pushing right to look for something even bigger (but still valid). Lower bound wants the *smallest* valid index, so it keeps pushing left. Same skeleton, opposite direction — tied directly to what "best" means for each problem.
- **Time Complexity:** O(log N) — one binary search for floor, one (reused) for ceil.
- **Space Complexity:** O(1).

---

## 6. First and Last Occurrence of an Element in a Sorted Array

### Brute Force (Linear Scan)
- **Approach:** Initialize `first = -1`, `last = -1`. Traverse the array once. The first time you see the target, set both `first` and `last` to that index. Every subsequent time you see the target, update only `last`.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Better (Using Lower Bound + Upper Bound)
- **Key insight:** The lower bound of the target *is* the first occurrence (first index `>= target`, and since target exists, this lands exactly on it). The upper bound of the target is the index right *after* the last occurrence (first index `> target`), so `upperBound - 1` gives the last occurrence.
- **Approach:** Compute `lb = lowerBound(nums, target)` and `ub = upperBound(nums, target)` using the binary searches from Problems 2 and 3. If `lb == n` or `nums[lb] != target` (meaning the target isn't actually present at all), return `{-1, -1}`. Otherwise return `{lb, ub - 1}`.
- **Time Complexity:** O(log N) + O(log N) = O(log N) (two independent binary searches, constants don't matter asymptotically).
- **Space Complexity:** O(1).
- **Edge case:** Must explicitly verify the target actually exists (`nums[lb] == target`) — lower bound alone doesn't guarantee presence, just position.

### Optimal (Two Dedicated Binary Searches — first & last)
- **firstOccurrence():** `low = 0`, `high = n-1`, `first = -1`. While `low <= high`: compute `mid`. If `nums[mid] == target`, this is a candidate — record `first = mid`, then keep searching **left** (`high = mid-1`) in case there's an even earlier occurrence. If `nums[mid] < target`, discard left (`low = mid+1`). If `nums[mid] > target`, discard right (`high = mid-1`).
- **lastOccurrence():** Same skeleton, but on a match you search **right** instead (`low = mid+1`) to look for a later occurrence, updating `last = mid` each time.
- **Approach:** Run `first = firstOccurrence(...)`. If it returns -1, the target isn't present at all — return `{-1, -1}` immediately (no need to even run lastOccurrence). Otherwise run `last = lastOccurrence(...)` and return `{first, last}`.
- **Why this avoids two full separate binary searches worth of redundant logic:** Both functions are still O(log N) each, so asymptotically identical to the lower/upper-bound approach — but this version is more *direct*, since it searches for the target itself rather than piggybacking on bound definitions, which can be easier to explain cleanly in an interview.
- **Time Complexity:** O(log N) (two binary searches back to back, still logarithmic overall).
- **Space Complexity:** O(1).
- **Edge cases:**
  - Target absent entirely → `firstOccurrence` returns -1, short-circuit to `{-1, -1}` without running `lastOccurrence`.
  - Target appears exactly once → `first == last`, both binary searches converge to the same index.
  - Target occupies the entire array → first converges to index 0, last converges to index n-1.

---

## How to Use These Notes
1. Before checking the approach, try to state the **search-space invariant** yourself: "what does it mean for an index to be a valid candidate, and which direction do I search when I find one?" That single question drives every variant above.
2. Notice the repeating shape: **lower bound**, **upper bound**, **floor**, **ceil**, **first occurrence**, **last occurrence** are all the *same* binary search skeleton with one comparison operator and one search direction (left vs right) swapped. Drill the skeleton, not six separate algorithms.
3. Practice explaining out loud *why* you discard a half and *why* you save a candidate before continuing — interviewers often care more about this justification than the final code.
4. Always state time/space complexity trade-offs unprompted (e.g., recursive vs iterative binary search) — it signals you understand the mechanism, not just the pattern.
