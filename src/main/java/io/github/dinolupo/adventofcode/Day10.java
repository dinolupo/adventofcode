package io.github.dinolupo.adventofcode;

/**
 * Created by dino on 06/01/16.
 */

/*
--- Day 10: Elves Look, Elves Say ---

Today, the Elves are playing a game called look-and-say.
They take turns making sequences by reading aloud the previous sequence and using that reading as the next sequence.
For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).

Look-and-say sequences are generated iteratively, using the previous value as input for the next step.
For each step, take the previous value, and replace each run of digits (like 111)
with the number of digits (3) followed by the digit itself (1).

For example:

1 becomes 11 (1 copy of digit 1).
11 becomes 21 (2 copies of digit 1).
21 becomes 1211 (one 2 followed by one 1).
1211 becomes 111221 (one 1, one 2, and two 1s).
111221 becomes 312211 (three 1s, two 2s, and one 1).
Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?

Your puzzle input is 1321131112.

*/

public class Day10 {
    static final String input = "1321131112";

    public static void main(String[] args) {

        System.out.printf("Part 1 - 40 iterations length: %d\n",calculateLength(40, input));
        System.out.printf("Part 2 - 50 iterations length: %d\n",calculateLength(50, input));

    }

    static int calculateLength(int n, String input) {
        for (int i=0; i<n; i++) {
            input = iterate(input);
        }
        return input.length();
    }

    // calculate next iteration of Look-and-say game
    static String iterate(String input) {

        StringBuilder sb = new StringBuilder();

        int count = 1;

        for(int i=0; i<input.length(); ) {
            while(i+count < input.length() && input.charAt(i) == input.charAt(i + count)) {
                count++;
            }
            sb.append(count).append(input.charAt(i));
            i += count;
            count = 1;
        }

        return sb.toString();

    }

}
