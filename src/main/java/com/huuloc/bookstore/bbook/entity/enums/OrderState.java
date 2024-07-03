package com.huuloc.bookstore.bbook.entity.enums;

public enum OrderState {
    NEW {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("NEW");
        }

        @Override
        public String getDisplayValue() {
            return "Mới tạo đơn";
        }
    }, PAYMENT {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("PAYMENT");
        }

        @Override
        public String getDisplayValue() {
            return "Chờ thanh toán";
        }
    }, PENDING {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("PENDING");
        }

        @Override
        public String getDisplayValue() {
            return "Đang lấy hàng";
        }
    }, SHIPPING {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("SHIPPING");
        }

        @Override
        public String getDisplayValue() {
            return "Đang giao hàng";
        }
    }, DELIVERED {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("DELIVERED");
        }

        @Override
        public String getDisplayValue() {
            return "Đã giao hàng";
        }
    }, CANCELLED {
        @Override
        public boolean isState(String state) {
            return state.equalsIgnoreCase("CANCELLED");
        }

        @Override
        public String getDisplayValue() {
            return "Đã hủy";
        }
    };

    public abstract boolean isState(String state);

    public abstract String getDisplayValue();
}
