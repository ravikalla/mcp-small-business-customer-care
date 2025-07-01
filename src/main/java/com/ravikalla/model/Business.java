package com.ravikalla.model;

public class Business {
    private String id;
    private String name;
    private String category;
    private String address;
    private String phone;
    private String email;
    private String description;
    private double rating;
    
    public Business() {}
    
    public Business(String id, String name, String category, String address, 
                   String phone, String email, String description, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.rating = rating;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}