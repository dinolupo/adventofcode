package io.github.dinolupo.adventofcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dino on 03/01/16.
 */

/*

--- Day 7: Some Assembly Required ---

This year, Santa brought little Bobby Tables a set of wiresDescriptions and bitwise logic gates! Unfortunately,
little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).
A signal is provided to each wire by a gate, another wire, or some specific value.
Each wire can only get a signal from one source, but can provide its signal to multiple destinations.
A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together:
x AND y -> z means to connect wiresDescriptions x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.
x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
Other possible gates include OR (bitwise OR) and RSHIFT (right-shift).
If, for some reason, you'd like to emulate the circuit instead, almost all programming languages
(for example, C, JavaScript, or Python) provide operators for these gates.

For example, here is a simple circuit:

123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
After it is run, these are the signals on the wiresDescriptions:

d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456
In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?

Your puzzle answer was 3176.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---

Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a). What new signal is ultimately provided to wire a?

Your puzzle answer was 14710.

*/
public class Day7 {

    static Map<String, String> wiresDescriptions = new HashMap<>();
    static Map<String, Integer> cachedWireValues = new HashMap<>();

    public static void main(String[] args) throws IOException {

        String workingDir = System.getProperty("user.dir");
        Path filePath = FileSystems.getDefault().getPath(workingDir + "/resources/Day7");

        // regex to take wire inputs (group 1) and wire name (group 2)
        String regex = "^(.+) -> (\\p{Lower}+)$";

        // put wiresDescriptions names in a hash map
        Files.lines(filePath).forEach(line -> {

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            boolean found = matcher.find();

            if (found) {
                MatchResult matchResult = matcher.toMatchResult();
                for (int i = 0; i <= matchResult.groupCount(); i++) {
                    String val = matchResult.group(i);
                    if (val != null) {
                        //System.out.printf("%s at %d\n", matchResult.group(i), i);
                        wiresDescriptions.put(matchResult.group(2), matchResult.group(1));
                    }
                }

            }

        });

        String WIRE = "a";
        int aValue = evaluateVariable(WIRE);
        System.out.printf("FIRST PART: %s = %d\n", WIRE, aValue);

        System.out.println("---------------------------------------------------------------------");

        // reset all the wires
        cachedWireValues = new HashMap<>();

        // override wire b to signal from the first iteration
        cachedWireValues.put("b", aValue);

        System.out.printf("SECOND PART: %s = %d\n", WIRE, evaluateVariable(WIRE));

//        for(String key : cachedWireValues.keySet()) {
//            System.out.printf("%s: %d\n",key, cachedWireValues.get(key));
//        }


    }

    // get the variable Value or evaluate it using recursive function
    static int evaluateVariable(String variable) {
        int value = 0;
        try {
            // verify if it is a number
            value = Integer.parseInt(variable);
        } catch (NumberFormatException nfe) {
            // it is a wire name so try to get it from the cache, otherwise call recursive function to evaluate it
            Integer cachedValue = cachedWireValues.get(variable);
            if (cachedValue == null) {
                cachedValue = calculateWireValue(wiresDescriptions.get(variable));
                cachedWireValues.put(variable, cachedValue);
            }
            value = cachedValue;
        }
        return value;
    }

    /*
   Regex reference: https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html

   in the following regular expression we will find values in groups as follows:

   group number |       description         |   example
   -------------|-------------------------------------------------------------------
        1       | signal number             |   123 in (123 -> x)
        2       | operand 1 in binary op.   |   x in (x AND y -> d)
        3       | operation of binary op.   |   AND in (x AND y -> d)
        4       | operand 2 in binary op.   |   y in (x AND y -> d)
        5       | not operand               |   x in (NOT x -> h)
 */
    static int calculateWireValue(String wireInput) {
        final String regex = "^(?:(\\d+|\\p{Lower}+)|(\\d+|\\p{Lower}+) (AND|OR|LSHIFT|RSHIFT) (\\d+|\\p{Lower}+)|NOT (\\p{Lower}+))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(wireInput);
        boolean found = matcher.find();

        if (found) {
            MatchResult matchResult = matcher.toMatchResult();

            String directSignal = matchResult.group(1);

            String operand1 = matchResult.group(2);
            String operation = matchResult.group(3);
            String operand2 = matchResult.group(4);

            String notOperand = matchResult.group(5);

            int value = 0;

            // case 1 - direct signal like (123 -> x) or (ab -> xy)
            if (directSignal != null) {
                value = evaluateVariable(directSignal);
            } // case 2 - not signal like (NOT ab -> x)
            else if (notOperand != null) {
                // execute bitwise NOT operation
                value = ~evaluateVariable(notOperand);
            } else // case 3 - binary operation
            {
                // retrieve from cache operand1 otherwise recursion to calculate it
                int integerOperand1 = evaluateVariable(operand1);
                int integerOperand2 = evaluateVariable(operand2);

                // calculate the value operation
                switch (operation) {
                    case "AND":
                        value = integerOperand1 & integerOperand2;
                        break;
                    case "OR":
                         value = integerOperand1 | integerOperand2;
                        break;
                    case "LSHIFT":
                        value = integerOperand1 << integerOperand2;
                        break;
                    case "RSHIFT":
                        value = integerOperand1 >> integerOperand2;
                        break;
                }
            }

            return value & 0xFFFF;

        }
        return -1;
    }

}
