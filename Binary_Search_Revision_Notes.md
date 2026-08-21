# Binary Search — Revision Notes
*Strivers A2Z Sheet — for interview prep. Approach + reasoning + complexity + edge cases for each problem.*
*Part 1: Binary search on 1D sorted arrays. Part 2: Rotated sorted arrays + binary search on the answer.*

---

# Part 1 — Binary Search on 1D Sorted Arrays

## Core Idea (applies to every problem in Part 1)
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

---
---

# Part 2 — Binary Search on Rotated Sorted Arrays & "Binary Search on Answer"

## New Core Idea: Binary Search Doesn't Need a Fully Sorted Array
Everything above relied on the whole array being sorted. The problems below extend the same skeleton to two new situations:
1. **Rotated sorted arrays** — the array isn't globally sorted, but at *any* midpoint, **at least one half is still sorted**. That's enough structure to decide which half to keep.
2. **Binary search on the answer** — the array/input isn't sorted at all, but the *set of possible answers* is monotonic (e.g., "if rate `x` works, every rate `> x` also works") — so you binary search over the **answer space**, not the array, using a `possible(x)` feasibility check at each step.

Recognizing which of these two situations you're in is the real skill — the mechanics (`low`, `high`, `mid`, shrink half) are the same skeleton as Part 1.

---

## 7. Search in Rotated Sorted Array I
*(All elements distinct.)*

### Brute Force (Linear Search)
- **Approach:** Traverse the array once, return the index if `nums[i] == target`, else return -1 at the end.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search — Identify the Sorted Half)
- **Approach:** `low = 0`, `high = n-1`. While `low <= high`: compute `mid`. If `nums[mid] == target`, return `mid`. Otherwise, figure out which half is sorted:
  - If `nums[low] <= nums[mid]`, the **left half is sorted**. Check if `target` lies within `[nums[low], nums[mid]]`. If yes, search left (`high = mid-1`); if no, it must be in the other half, so search right (`low = mid+1`).
  - Otherwise, the **right half is sorted** (`nums[mid] <= nums[high]`). Check if `target` lies within `[nums[mid], nums[high]]`. If yes, search right (`low = mid+1`); if no, search left (`high = mid-1`).
- **Why this works:** In a rotated sorted array, no matter where the rotation point is, splitting at any `mid` always leaves at least one of the two halves fully sorted (the rotation "break" can only be in one half at a time). Once you know a half is sorted, checking if the target's value falls in that half's range is a simple bound check — exactly like normal binary search, just with an extra "which half is sorted" decision layered on top.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - Array not rotated at all (fully sorted) → left half check always succeeds naturally, behaves like standard binary search.
  - Target equal to `nums[low]` or `nums[high]` exactly → included correctly since bound checks use `<=`/`>=`.
  - Single-element array → loop runs once, either matches or returns -1.

---

## 8. Search in Rotated Sorted Array II
*(Array may contain duplicates.)*

### Brute Force (Linear Search)
- **Approach:** Traverse and compare each element to target; return `true` on match, `false` if the loop finishes with no match.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search — Handling Duplicates)
- **Approach:** Same skeleton as Problem 7, with one extra check up front each iteration: if `nums[low] == nums[mid] == nums[high]`, you **cannot tell** which half is sorted (duplicates break the bound check), so just shrink the search space defensively — `low++`, `high--` — and continue. Otherwise, proceed exactly like Problem 7 (check `nums[low] <= nums[mid]` to determine the sorted half, then bound-check target against that half).
- **Why the extra check is necessary:** The "identify the sorted half" trick from Problem 7 depends on strict comparisons being meaningful. If `nums[low] == nums[mid]`, you genuinely don't know if the left half is sorted or if you're just looking at a run of duplicates straddling the rotation point — so the safest move is to shrink from both ends by one and try again, rather than risk discarding the half that actually contains the target.
- **Time Complexity:**
  - **Best/Average case:** O(log N) — behaves like Problem 7 when duplicates aren't adversarial.
  - **Worst case:** O(N/2) → effectively O(N) — happens when most/all elements are identical (e.g., `[3,3,3,3,3,3,3]` searching for a non-existent value), forcing the `low++, high--` fallback on nearly every iteration.
