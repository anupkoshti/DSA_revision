# Arrays — Revision Notes (Approach-Only)
*Strivers A2Z Sheet — for interview prep. No code, just the thinking process.*

---

## 1. Largest Element in Array
- **Brute:** Sort the array, return the last element. O(n log n).
- **Optimal:** Single pass, keep a running `max` variable, compare each element against it. O(n).
- **Key idea:** Any "find extreme value" problem should make you ask — do I really need to sort, or can one pass do it?

---

## 2. Second Largest Element
- **Brute:** Sort the array, then scan from the end to find the first element different from the largest (handles duplicates). O(n log n).
- **Better:** Two passes — first pass finds `largest`, second pass finds the max element that is `!= largest`. O(2n).
- **Optimal:** Single pass — maintain `largest` and `secondLargest`. When current element beats `largest`, demote old `largest` into `secondLargest` before updating `largest`. Else if it beats `secondLargest` (and isn't equal to `largest`), update `secondLargest`. O(n).
- **Key idea:** Classic "track two running values, update in the right order" pattern — very common (min/max, top-2, etc.)

---

## 3. Maximum Consecutive Ones
- **Optimal (only one approach needed):** Single pass with a counter — increment on `1`, reset to 0 on `0`, track the max seen. O(n).
- **Key idea:** This is the template for "longest streak/run" problems.

---

## 4. Left Rotate Array by One
- **Optimal:** Store the first element in a temp variable, shift every other element one position left, place temp at the last index. O(n).
- **Key idea:** Rotation by 1 is a warm-up for rotation by k — understand it before generalizing.

---

## 5. Left Rotate Array by K Places
- **Brute (extra array):** Copy first `k` elements into a temp array, shift the remaining `n-k` elements left by `k`, then place temp array at the end. O(n) time, O(k) space.
- **Optimal (reversal algorithm):** Reverse first `k` elements, reverse remaining `n-k` elements, then reverse the whole array. O(n) time, O(1) space.
- **Key idea:** The "reverse in parts" trick is a very reusable pattern for rotation problems — think about *why* reversing three times gives rotation (write it out on paper with a small example if you forget).
- **Gotcha:** Always do `k = k % n` first, since k can be larger than n.

---

## 6. Move Zeroes to End
- **Brute:** Push all non-zero elements into a temp array, fill rest with 0, copy back. O(n) time, O(n) space.
- **Optimal (two-pointer):** Maintain pointer `j` for the "next position to place a non-zero". Iterate `i` through array; whenever `nums[i] != 0`, swap `nums[i]` and `nums[j]`, then increment `j`. O(n) time, O(1) space.
- **Key idea:** "Partition in place" pattern — same skeleton reused for removeDuplicates, sortZeroOneTwo, etc.

---

## 7. Remove Duplicates from Sorted Array
- **Optimal (two-pointer):** Since array is sorted, duplicates are adjacent. Keep pointer `j` at last unique element found; scan with `i`; whenever `nums[i] != nums[j]`, it's a new unique value — increment `j` and place it there. Answer is `j+1` (count of uniques).
- **Key idea:** Sorted array + "remove duplicates in place" almost always means two pointers, not extra space.

---

## 8. Missing Number (1 to N)
- **Brute:** For each number `0..N`, linear-search whether it exists in the array. O(n²).
- **Better (hashing):** Create a frequency array of size `n+1`, mark presence of each number, then scan for the index with count 0. O(n) time, O(n) space.
- **Optimal 1 (sum formula):** Expected sum = `n*(n+1)/2`. Subtract actual sum of array from expected sum → missing number. O(n) time, O(1) space.
- **Optimal 2 (XOR trick):** XOR all numbers from `0..n` together, then XOR with all array elements. Duplicates cancel out (`a^a=0`), leaving only the missing number. O(n) time, O(1) space, and avoids overflow issues the sum method can have for large n.
- **Key idea:** When you see "missing/duplicate/single number" in an array of a known range, think **sum vs expected sum** or **XOR properties** before jumping to hashing.

---

## 9. Union of Two Arrays
- **Using a Set:** Insert everything from both arrays into a `TreeSet` (auto sorted + auto de-duplicated). O((n+m) log(n+m)).
- **Optimal (two-pointer, arrays sorted):** Move two pointers across both sorted arrays simultaneously; at each step take the smaller element (or either if equal), but only add it if it's different from the last element added to result (to skip duplicates). O(n+m) time.
- **Key idea:** "Sorted arrays + union/intersection" → two pointers beats hashing/sets on time complexity, since no log factor and no extra hashing overhead.

---

## 10. Intersection of Two Arrays
- **Optimal (two-pointer, arrays sorted):** Move two pointers; whenever elements are equal, add to result and move both pointers; otherwise move the pointer with the smaller value forward. O(n+m).
- **Key idea:** Very similar skeleton to union, but you only add on a *match*, not on every step.

---

## 11. Majority Element (> n/2 times)
- **Brute:** For each element, count its occurrences by scanning the whole array. O(n²).
- **Better (hashmap):** Build a frequency map in one pass, then scan the map for any count `> n/2`. O(n) time, O(n) space.
- **Optimal (Moore's Voting Algorithm):** Maintain a `candidate` and a `count`. If `count == 0`, pick current element as new candidate. If current element equals candidate, increment count, else decrement. At the end, the candidate is the majority element (guaranteed to exist per problem statement). O(n) time, O(1) space.
- **Key idea:** Moore's Voting works because a true majority element (>n/2) can never be fully "cancelled out" by the other elements. Practice explaining *why* it works out loud — interviewers love asking this.
- **Gotcha:** If majority element isn't guaranteed to exist, you need a second pass to verify the candidate's count actually exceeds n/2.

---

## 12. Leaders in an Array
*(An element is a leader if it's strictly greater than all elements to its right.)*
- **Brute:** For every element, scan all elements to its right; if none is `>=` it, it's a leader. O(n²).
- **Optimal:** Traverse from the **right**, keep track of the max seen so far. Any element greater than the current max is a leader (and becomes the new max). Since you're building the list back-to-front, reverse it at the end (or use a stack/insert-at-front logic). O(n).
- **Key idea:** "Something is valid based on everything to its right" → traverse right to left, maintain a running best.

---

## 13. Rearrange Array Elements by Sign (equal positive/negative count)
- **Brute:** Separate positives and negatives into two lists, then merge back by placing positive at even indices and negative at odd indices.
- **Optimal (in one pass, O(1) extra beyond output array):** Use two index pointers, `posIdx` starting at 0 and `negIdx` starting at 1, both stepping by 2. Single pass through input: place positives at `posIdx` (then +=2), negatives at `negIdx` (then +=2). Avoids two separate lists.
- **Key idea:** When output positions follow a fixed alternating pattern, you often don't need auxiliary lists — just two direct index trackers into the result array.

---

## 14. Spiral Traversal of Matrix
- **Optimal (boundary shrinking):** Maintain four boundaries: `top`, `bottom`, `left`, `right`. Traverse top row left→right, right column top→bottom, bottom row right→left (only if `top <= bottom` still valid), left column bottom→top (only if `left <= right` still valid). Shrink the corresponding boundary after each side. Repeat until boundaries cross.
- **Key idea:** This is a "simulation" problem — the trick is entirely in careful boundary management and the two extra validity checks before doing the last row/column (to avoid re-processing a row/column already fully covered when the matrix isn't square).

---

## 15. Pascal's Triangle
Three sub-variants, escalating in complexity:

**(a) Find a specific element at (row, col):**
- **Optimal:** It's simply `nC r` (combinations) where n = row-1, r = col-1. Compute using the iterative nCr formula (multiply/divide progressively to avoid factorial overflow).

**(b) Print an entire row:**
- **Brute:** Call the nCr calculation independently for every column in that row. O(r²) roughly, due to repeated work.
- **Optimal:** Note that each entry can be derived from the previous one: `next = prev * (row - i) / i`. Build the row iteratively using this relation instead of recomputing nCr from scratch each time. O(r).

**(c) Print the entire triangle (n rows):**
- **Optimal:** Just call the "print a row" logic for each row from 1 to n.
- **Key idea:** Recognize the relationship `C(n,r) = C(n,r-1) * (n-r+1)/r` — this incremental-update pattern shows up in a lot of combinatorics problems, not just Pascal's Triangle.

---

## 16. Rotate Matrix by 90 Degrees (in place)
- **Optimal (transpose + reverse):** First take the transpose of the matrix (swap `matrix[i][j]` with `matrix[j][i]` for `i > j`, i.e., only iterate the lower/upper triangle to avoid swapping twice). Then reverse each row individually. Together this gives a 90° clockwise rotation. O(n²) time, O(1) extra space.
- **Key idea:** Get comfortable visualizing why "transpose + row-reverse = rotate" — draw a 3x3 grid by hand once and you won't forget it. (Counter-clockwise rotation instead uses transpose + reverse each *column*, or reverse rows first then transpose.)

---

## 17. Two Sum
- **Brute:** Nested loop — for every pair (i, j), check if they sum to target. O(n²).
- **Better (hashmap):** Single pass — for each element, compute `target - nums[i]` and check if it's already been seen (stored in a map with its index). If yes, you've found the pair; if no, insert the current element into the map. O(n) time, O(n) space. **Preserves original indices**, which matters if the problem wants indices, not values.
- **Optimal (two-pointer, only if you don't need original indices / array can be sorted):** Sort the array, use two pointers from both ends, move inward based on whether current sum is less than or greater than target. O(n log n) due to sort, O(1) extra space — but loses original index info unless you track it separately.
- **Key idea:** This is the foundational pattern for 3Sum, 4Sum, and pair-sum-family problems. Understand *why* hashmap beats brute force here (trade space for time) before moving to the harder variants.

---

## 18. Three Sum
- **Brute:** Three nested loops checking every triplet, use a Set of sorted triplets to avoid duplicates. O(n³).
- **Better:** Fix the first element with one loop, then use a hashset (per outer loop) to find pairs summing to `-nums[i]` in the remaining subarray, similar to two-sum's hashmap idea. O(n²) time, O(n) extra space, still needs a set to dedupe triplets.
- **Optimal (sort + two-pointer):** Sort array first. Fix one element `i`, then use two pointers `j = i+1` and `k = end` to find pairs summing to `-nums[i]`, moving pointers inward based on comparison to target. Skip duplicate values for `i`, `j`, and `k` explicitly to avoid duplicate triplets (no need for a Set). O(n²) time, O(1) extra space (excluding output).
- **Key idea:** Sorting first is what *unlocks* the two-pointer technique and lets you avoid a hashset for deduplication — this trade-off (sort once, then linear scans) is central to the whole Sum-family of problems.

---

## 19. Four Sum
- **Brute:** Four nested loops over all quadruplets, use a Set to dedupe. O(n⁴).
- **Better:** Fix two elements with nested loops, then use a hashset to find the remaining pair summing to the required remainder (like 3Sum's better approach, one level deeper). O(n³) time, O(n) space.
- **Optimal:** Sort the array. Fix two elements `i` and `j` with nested loops (skipping duplicates), then use two pointers `k` and `l` for the remaining two, adjusting based on whether the current sum is less than or greater than target (skip duplicates for k, l too). O(n³) time, O(1) extra space (excluding output).
- **Key idea:** Direct generalization of 3Sum — each "Sum" level just adds one more fixed pointer before dropping into the two-pointer core. Recognizing this pattern means you don't need to memorize 4Sum separately.

---

## 20. Sort an Array of 0s, 1s, and 2s (Dutch National Flag)
- **Brute:** Sort the array using a standard sort. O(n log n) — technically works but ignores the special structure.
- **Better (counting):** Count occurrences of 0, 1, and 2 in one pass, then overwrite the array in a second pass using those counts. O(2n) time, O(1) space — two passes.
- **Optimal (Dutch National Flag algorithm, one pass):** Maintain three pointers: `low`, `mid`, `high`. `mid` scans the array. If `nums[mid] == 0`, swap with `low` and increment both `low` and `mid`. If `nums[mid] == 1`, just move `mid`. If `nums[mid] == 2`, swap with `high` and decrement `high` only (don't move `mid`, since the swapped-in element from `high` hasn't been checked yet). Continue until `mid > high`. O(n) time, single pass, O(1) space.
- **Key idea:** The reason you *don't* advance `mid` after a swap with `high` (but *do* after a swap with `low`) is the classic trip-up — think about *why*: the element coming from the front (low side) is guaranteed already processed/known, but the element coming from the back (high side) hasn't been looked at yet.

---

## 21. Maximum Subarray Sum (Kadane's Algorithm)
- **Brute:** Three nested loops — for every (i, j) pair, compute the sum of that subarray from scratch. O(n³).
- **Better:** Two nested loops — for each starting index `i`, extend `j` forward while maintaining a running sum incrementally instead of recomputing it. O(n²).
- **Optimal (Kadane's Algorithm):** Maintain a running `sum`. At each element, decide: is it better to extend the previous subarray (`sum + nums[i]`), or start fresh from this element (`nums[i]` alone)? Take the max of those two as the new running sum, and track the overall max across the whole traversal. O(n) time, O(1) space.
- **Bonus — tracking the actual subarray (not just the sum):** Reset `sum = 0` and mark a new `start` index whenever `sum` drops below 0 (since a negative running sum can only hurt future subarrays). Whenever a new max is found, record `start` and current index as the answer's boundaries.
- **Key idea:** Kadane's core insight — "a negative running sum is never worth carrying forward, discard it and start over" — is one of the most reused ideas in DP-adjacent array problems. Make sure you can derive it from scratch, not just recall the code.

---

## 22. Next Permutation
- **Brute:** Generate all permutations of the array, sort them lexicographically, find the current permutation in that list, and return the one right after it. Extremely expensive — O(n! · n log n!) roughly — and wasteful since you don't actually need every permutation.
- **Better (still somewhat naive):** Generate the next permutation by trial — repeatedly try to find *some* rearrangement greater than current using next-greater logic without a clean method — in practice this collapses to needing the same insight as the optimal approach, so there isn't a clean "better" tier here; the real unlock is a single mathematical observation.
- **Optimal (single pass + pivot + swap + reverse):**
  1. **Find the pivot (break point):** Scan from the right, find the first index `i` where `nums[i] < nums[i+1]`. This marks the longest non-increasing suffix — everything after this point is already in its "maximum" (descending) arrangement, so no rearrangement of just the suffix can produce anything bigger.
  2. **No pivot found:** If the entire array is non-increasing (pivot stays -1), it means the array is already the *largest* possible permutation — so the "next" permutation wraps around to the *smallest*, i.e., reverse the whole array.
  3. **Find the right swap candidate:** If a pivot exists, scan from the right again to find the *smallest* element that is still greater than `nums[pivot]` (the first one found scanning right-to-left, since the suffix is sorted in descending order). Swap it with `nums[pivot]` — this gives the smallest possible increase at that position.
  4. **Reverse the suffix:** After the swap, the suffix (`pivot+1` to end) is still in descending order (swapping doesn't change that). Reverse it to make it ascending — this yields the *smallest* possible arrangement of that suffix, which combined with step 3 gives the overall next permutation (smallest permutation strictly greater than the current one).
- **Key idea:** This is a pure "insight" problem, not a data-structure problem — there's no meaningful brute-to-optimal spectrum of trade-offs like in Two Sum or Kadane's; you either see the pivot/suffix structure or you don't. Practice explaining *why* each step works (why the suffix is always sorted descending going in, why picking the smallest-greater element at the swap step is correct, why reversing gives the smallest suffix) rather than just memorizing the steps — this is a frequently asked "explain your reasoning" interview question.
- **Gotcha:** Remember this reuses your existing `reverseArray` helper from the rotation problems — recognizing reusable primitives across problems is itself a useful interview habit.

---

## How to Use These Notes
1. Pick a problem, **don't look at the approach** — try to derive brute force first, then think about what's redundant/recomputed, then ask "what data structure or two-pointer trick removes that redundancy?"
2. Cover the approach column and try to reconstruct just the *optimal* idea in one sentence, out loud.
3. Group problems by pattern when revising (two-pointer, hashing, Kadane-style, boundary shrinking) instead of by original order — pattern recall is what actually helps in interviews.
