package code.codewars.poker.ranking.cards;

public enum GroupCount {

    SINGLE, DOUBLE;

    public boolean match(CardGroups groups) {
        return groups.size() == this.ordinal() + 1;
    }
}
