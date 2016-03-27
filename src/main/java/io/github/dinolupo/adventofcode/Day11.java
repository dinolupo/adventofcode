package io.github.dinolupo.adventofcode;

/**
 * Created by dino on 26/03/16.
 */

/**
 --- Day 11: Corporate Policy ---

 Santa's previous password expired, and he needs help choosing a new one.

 To help him remember his new password after the old one expires, Santa has devised a method of coming up with a
 password based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters
 (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.

 Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter
 one step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.

 Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:

 Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz.
 They cannot skip letters; abd doesn't count.
 Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are
 therefore confusing.
 Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
 For example:

 hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement
 requirement (because it contains i and l).
 abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 abbcegjk fails the third requirement, because it only has one double letter (bb).
 The next password after abcdefgh is abcdffaa.
 The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with
 ghi..., since i is not allowed.
 Given Santa's current password (your puzzle input), what should his next password be?

 Your puzzle answer was cqjxxyzz.

 --- Part Two ---

 Santa's password expired again. What's the next one?

 Your puzzle answer was cqkaabcc.

 Both parts of this puzzle are complete! They provide two gold stars: **

 At this point, you should return to your advent calendar and try another puzzle.

 Your puzzle input was cqjxjnds.
 */
public class Day11 {

    static final String input1 = "cqjxjnds";

    public static boolean checkStraightOfAtLeastThreeLetters(char[] input) {
        int count = 1;
        for (int i=1; i<input.length; i++) {
            if (input[i] == input[i-1] + 1) {
                count++;
            } else {
                count = 1;
            }
            if (count == 3) return true;
        }
        return false;
    }

    public static int doesNotContainForbiddenCharachters(char[] input) {
        for (int i=input.length-1; i>=0; i--) {
            if (input[i] == 'i' || input[i] == 'o' || input[i] == 'l') return i;
        }
        return -1;


    }

    public static boolean checkTwoNotOverlappingPairs(char[] input) {
        boolean pairOne = false;
        boolean pairTwo = false;
        char pairOneChar = 0;
        for (int i=1; i<input.length-1; i++) {
            if (input[i-1] == input[i]) {
                pairOne = true;
                pairOneChar = input[i];
                continue;
            }
            if(pairOne == true && input[i] != pairOneChar && input[i] == input[i+1]) {
                pairTwo = true;
                break;
            }
        }
        return pairOne && pairTwo;
    }


    public static String incrementPassword(char[] input) {
        int index = input.length-1;
        while (true) {
            input[index] = (char)(input[index] + 1);
            if (input[index] > 'z') {
                input[index] = 'a';
                index--;
            } else {
                return new String(input);
            }
        }
    }

    public  static String nextPassword(String current) {
        do {
            current = incrementPassword(current.toCharArray());
            int index = doesNotContainForbiddenCharachters(current.toCharArray());
            if (index >= 0
                    || !checkTwoNotOverlappingPairs(current.toCharArray())
                    || !checkStraightOfAtLeastThreeLetters(current.toCharArray())
                    ) continue;
            else break;
        }
        while(true);
        return current;
    }

    public static void main(String[] args) {

        String firstPassword = nextPassword(input1);
        //String secondPassword = "NOPE";
        String secondPassword = nextPassword(firstPassword);
        System.out.printf("1st: %s\n2nd: %s\n",firstPassword, secondPassword);

    }


}
