package io.github.dinolupo.adventofcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dino on 06/01/16.
 */

/*
--- Day 9: All in a Single Night ---

Every year, Santa manages to deliver all of his presents in a single night.

This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations.
He can start and end at any two (different) locations he wants, but he must visit each location exactly once.
What is the shortest distance he can travel to achieve this?

For example, given the following distances:

London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
The possible routes are therefore:

Dublin -> London -> Belfast = 982
London -> Dublin -> Belfast = 605
London -> Belfast -> Dublin = 659
Dublin -> Belfast -> London = 659
Belfast -> Dublin -> London = 605
Belfast -> London -> Dublin = 982
The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.

What is the distance of the shortest route?

Your puzzle answer was 251.

--- Part Two ---

The next year, just to show off, Santa decides to take the route with the longest distance instead.

He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.

For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.

What is the distance of the longest route?

Your puzzle answer was 898.


 */
public class Day9 {

    static Set<String> cities = new HashSet<>();
    static Map<String, Map<String, Integer>> distances = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String workingDir = System.getProperty("user.dir");
        Path filePath = FileSystems.getDefault().getPath(workingDir + "/resources/Day9");



        final String regex = "(\\p{Alpha}+) to (\\p{Alpha}+) = (\\d+)";
        Pattern pattern = Pattern.compile(regex);

        Files.lines(filePath).forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            boolean found = matcher.find();

            if (found) {
                MatchResult matchResult = matcher.toMatchResult();

                String city1 = matchResult.group(1);
                String city2 = matchResult.group(2);
                int distance = Integer.parseInt(matchResult.group(3));

                if (!distances.containsKey(city1)) {
                    distances.put(city1, new HashMap<>());
                }

                if (!distances.containsKey(city2)) {
                    distances.put(city2, new HashMap<>());
                }

                // the distance between the cities is valid in both directions
                distances.get(city1).put(city2, distance);
                distances.get(city2).put(city1, distance);

                // add the cities to the Set
                cities.add(city1);
                cities.add(city2);

            }


        });

        // test the graph connections

/*        for(String city1 : distances.keySet()) {
            for(String city2 : distances.get(city1).keySet()) {
                System.out.printf("%s -> %s = %d\n",city1, city2, distances.get(city1).get(city2));
            }
        }
*/

        System.out.println();

        // show permutations
/*        List<List<String>> permutations = permutations(cities);
        permutations.stream().forEach(list -> {
            list.stream().forEach(city -> System.out.printf("%s ",city));
            System.out.println();
        });
*/
        Set<Integer> distances = permutations(cities).stream().map(Day9::distance).collect(Collectors.toSet());

        System.out.printf("Part 1 - Shortest distance: %d\n",distances.stream().min(Integer::compare).get());
        System.out.printf("Part 2 - Longest distance: %d\n",distances.stream().max(Integer::compare).get());

    }

    private static int distance(List<String> names) {
        int distance = 0;

        for(int i = 0;i < names.size() - 1;i++) {

            String n1 = names.get(i);
            String n2 = names.get(i + 1);

            if(distances.containsKey(n1) && distances.get(n1).containsKey(n2)) {
                distance += distances.get(n1).get(n2);
            }

        }

        return distance;
    }


    public static List<List<String>> permutations(Collection<String> names) {
        List<List<String>> permutations = new ArrayList<>();

        permutations(new ArrayList<>(), new ArrayList<>(names), permutations);

        return permutations;
    }


    public static void permutations(List<String> head, List<String> tail, List<List<String>> permutations) {
        if(tail.size() == 0) {
            permutations.add(head);
            return;
        }
        for(int i = 0;i < tail.size();i++) {
            List<String> newHead = new ArrayList<>(head.size() + 1);
            List<String> newTail = new ArrayList<>(tail);

            newHead.addAll(head);
            newHead.add(newTail.remove(i));

            permutations(newHead, newTail, permutations);
        }
    }

}
