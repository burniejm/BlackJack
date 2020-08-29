package macyBlackJack.model;

import java.util.stream.Stream;

public enum PlayingCardType{
    H2(2, "H2"),
    H3(3, "H3"),
    H4(4, "H4"),
    H5(5, "H5"),
    H6(6, "H6"),
    H7(7, "H7"),
    H8(8, "H8"),
    H9(9, "H9"),
    H10(10, "H10"),
    HJ(10, "HJ"),
    HQ(10, "HQ"),
    HK(10, "HK"),
    HA(RuleSet.ACE_HIGH_VALUE, "HA"),
    D2(2, "D2"),
    D3(3, "D3"),
    D4(4, "D4"),
    D5(5, "D5"),
    D6(6, "D6"),
    D7(7, "D7"),
    D8(8, "D8"),
    D9(9, "D9"),
    D10(10, "D10"),
    DJ(10, "DJ"),
    DQ(10, "DQ"),
    DK(10, "DK"),
    DA(RuleSet.ACE_HIGH_VALUE, "DA"),
    C2(2, "C2"),
    C3(3, "C3"),
    C4(4, "C4"),
    C5(5, "C5"),
    C6(6, "C6"),
    C7(7, "C7"),
    C8(8, "C8"),
    C9(9, "C9"),
    C10(10, "C10"),
    CJ(10, "CJ"),
    CQ(10, "CQ"),
    CK(10, "CK"),
    CA(RuleSet.ACE_HIGH_VALUE, "CA"),
    S2(2, "S2"),
    S3(3, "S3"),
    S4(4, "S4"),
    S5(5, "S5"),
    S6(6, "S6"),
    S7(7, "S7"),
    S8(8, "S8"),
    S9(9, "S9"),
    S10(10, "S10"),
    SJ(10, "SJ"),
    SQ(10, "SQ"),
    SK(10, "SK"),
    SA(RuleSet.ACE_HIGH_VALUE, "SA");

    private int value;
    private String shortName;

    PlayingCardType(int cardValue, String shortName) {
        this.value = cardValue;
        this.shortName = shortName;
    }

    public static Stream<PlayingCardType> stream() {
        return Stream.of(PlayingCardType.values());
    }

    public boolean isAce() {
        return this.value == RuleSet.ACE_HIGH_VALUE;
    }

    public int getValue() {
        return this.value;
    }

    public String getShortName() {
        return shortName;
    }
}
