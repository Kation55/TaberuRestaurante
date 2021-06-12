package com.example.restaurant_taberu;

public class UpdateDishModel {

    String Dishes,RandomUID,Descripcion,Ingredientes,Price,ImageURL,RestaurantId;

    // Press Alt+insert

    public UpdateDishModel(){

    }

    public String getDishes() {
        return Dishes;
    }

    public void setDishes(String dishes) {
        Dishes = dishes;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String description) {
        Descripcion = description;
    }

    public String getIngredientes() {
        return Ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        Ingredientes = ingredientes;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL)
    {
        ImageURL = imageURL;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String chefId) {
        RestaurantId = chefId;
    }
}