import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];

        if (n == 0) {
            return tested;
        }

        long[][] dp = new long[n][2];
        int[] parent = new int[n];
        int[] order = new int[n];

        Arrays.fill(parent, -1);

        // Build a traversal order starting from root 0
        int count = 0;
        order[count++] = 0;

        for (int i = 0; i < count; i++) {
            int v = order[i];

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                parent[u] = v;
                order[count++] = u;
            }
        }

        // Calculate DP from leaves to root
        for (int i = count - 1; i >= 0; i--) {
            int v = order[i];

            // State 1: person v is tested
            dp[v][1] = cost[v];

            // State 0: person v is not tested
            dp[v][0] = 0;

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                dp[v][1] += Math.min(dp[u][0], dp[u][1]);
                dp[v][0] += dp[u][1];
            }
        }

        // Reconstruct the chosen people
        tested[0] = dp[0][1] <= dp[0][0];

        for (int i = 1; i < count; i++) {
            int v = order[i];
            int p = parent[v];

            if (!tested[p]) {
                tested[v] = true;
            } else {
                tested[v] = dp[v][1] <= dp[v][0];
            }
        }

        return tested;
    }


    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */

    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) {
            return 0;
        }

        int[] diameter = {0};

        longestDown(0, -1, nb, diameter);

        return diameter[0];
    }

    private static int longestDown(
            int v, int parent, int[][] nb, int[] diameter) {

        int longest = 0;
        int secondLongest = 0;

        for (int u : nb[v]) {
            if (u == parent) {
                continue;
            }

            int path = 1 + longestDown(u, v, nb, diameter);

            if (path > longest) {
                secondLongest = longest;
                longest = path;
            } else if (path > secondLongest) {
                secondLongest = path;
            }
        }

        diameter[0] = Math.max(
                diameter[0],
                longest + secondLongest
        );

        return longest;
    }

}
