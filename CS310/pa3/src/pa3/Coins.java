package pa3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Coins {
    private int amount;
    private ArrayList<Integer> coins;
    private HashMap<Integer, Integer> coinsUsed; // coin : # used
    private int[][] dp; // dp[i][0]==optimal # of coins to make i, dp[i][1] == largest coin used

    /**
     * 
     * @param s
     *          file path name
     */
    public Coins(String s) {
        File file = new File(s);
        this.coins = new ArrayList<Integer>();
        this.coinsUsed = new HashMap<Integer, Integer>();
        // Read text file for amount and coin denominations
        try {
            Scanner sc = new Scanner(file);
            int line = 1;
            while (sc.hasNextLine()) {
                if (line == 1) {
                    this.amount = Integer.parseInt(sc.nextLine());
                } else {
                    String[] integers = sc.nextLine().split(" ");

                    for (String integer : integers) {
                        this.coins.add(Integer.parseInt(integer));
                    }

                }
                line++;
            }

        } catch (FileNotFoundException x) {
            System.out.println("couldnt read");
        }

        int temp;
        this.dp = new int[this.amount + 1][2];
        for (int i = 0; i <= this.amount; i++) {
            dp[i][0] = Integer.MAX_VALUE;
        }
        dp[0][0] = 0;
        // From 1 -> M, find the optimal number of coins to make i, and store it's
        // largest coin
        for (int i = 1; i <= this.amount; i++) {
            // Iterate through every coin denomination
            for (int j = 0; j < this.coins.size(); j++) {
                if (this.coins.get(j) <= i) { // coin is valid if less than amount needed to create
                    temp = dp[i - this.coins.get(j)][0]; // temp == optimal number of coins to make (i - coin), which
                                                         // was calculated in previous iterations

                    // If a lesser amount of coins is more optimal, use that amount
                    if (temp != Integer.MAX_VALUE && temp + 1 < dp[i][0]) {
                        dp[i][0] = temp + 1;
                        dp[i][1] = this.coins.get(j);
                    }
                }

            }

        }
        int val = this.amount; // M
        // Store the optimal combination to make M in a HashMap
        while (val != 0) {
            int coin = dp[val][1]; // the optimal coin at that amount
            coinsUsed.put(coin, coinsUsed.containsKey(coin) ? coinsUsed.get(coin) + 1 : 1); // increment
            val = val - coin; // find the next amount to get its optimal coin
        }
    }

    /**
     * 
     * @return minimum number of coins
     *         needed to make change for M(amount)
     */
    public int makeChange() {
        return this.dp[this.amount][0];
    }

    /**
     * 
     * @param coin
     * @return number of coins needed for specific coin in the combination
     */
    public int howMany(int coin) {

        return this.coinsUsed.containsKey(coin) ? this.coinsUsed.get(coin) : 0;
    }

    public HashMap<Integer, Integer> getUsed() {
        return this.coinsUsed;
    }

    public static void main(String[] args) {
        String s = args[0];
        Coins c = new Coins(s);
        System.out.println(c.howMany(1));
        System.out.println(c.makeChange());
        System.out.println(c.getUsed());
    }
}
