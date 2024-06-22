package com.swp.jewelrystore.enums;

public enum PurchaseDiscountRate {
    PURCHASE_DISCOUNT_RATE(1);
    private double value;
    PurchaseDiscountRate(double value) {
        this.value = value;
    }
    public double getValue() {
        return value;
    }
}