- **Space Complexity:** O(1).
- **Edge cases:**
  - All elements identical, target absent → worst-case linear shrink, must still terminate correctly returning `false`.
  - Duplicates only at the boundaries (`nums[low]`/`nums[high]`) but not at `mid` → normal binary search logic still applies without needing the fallback.
- **Key takeaway:** Always mention this worst-case degradation explicitly in interviews — it's the classic follow-up ("what breaks when there are duplicates?") after solving Problem 7.

---

## 9. Minimum in Rotated Sorted Array

### Brute Force (Linear Search)
- **Approach:** Initialize `mini = INT_MAX`, scan the whole array taking the running minimum.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search — Sorted Half Always Contains Its Own Minimum)
- **Approach:** `low = 0`, `high = n-1`, `ans = INT_MAX`. While `low <= high`: compute `mid`.
  - If `nums[low] <= nums[mid]`, the **left half is sorted** — its own minimum is simply `nums[low]` (the leftmost element of a sorted range). Update `ans = min(ans, nums[low])`, then discard the entire left half (it's already accounted for) — `low = mid+1` — and continue searching the right half for a potentially smaller value.
  - Otherwise, the **right half is sorted** — its minimum is `nums[mid]` (leftmost element of that sorted range). Update `ans = min(ans, nums[mid])`, then discard the right half — `high = mid-1` — and search left.
- **Why you can "account for and discard" a sorted half in one step:** Once you know a half is sorted, you already know its minimum without any further searching (it's just the first element of that range) — so there's no reason to search inside it further. The only place a *smaller* minimum could still be hiding is the unsorted half, which is why you discard the sorted half and keep narrowing the other one.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - Array not rotated → left half is always "sorted" trivially, converges immediately to `nums[0]`.
  - All elements identical → any comparison path still correctly lands on that value.
  - Minimum located exactly at `low` or `high` at some step → still captured since the comparison happens before discarding.

---

## 10. Find How Many Times the Array Has Been Rotated
*(Equivalent to: find the index of the minimum element.)*

### Brute Force (Linear Search)
- **Approach:** Same as Problem 9's brute force, but track the **index** of the running minimum instead of just its value.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Optimal (Binary Search — Same as Problem 9, Track Index Too)
- **Approach:** Identical structure to Problem 9, but alongside `ans` (the minimum value), also track `index` (its position). One added shortcut: if `nums[low] <= nums[high]` at any point, the current search space is *already fully sorted* — so `nums[low]` is immediately the minimum of that space; update `ans`/`index` and break early instead of continuing to subdivide.
- **Why the number of rotations equals the index of the minimum:** A sorted array rotated `k` times moves the original first element (the smallest) to index `k`. So finding *where* the minimum ended up directly tells you *how many* positions the rotation shifted everything by.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:** Same as Problem 9 — zero rotations (already sorted) short-circuits immediately via the "already sorted" check.

---

## 11. Single Element in a Sorted Array
*(Every element appears exactly twice except one — find the single one. Array is sorted.)*

### Brute Force (Compare with Neighbors)
- **Approach:** Handle `n==1` as a trivial edge case (return the only element). For index 0 and the last index, compare only with the single adjacent neighbor that exists. For every other index, check if it differs from *both* neighbors — if so, it's the unpaired element.
- **Time Complexity:** O(N).
- **Space Complexity:** O(1).

### Better (XOR of All Elements)
- **Approach:** XOR every element together. Since `a ^ a = 0` and `a ^ 0 = a`, every properly-paired duplicate cancels out, leaving only the single unpaired element.
- **Time Complexity:** O(N) — still linear, but avoids explicit neighbor-index bookkeeping.
- **Space Complexity:** O(1).
- **Limitation:** Doesn't exploit the fact that the array is *sorted* — this only uses the pairing property, so it can't be improved to O(log N) on its own.

### Optimal (Binary Search — Exploit Sortedness + Pairing Pattern)
- **Approach:** Handle `n==1` and the two boundary indices as edge cases up front (same as brute force). Otherwise: `low = 1`, `high = n-2` (skip the boundaries already handled). While `low <= high`: compute `mid`. If `nums[mid]` differs from *both* neighbors, it's the answer — return it. Otherwise, use the index parity trick to decide direction:
  - If `mid` is odd and `nums[mid] == nums[mid-1]` (or `mid` is even and `nums[mid] == nums[mid+1]`) — this means the pairing pattern is *still intact* to the left of `mid`, so the single element must be to the **right** — discard left: `low = mid+1`.
  - Otherwise, the pairing pattern has already broken by this point, meaning the single element is to the **left** — discard right: `high = mid-1`.
- **Why the odd/even index pattern matters:** Before the single element appears, every pair sits at `(even, odd)` index positions (first copy at an even index, second at the next odd index). Once you pass the single element, that pairing shifts to `(odd, even)`. Checking whether `nums[mid]` matches its "expected" partner based on parity tells you, in O(1), whether you're still before or already after the single element — without needing to scan.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - Single element at index 0 or last index → caught directly by the boundary check before binary search even starts.
  - Only one element in the array → trivial return.

---

## 12. Find Square Root of a Number (Floor)

### Brute Force (Linear Search)
- **Approach:** Try every integer `x` from 1 upward, computing `x*x`. Keep updating `ans = x` as long as `x*x <= n`. Stop as soon as `x*x > n` (further values can only get bigger).
- **Time Complexity:** O(√N) — loop runs until `x` exceeds `√n`, not until `n` itself.
- **Space Complexity:** O(1).

### Optimal (Binary Search on the Answer)
- **Approach:** The search space here isn't the array — it's the **range of possible answers**, `[1, n]`. `low = 1`, `high = n`, `ans = 0`. While `low <= high`: compute `mid`, `val = mid*mid`. If `val <= n`, `mid` is a valid candidate (floor of sqrt could be this or bigger) — record `ans = mid`, search right (`low = mid+1`) for a possibly larger valid value. If `val > n`, `mid` is too big — discard right (`high = mid-1`).
- **Why binary search applies even though there's no array:** The function `x*x` is **monotonically increasing** in `x` — once `mid*mid` exceeds `n`, every larger `x` will too. That monotonic "yes/no" feasibility structure is exactly what binary search needs — you don't need an actual sorted array, just a predicate that flips from true to false (or vice versa) exactly once across the range.
- **Time Complexity:** O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - `n = 0` or `n = 1` → converges immediately to 0 or 1 respectively.
  - Perfect squares → `ans` lands exactly on the true square root.
  - Watch for overflow when computing `mid*mid` for large `n` in fixed-width integer types — worth mentioning explicitly in an interview.

---

## 13. Find the Nth Root of a Number

### Brute Force (Linear Search + Fast Exponentiation)
- **Approach:** Try every integer `x` from 1 to `M`. For each, compute `x^N` using fast exponentiation (exponentiation by squaring: square the base and halve the exponent when even, multiply once and decrement when odd). If `x^N == M`, return `x`. If `x^N > M`, no answer exists beyond this point — break and return -1.
- **Time Complexity:** O(N · log N) — loop runs up to `N` iterations in practice (since it breaks once the power exceeds `M`), and each power computation costs O(log N) via fast exponentiation.
- **Space Complexity:** O(1).

### Optimal (Binary Search on the Answer + Fast Exponentiation Helper)
- **Approach:** Search space is `[1, M]` (candidate values for the root). `low = 1`, `high = M`. While `low <= high`: compute `mid`. Use a helper that computes `mid^N` via exponentiation by squaring, **stopping early** (short-circuiting) the moment the running value exceeds `M` — this avoids overflow and wasted work. The helper returns one of three outcomes: equal to `M` (found the answer, return `mid` immediately), less than `M` (search right — `low = mid+1`), or greater than `M` (search left — `high = mid-1`). If the loop finishes with no exact match, return -1.
- **Why the search space is monotonic here too:** `x^N` strictly increases as `x` increases (for positive `x`, `N`) — so exactly like Problem 12, this is "binary search on the answer" rather than on an array.
- **Time Complexity:** O(log M · log N) — binary search over the answer range takes O(log M) steps, and each step's power computation via fast exponentiation costs O(log N).
- **Space Complexity:** O(1).
- **Edge cases:**
  - No integer Nth root exists (e.g., `M` isn't a perfect Nth power) → loop exhausts without a match, correctly returns -1.
  - `N = 1` → answer is simply `M` itself, still handled correctly by the general logic.
  - Early-stopping inside the power helper is essential — without it, large `mid^N` values could silently overflow before ever being compared to `M`.

---

## 14. Find the Smallest Divisor Given a Threshold

### Brute Force (Linear Search Over Divisors)
- **Approach:** Try every possible divisor `d` from 1 to `max(nums)`. For each, compute the sum of `ceil(nums[i] / d)` across the whole array. Return the first `d` for which this sum is `<= limit`.
- **Time Complexity:** O(max · N) — outer loop over divisor candidates, inner loop over the array for each candidate.
- **Space Complexity:** O(1).

### Optimal (Binary Search on the Answer)
- **Approach:** Search space is `[1, max(nums)]` (candidate divisors). While `low <= high`: compute `mid` (candidate divisor). Use a helper `sumByD(nums, mid)` that computes the sum of `ceil(nums[i]/mid)` over the array. If that sum is `<= limit`, `mid` is a valid (possibly non-minimal) divisor — record it and search left (`high = mid-1`) for a possibly smaller valid divisor. Otherwise, `mid` is too small a divisor (produces too large a sum) — discard left (`low = mid+1`).
- **Why this is monotonic:** As the divisor increases, the resulting sum of ceiling-divisions can only decrease or stay the same — never increase. That monotonic relationship (bigger divisor → smaller-or-equal sum) is exactly the yes/no flip binary search needs.
- **Time Complexity:** O(log(max) · N) — binary search over the divisor range, with an O(N) feasibility check at each step.
- **Space Complexity:** O(1).
- **Edge cases:**
  - `limit` smaller than array length → no valid divisor could ever bring the sum down enough (in general problem constraints this is usually guaranteed not to happen, but worth flagging).
  - All elements equal to 1 → smallest valid divisor converges to 1 immediately.

---

## 15. Koko Eating Bananas (Minimum Eating Speed)

### Brute Force (Linear Search Over Eating Speeds)
- **Approach:** Find `max` (largest pile). Try every eating speed from 1 to `max`. For each speed, compute total hours needed (`calculateTotalHours`, summing `ceil(pile/speed)` for every pile). Return the first speed where total hours `<= h`.
- **Time Complexity:** O(max · N) — nested loop, outer over candidate speeds, inner over piles.
- **Space Complexity:** O(1).

### Optimal (Binary Search on the Answer)
- **Approach:** Search space is `[1, max(nums)]` (candidate eating speeds — no point trying a speed faster than the largest single pile, since that pile alone can be finished in exactly 1 hour at that rate). While `low <= high`: compute `mid` (candidate speed). Use `calculateTotalHours(nums, mid)` to get the hours required at that speed. If hours `<= h`, this speed *works* — but a slower speed might also work and is "better" (the problem wants minimum speed), so record it and search left (`high = mid-1`). Otherwise, this speed is too slow — discard left (`low = mid+1`).
- **Why this is monotonic:** As eating speed increases, total hours required can only decrease or stay the same — never increase. That's the yes/no flip binary search needs: "is speed `x` fast enough?" flips from false to true exactly once as `x` increases.
- **Time Complexity:** O(N · log(max)) — binary search over the speed range, O(N) feasibility check at each step.
- **Space Complexity:** O(1).
- **Edge cases:**
  - `h` exactly equal to the number of piles → forces the maximum possible speed (must finish every pile in exactly 1 hour each).
  - Single pile → converges to a speed that clears it within `h` hours directly.
  - Very large `h` (much greater than needed) → converges to a low speed, potentially 1.

---

## 16. Minimum Number of Days to Make M Bouquets

### Brute Force (Linear Search Over Candidate Days)
- **Edge case handled up front:** If `k * m > n` (not enough flowers to ever form `m` bouquets of `k` roses each), immediately return -1 — no need to search at all.
- **Approach:** Find `mini` and `maxi` — the earliest and latest bloom days across all flowers (defines the meaningful day range to check). Try every day from `mini` to `maxi`. For each day, use a `possible(nums, day, k)` helper: count consecutive flowers that have bloomed by `day` (in their original array order — bouquets must use *adjacent* bloomed flowers), forming complete bouquets of `k` from each consecutive bloomed run, and summing total bouquets formed. Return the first day where the total bouquets `>= m`.
- **Time Complexity:** O((max - min + 1) · N) — outer loop over candidate days, inner O(N) feasibility check per day.
- **Space Complexity:** O(1).

### Optimal (Binary Search on the Answer)
- **Approach:** Same edge case check (`k*m > n` → return -1) up front. Search space is `[mini, maxi]` (candidate days). While `low <= high`: compute `mid` (candidate day). Run `possible(nums, mid, k)`. If feasible (`>= m` bouquets can be formed by this day), this day *works*, but an earlier day might also work — record `ans = mid` and search left (`high = mid-1`) for a possibly earlier feasible day. Otherwise, discard left (`low = mid+1`) and try a later day.
- **Why this is monotonic:** As the day increases, more flowers have had time to bloom, so the number of possible bouquets can only increase or stay the same — never decrease. That gives the clean "is day `x` feasible?" flip from false to true exactly once, which is what makes binary search valid here.
- **Time Complexity:** O(log(max - min + 1) · N) — binary search over the day range, O(N) feasibility check at each step.
- **Space Complexity:** O(1).
- **Edge cases:**
  - `k * m > n` → impossible from the start, must be checked *before* attempting any search (not caught naturally by the binary search itself).
  - `m` bouquets exactly consume all `n` flowers with no leftovers → still handled correctly since `possible()` counts consecutive runs generically.
  - All flowers bloom on the same day → `mini == maxi`, search space collapses to a single day, trivially checked.

---

## Pattern Recap: "Binary Search on the Answer"
Problems 12–16 all share one meta-pattern, distinct from Problems 1–11:
1. You're **not** searching inside a sorted array — you're searching over a **range of candidate answers** (e.g., possible speeds, possible divisors, possible days).
2. You need a **feasibility function** — `possible(x)` — that tells you, for a candidate answer `x`, whether it satisfies the problem's constraint.
3. That feasibility function must be **monotonic** across the range — once it flips from "doesn't work" to "works" (or vice versa), it never flips back. This monotonicity is *the* thing to explicitly verify/state before reaching for binary search — if a problem doesn't have it, binary search on the answer isn't valid.
4. The binary search skeleton is unchanged: narrow `[low, high]` based on the feasibility check, remembering to record `ans` before continuing to search for a possibly better candidate in the "improving" direction (left if minimizing, right if maximizing).

Recognizing "the answer range is monotonic in feasibility" is the single biggest unlock for a whole category of interview problems (allocation problems, capacity problems, minimizing-the-maximum / maximizing-the-minimum problems) — always ask "if X works, does everything past X also work?" before deciding this pattern applies.

---

## How to Use These Notes
1. Before checking the approach, try to state the **search-space invariant** yourself: "what does it mean for a candidate to be valid, and which direction do I search when I find one?" That single question drives nearly every problem above, in both parts.
2. **Part 1 pattern:** lower bound, upper bound, floor, ceil, first occurrence, last occurrence are all the *same* binary search skeleton with one comparison operator and one search direction (left vs right) swapped. Drill the skeleton, not six separate algorithms.
3. **Part 2 pattern:** rotated-array problems hinge on "which half is sorted right now?" — always identify that first, then bound-check against it. Binary-search-on-answer problems (12–16) hinge on "is this candidate feasible, and is feasibility monotonic?" — always state the monotonicity explicitly before applying the technique.
4. Practice explaining out loud *why* you discard a half and *why* you save a candidate before continuing — interviewers often care more about this justification than the final code.
5. Always state time/space complexity trade-offs unprompted (e.g., recursive vs iterative binary search, or the O(N) worst case in rotated search with duplicates) — it signals you understand the mechanism, not just the pattern.
6. When you see a problem where the *input* isn't sorted but you're asked to "minimize the maximum" or "maximize the minimum" or "find the smallest/largest X such that some condition holds" — that's your cue to check for the binary-search-on-answer pattern from Part 2.
