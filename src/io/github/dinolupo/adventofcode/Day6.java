package io.github.dinolupo.adventofcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.github.dinolupo.adventofcode.Util.*;

/**
 * Created by dino on 18/12/15.

 --- Day 6: Probably a Fire Hazard ---

 Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

 Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

 Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0.
 The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs.
 Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square.
 The lights all start turned off.

 To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

 For example:

 turn on 0,0 through 999,999 would turn on (or leave on) every light.
 toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
 turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 After following the instructions, how many lights are lit?

 Your puzzle answer was 377891.

 The first half of this puzzle is complete! It provides one gold star: *

 --- Part Two ---

 You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

 The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

 The phrase turn on actually means that you should increase the brightness of those lights by 1.

 The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

 The phrase toggle actually means that you should increase the brightness of those lights by 2.

 What is the total brightness of all lights combined after following Santa's instructions?

 For example:

 turn on 0,0 through 0,0 would increase the total brightness by 1.
 toggle 0,0 through 999,999 would increase the total brightness by 2000000.

 */


public class Day6 {

    // Light Class
    public class Light {

        private int status;
        private int brightness;

        public Light() { status=0; brightness=0; }

        void toggle() {
            status ^= 1;
            brightness += 2;
        }

        void turnOn() {
            status = 1;
            brightness += 1;
        }

        void turnOff() {
            status = 0;
            if ( brightness>0 ) {
                brightness -= 1;
            }
        }

        int getStatus() { return status; }
        int getBrightness() { return brightness; }
        void setStatus(int status) { this.status = status; }
        void setBrightness(int brightness) { this.brightness = brightness; }
    }


    int turnedOnLights;
    int totalBrightness;

    int getTurnedOnLights() {
        return turnedOnLights;
    }

    int getTotalBrightness() {
        return totalBrightness;
    }

    public Day6() {
        turnedOnLights = 0;
        totalBrightness = 0;
    }


    void execute () throws IOException {
        String regex = "^(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)$";

        String workingDir = System.getProperty("user.dir");
        Path filePath = FileSystems.getDefault().getPath(workingDir + "/resources/Day6");

        // declare a 1000x1000 grid
        // here I use long to store both status (high bits) and brightness (low bits)
        Light[][] lights = new Light[1000][1000];

        //  The lights all start turned off.
        for (int x=0; x<1000; x++)
        {
            for (int y=0; y<1000; y++)
            {
                lights[x][y] = new Light();
            }
        }

        // for each line of the input file
        Files.lines(filePath).forEachOrdered(line -> {

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            final MatchResult matchResult = matcher.toMatchResult();

            String operation = matchResult.group(1);

            int x1 = Integer.parseInt(matchResult.group(2));
            int y1 = Integer.parseInt(matchResult.group(3));
            int x2 = Integer.parseInt(matchResult.group(4));
            int y2 = Integer.parseInt(matchResult.group(5));

            for( int i1 = x1; i1 <= x2; i1++ )
            {
                for( int i2 = y1; i2 <= y2; i2++ )
                {
                    // The instructions include whether to turn on, turn off,
                    // or toggle various inclusive ranges given as coordinate pairs.
                    switch( operation )
                    {
                        case "turn on":
                        {
                            lights[ i1 ][ i2 ].turnOn();
                            break;
                        }
                        case "turn off":
                        {
                            lights[ i1 ][ i2 ].turnOff();
                            break;
                        }
                        case "toggle":
                        {
                            lights[ i1 ][ i2 ].toggle();
                            break;
                        }
                    }
                }
            }

        });

        Light total = Stream.of(lights).flatMap(x -> Arrays.stream(x)).parallel().reduce(
                (light1, light2) -> { light1.status += light2.status; light1.brightness+=light2.brightness; return light1;} ).get();

        turnedOnLights = total.getStatus();
        totalBrightness = total.getBrightness();

    }

    // main method
    public static void main(String[] args) throws IOException {

        Day6 day6 = new Day6();

        day6.execute();

        System.out.printf("Turned On Lights %d\nTotal Brightness %d\n",day6.getTurnedOnLights(), day6.getTotalBrightness());

    }
}
