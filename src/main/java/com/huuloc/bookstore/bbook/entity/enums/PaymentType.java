package com.huuloc.bookstore.bbook.entity.enums;

public enum PaymentType {
    COD {
        @Override
        public String getDisplayValue() {
            return "Thanh toán khi nhận hàng";
        }

        @Override
        public boolean isType(String type) {
            return type.equalsIgnoreCase("COD");
        }
    }, ONLINE {
        @Override
        public String getDisplayValue() {
            return "Chuyển khoản online";
        }

        @Override
        public boolean isType(String type) {
            return type.equalsIgnoreCase("ONLINE");
        }
    };

    public abstract String getDisplayValue();

    public abstract boolean isType(String type);
}
