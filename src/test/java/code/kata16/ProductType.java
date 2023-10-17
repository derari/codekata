package code.kata16;

import java.util.List;

public enum ProductType {

    KEBAB,
    BOOK,
    VIDEO,

    GYM_BASIC,
    GYM_PREMIUM;

    public ProductClass getProductClass() {
        if (isPhysical()) return ProductClass.PHYSICAL;
        if (isMembership()) return ProductClass.MEMBERSHIP;
        return ProductClass.OTHER;
    }

    public boolean isPhysical() {
        return between(KEBAB, VIDEO);
    }

    public boolean isMembership() {
        return between(GYM_BASIC, GYM_PREMIUM);
    }

    public List<ProductType> getUpgradable() {
        if (this == GYM_PREMIUM) return List.of(GYM_BASIC);
        return List.of();
    }

    private boolean between(ProductType low, ProductType high) {
        int n = ordinal();
        return n >= low.ordinal() && n <= high.ordinal();
    }
}
