package com.example.restaurant_taberu.resFoodPanel;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;


public class FoodDetails{


    public String nombre,ingredientes,descripcion,imageURL,categoria,id;
    Boolean bportada;
    public int precio;
    private Bitmap bitmap;


    public FoodDetails(String nombre, String ingredientes, int precio, String descripcion, String categoria,Boolean bportada,String imageURL) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = ingredientes;
        this.categoria = categoria;
        this.bportada = bportada;
        this.imageURL=imageURL;


    }

    public static FoodDetails of (Map<String, Object> data) {

        String nombre,ingredientes,descripcion,imageURL,categoria;
        boolean bportada;
        int precio;

        double puntuacion;
        Timestamp diaCreacion;

        nombre = (String) data.get("Nombre");
        descripcion = (String) data.get("Descripcion");
        ingredientes = (String) data.get("Ingredientes");
        precio = ((Number)data.get("Precio")).intValue();
        categoria = (String) data.get("Categoria");
        bportada = ((Boolean)data.get("EsPortada")).booleanValue();
        imageURL = (String)data.get("ImageURL");

        FoodDetails foodDetails = new FoodDetails(nombre, descripcion, precio,ingredientes,
                categoria, bportada,imageURL);



        return foodDetails;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("Nombre", nombre);
        map.put("Descripcion", descripcion);
        map.put("Ingredientes", ingredientes);
        map.put("Precio", precio);
        map.put("Categoria", categoria);
        map.put("EsPortada", bportada);
        map.put("ImageURL", imageURL);


        return map;
    }



    public String getId() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public int getPrecio() {
        return precio;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCategoria() {
        return categoria;
    }

    public Boolean getEsPortada() {
        return bportada;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public String getNombre() {
        return nombre;
    }


    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEsPortada(Boolean boolportada) {
        this.bportada = bportada;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}