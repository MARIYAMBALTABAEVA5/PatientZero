import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */

    public static int[] prefixFunction(String p) {
        int n = p.length();
        int[] pi = new int[n];

        int j = 0;

        for (int i = 1; i < n; i++) {

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }


    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */

    public static int[] findAll(String text, String p) {
        if (p.length() == 0) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        int[] result = new int[text.length()];

        int count = 0;
        int j = 0;

        for (int i = 0; i < text.length(); i++) {

            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                result[count] = i - p.length() + 1;
                count++;

                j = pi[j - 1];
            }
        }

        return java.util.Arrays.copyOf(result, count);
    }


    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */

    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) {
            return 0;
        }

        long base = 5;
        long power = 1;

        for (int i = 1; i < k; i++) {
            power *= base;
        }

        Map<Long, List<Integer>> hashMap = new HashMap<>();

        long hash = 0;

        // Calculate hash of the first window in b
        for (int i = 0; i < k; i++) {
            hash = hash * base + (b.charAt(i) - 'A' + 1);
        }

        hashMap.computeIfAbsent(hash, x -> new ArrayList<>()).add(0);

        // Calculate hashes of the remaining windows in b
        for (int i = k; i < b.length(); i++) {
            hash = (hash - (b.charAt(i - k) - 'A' + 1) * power) * base
                    + (b.charAt(i) - 'A' + 1);

            int position = i - k + 1;

            hashMap.computeIfAbsent(hash, x -> new ArrayList<>()).add(position);
        }

        int count = 0;

        // Check every window in a
        hash = 0;

        for (int i = 0; i < k; i++) {
            hash = hash * base + (a.charAt(i) - 'A' + 1);
        }

        if (containsEqualWindow(a, b, 0, hashMap.get(hash), k)) {
            count++;
        }

        for (int i = k; i < a.length(); i++) {
            hash = (hash - (a.charAt(i - k) - 'A' + 1) * power) * base
                    + (a.charAt(i) - 'A' + 1);

            int position = i - k + 1;

            if (containsEqualWindow(a, b, position, hashMap.get(hash), k)) {
                count++;
            }
        }

        return count;
    }

    private static boolean containsEqualWindow(
            String a, String b, int aPosition,
            List<Integer> positions, int k) {

        if (positions == null) {
            return false;
        }

        for (int bPosition : positions) {
            int j = 0;

            while (j < k &&
                    a.charAt(aPosition + j) == b.charAt(bPosition + j)) {
                j++;
            }

            if (j == k) {
                return true;
            }
        }

        return false;
    }

}
