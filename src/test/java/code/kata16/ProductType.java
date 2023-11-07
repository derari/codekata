package code.kata16;

import java.util.*;

public enum ProductType {

    PHYSICAL(0),
    FOOD(1),
    KEBAB,
    MEDIA(1),
    BOOK,
    VIDEO,

    MEMBERSHIP(0),
    GYM_MEMBERSHIP(1),
    GYM_BASIC,
    GYM_PREMIUM,
    GYM_ULTRA,
    CINEMA_MEMBERSHIP(1),
    CINEMA_BASIC,
    CINEMA_PREMIUM,
    CINEMA_ULTRA,
    ;

    private final int groupLevel;
    private final List<ProductType> parents = new ArrayList<>();
    private final List<ProductType> upgradable = new ArrayList<>();
    private final List<ProductType> upgradableRO = Collections.unmodifiableList(upgradable);

    ProductType(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    ProductType() {
        this.groupLevel = 99;
    }

    public boolean isProduct() {
        return groupLevel == 99;
    }

    public boolean isGroup() {
        return groupLevel < 99;
    }

    public boolean canUpgrade() {
        return isMembership() && isProduct();
    }

    public boolean is(ProductType type) {
        return this == type || parents.contains(type);
    }

    public boolean isPhysical() {
        return is(PHYSICAL);
    }

    public boolean isMembership() {
        return is(MEMBERSHIP);
    }

    public List<ProductType> getUpgradable() {
        return upgradableRO;
    }

    public static final List<ProductType> VALUES = List.of(ProductType.values());

    static {
        var parents = new ArrayList<ProductType>();
        var upgradable = new ArrayList<ProductType>();
        VALUES.forEach(type -> {
            parents.removeIf(p -> p.groupLevel >= type.groupLevel);
            type.parents.addAll(parents);
            parents.add(type);
            if (type.canUpgrade()) {
                type.upgradable.addAll(upgradable);
                upgradable.add(type);
            } else {
                upgradable.clear();
            }
        });
    }
}
