# Assignment 4. Patient Zero

Student ID: 254752
Name: Mariyam
Group: SE-2510

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

```text
Mutations: 100
Shared core: 1117
Marker hits: 5
First hit: 1995
Border length: 3
Shared words: 450
Tests needed: 44
Cheapest testing: 162
Longest chain: 44
```

My outbreak report (a screenshot of `![outbreak](output/outbreak.png)`):



## Questions

### Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

The cell `d[i][j]` represents the minimum number of operations needed to transform the first `i` characters of string A into the first `j` characters of string B. If the current characters are equal, we take the value from the diagonal cell `d[i-1][j-1]`. Otherwise, we take the minimum of insertion, deletion and replacement costs, and add 1.

### Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A subsequence contains characters in the same order, but they do not have to be next to each other. A substring must contain consecutive characters. My Shared core can be bigger than the number of letters at the same positions because LCS can find matching characters at different positions in the two strains.

### Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

The prefix function tells us the length of the longest proper prefix that is also a suffix for every prefix of the pattern. KMP uses these values after a mismatch, so the pointer in the text never needs to move backwards. My Border length is 3, which means that the longest proper prefix of my marker that is also its suffix has three characters.

### Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Different strings can have the same hash because hash collisions are possible. Therefore, I compare the actual characters after finding equal hashes to avoid false matches. If many windows have the same hash, more character comparisons are needed, so the algorithm can become slower.

### Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

The two states are whether a person is tested or not tested. If a person is tested, each child can be tested or not tested, and I choose the cheaper option; if the person is not tested, every child must be tested. A greedy choice based only on the number of links can be wrong because it does not consider the testing costs of the person and the connected people.

## AI use

I used ChatGPT to explain the algorithms, help implement and review the Java methods, and understand errors during testing. 

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.
| test          | size   | time     |
|---------------|--------|----------|
| naive search  | 20 000 | 72.9 ms  |
| naive search  | 40 000 | 136.0 ms |
| naive search  | 80 000 | 280.7 ms |
| KMP           | 20 000 | 3.5 ms   |
| KMP           | 40 000 | 1.3 ms   |
| KMP           | 80 000 | 0.6 ms   |
| edit distance | 500    | 8.1 ms   |
| edit distance | 1000   | 10.5 ms  |
| edit distance | 2000   | 24.3 ms  |
| edit distance | 4000   | 116.5 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow?

The naive search time approximately doubles when the text size doubles, which shows its linear growth in this test. KMP is much faster, although the measured times are very small and affected by JVM optimizations. Edit distance becomes significantly slower as the input size increases because its time complexity is O(n × m).

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm                 | time                                          | space    | assumptions                                                                            |
| ------------------------- | --------------------------------------------- | -------- | -------------------------------------------------------------------------------------- |
| Edit distance             | O(n × m)                                      | O(n × m) | Two-dimensional DP table                                                               |
| LCS                       | O(n × m)                                      | O(n × m) | Two-dimensional DP table                                                               |
| Prefix function and KMP   | O(n + m)                                      | O(n + m) | Prefix function is precomputed; result array stores matches                            |
| Rabin-Karp (shared words) | Expected O(n + m + k × c); worst O(n × m × k) | O(m + n) | c is the number of same-hash candidates examined; character comparisons verify matches |
| Tree DP                   | O(v)                                          | O(v)     | Tree adjacency lists; traversal and DP arrays                                          |

## Before you submit

- [x] `java tools/Check.java` shows all examples right.
- [ ] The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- [x] This README is filled in, the program output and the section AI use included.
- [ ] The work is pushed to main (at least one commit per mission) and the repository is public.
- [ ] The link is attached in Moodle.
