package io.github.dinolupo.adventofcode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dino on 15/12/15.

 Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

 To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

 For example:

 If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
 If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....

 Your puzzle answer was 346386.

 The first half of this puzzle is complete! It provides one gold star: *

 --- Part Two ---

 Now find one that starts with six zeroes.

 */
public class Day4 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        final String secret = "iwrupvqb";

        // starting test
        int num = 1;

        while(true) {
            StringBuffer input = new StringBuffer(secret);

            byte[] md5 = md.digest(input.append(num).toString().getBytes());

            if (    (md5[0] & 0xff) == 0x0 &&
                    (md5[1] & 0xff) == 0x0 &&
                    (md5[2] & 0xf0) == 0x0 ) {
                break;
            }
            ++num;
            //System.out.printf("hashing \"%s\": %s - %s - %s\n",input.toString(), Integer.toHexString(md5[0] & 0xff), Integer.toHexString(md5[1] & 0xff ), Integer.toHexString(md5[2] & 0xff));
        }

        System.out.printf("First number with five zeroes: %d\n", num);

        // continue from the previous number to find six zeroes
        while(true) {
            StringBuffer input = new StringBuffer(secret);

            byte[] md5 = md.digest(input.append(num).toString().getBytes());

            if (    (md5[0] & 0xff) == 0x0 &&
                    (md5[1] & 0xff) == 0x0 &&
                    (md5[2] & 0xff) == 0x0 ) {
                break;
            }
            ++num;
        }

        System.out.printf("First number with six zeroes: %d\n", num);


    }
}
