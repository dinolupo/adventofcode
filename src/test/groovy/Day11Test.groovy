import io.github.dinolupo.adventofcode.Day11

/**
 * Created by dino on 26/03/16.
 */
class Day11Test extends spock.lang.Specification {
    def "Check Straight Of At Least Three Letters"() {
        expect:
        Day11.checkStraightOfAtLeastThreeLetters(input.toCharArray()) == ret

        where:
        input       |   ret
        "hijklmmn"  |   true
        "aaaabcaa"  |   true
        "aaaaafgh"  |   true
        "abdabdab"  |   false
        "ababaxyz"  |   true

    }

    def "Must contain two not overlapping pairs"() {
        expect:
        Day11.checkTwoNotOverlappingPairs(input.toCharArray()) == ret

        where:
        input       |   ret
        "aaqwecbb"  |   true
        "aaartyui"  |   false
        "aafghiaa"  |   false
    }

    def "Must not contain characters i o l"() {
        expect:
        Day11.doesNotContainForbiddenCharachters(input.toCharArray()) == ret

        where:
        input       |   ret
        "abcdefgh"  |   -1
        "abcdefgi"  |   7
        "abcdefgo"  |   7
        "abcdefgl"  |   7
        "iaaaaaaa"  |   0
        "oaaaaaaa"  |   0
        "laaaaaaa"  |   0
        "lbcdefgi"  |   7
        "oooooooi"  |   7

    }


    def "Increment password"() {
        expect:
        Day11.incrementPassword(input.toCharArray()) == newPassword

        where:
        input       |   newPassword
        "aaaaaaaa"  |   "aaaaaaab"
        "aaaaaaaz"  |   "aaaaaaba"
        "azzzzzzz"  |   "baaaaaaa"
        "aaaaaaad"  |   "aaaaaaae"

    }


}
