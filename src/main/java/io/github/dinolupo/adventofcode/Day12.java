package io.github.dinolupo.adventofcode;

/**
 * Created by dino on 27/03/16.
 */


import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.json.*;
import javax.json.stream.JsonParser;


/**
 --- Day 12: JSAbacusFramework.io ---

 Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.

 They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.

 For example:

 [1,2,3] and {"a":2,"b":4} both have a sum of 6.
 [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
 {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
 [] and {} both have a sum of 0.
 You will not encounter any strings containing numbers.

 What is the sum of all numbers in the document?

 Your puzzle answer was 111754.

 --- Part Two ---

 Uh oh - the Accounting-Elves have realized that they double-counted everything red.

 Ignore any object (and all of its children) which has any property with the value "red". Do this only for objects ({...}), not arrays ([...]).

 [1,2,3] still has a sum of 6.
 [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
 {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
 [1,"red",5] has a sum of 6, because "red" in an array has no effect.
 Your puzzle answer was 65402.
 */
public class Day12 {


    public static int navigateTree(JsonValue tree, String skip) {
        int ret = 0;
        switch(tree.getValueType()) {
            case OBJECT:
                //System.out.println("OBJECT");
                JsonArrayBuilder dirtyResult = Json.createArrayBuilder();
                JsonObject object = (JsonObject) tree;
                for (String name : object.keySet()) {
                    if (object.get(name).getValueType() == JsonValue.ValueType.STRING &&
                    object.getString(name).equals(skip)) {
                        return 0;
                    }
                    dirtyResult.add(object.get(name));
                }
                ret = ret + navigateTree(dirtyResult.build(), skip);
                break;
            case ARRAY:
                //System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array)
                    ret = ret + navigateTree(val, skip);
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                //System.out.println("STRING " + st.getString());
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                return num.intValue();
            case TRUE:
            case FALSE:
            case NULL:
                //System.out.println(tree.getValueType().toString());
                break;
        }
        return ret;
    }


    public static void main(String[] args) throws IOException {
        String workingDir = System.getProperty("user.dir");
        Path filePath = FileSystems.getDefault().getPath(workingDir + "/resources/Day12");

        JsonReader reader = Json.createReader(Files.newInputStream(filePath));
        JsonStructure jsonst = reader.read();

        System.out.printf("Value is %d\n", navigateTree(jsonst, null));
        System.out.printf("Value is %d\n", navigateTree(jsonst, "red"));

    }
}
