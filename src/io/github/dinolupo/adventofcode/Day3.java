package io.github.dinolupo.adventofcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static io.github.dinolupo.adventofcode.Util.*;

/**
 * Created by dino on 14/12/15.

 --- Day 3: Perfectly Spherical Houses in a Vacuum ---

 Santa is delivering presents to an infinite two-dimensional grid of houses.

 He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

 However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

 For example:

 > delivers presents to 2 houses: one at the starting location, and one to the east.
 ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 Your puzzle answer was 2592.

 The first half of this puzzle is complete! It provides one gold star: *

 --- Part Two ---

 The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

 Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

 This year, how many houses receive at least one present?

 For example:

 ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
 ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
 ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.

 */
public class Day3 {

    final static int MAX_SANTA = 2;

    public static void main(String[] args) throws IOException {

        String workingDir = System.getProperty("user.dir");
        Path filePath = FileSystems.getDefault().getPath(workingDir + "/resources/Day3");

        IntStream input = bytesToIntStream(Files.readAllBytes(filePath));

        // every long is a coordinate (x,y) stored in high and low bits
        long[] directions = input.asLongStream().map(c -> {
            long l = 0L;
            switch ((int)c) {
                case '^':
                    l = compose(0,1);
                    break;
                case 'v':
                    l = compose(0,-1);
                    break;
                case '>':
                    l = compose(1,0);
                    break;
                case '<':
                    l = compose(-1,0);
                    break;
            }
            return l;
        }).toArray();

        Map<Long,Integer> houses;

        // s is the current number of Santas
        for(int s=0; s<MAX_SANTA; s++) {

            // store the current Santa that is delivering the present
            int currentSanta;

            // store the coordinates of the Santas
            long[] currentCoordinate = new long[MAX_SANTA];

            houses = new HashMap<Long,Integer>();

            // store initial coordinates of the starting location of each Santas
            int x = 0, y = 0, oldx = 0, oldy = 0;
            for (int cs=0; cs<s; cs++)
                currentCoordinate[cs] = compose(x, y);

            // start to deliver 1 present for each Santas at the current position
            houses.put(currentCoordinate[s], s+1);

            for (int i = 0; i < directions.length; i++) {

                // take turns moving based on instructions
                currentSanta = i % (s+1);

                // get current coordinate of the current Santa
                x = high(currentCoordinate[currentSanta]);
                y = low(currentCoordinate[currentSanta]);

                // move Santa
                x += high(directions[i]);
                y += low(directions[i]);
                currentCoordinate[currentSanta] = compose(x, y);

                // get the number of present at the new coordinate or 0 if it is the first time
                Integer house = houses.getOrDefault(currentCoordinate[currentSanta], 0);

                // put a present in that house
                house++;

                // register the new value
                houses.put(currentCoordinate[currentSanta], house);

                //System.out.printf("S%d at (%d,%d)\n",currentSanta,high(currentCoordinate[currentSanta]),low(currentCoordinate[currentSanta]));

            }

            System.out.printf("With %d Santa(s) the number of houses with at least one present is: %d\n", s+1, houses.size());
        }
    }
}
