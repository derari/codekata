package code.codewars.poker.ranking.cards;

public enum GroupSize {

    TWO, THREE, FOUR;

    boolean match(int size) {
        return size == this.ordinal() + 2;
    }
}
