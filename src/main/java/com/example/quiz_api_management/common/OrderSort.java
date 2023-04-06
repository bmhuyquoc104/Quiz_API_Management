package com.example.quiz_api_management.common;

public enum OrderSort {
    DESC("desc"),
    ASC("asc");
    public final String levelOrder;
    OrderSort (String levelOrder){
        this.levelOrder = levelOrder;
    }

    @Override
    public String toString() {
        return "OrderSort{" +
                "levelOrder='" + levelOrder + '\'' +
                '}';
    }

    public static boolean contains(String checkValid){
        for (OrderSort orderSort: OrderSort.values()){
            if(orderSort.levelOrder.equals(checkValid)) return true;
        }
        return false;
    }
}
