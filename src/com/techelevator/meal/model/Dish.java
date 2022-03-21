package com.techelevator.meal.model;

public class Dish {
    private long dishId;
    private String dishName;

    // getter & setter

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    // constructor
    public Dish() {
        this.dishId = 0;
    }

    @Override
    public String toString(){
        return String.format("No.%d - %s",getDishId(), getDishName());
    }

}
