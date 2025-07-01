package com.ravikalla.service;

import com.ravikalla.model.Business;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {

    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);
    private List<Business> businesses = new ArrayList<>();
    private int nextBusinessId = 6;

    @Tool(name = "list_businesses", description = "Get a list of all businesses")
    public List<Business> listBusinesses() {
        return businesses;
    }

    @Tool(name = "search_businesses", description = "Search businesses by name, category, or description")
    public List<Business> searchBusinesses(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        String searchQuery = query.toLowerCase();
        return businesses.stream()
            .filter(business -> 
                business.getName().toLowerCase().contains(searchQuery) ||
                business.getCategory().toLowerCase().contains(searchQuery) ||
                business.getDescription().toLowerCase().contains(searchQuery))
            .toList();
    }

    @Tool(name = "get_business", description = "Get detailed information about a specific business by ID")
    public Business getBusiness(String id) {
        return businesses.stream()
            .filter(business -> business.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Tool(name = "create_business", description = "Create a new business")
    public Business createBusiness(String name, String category, String address, 
                                 String phone, String email, String description, Double rating) {
        // Validate rating
        if (rating != null && (rating < 0.0 || rating > 5.0)) {
            rating = 0.0;
        }
        if (rating == null) {
            rating = 0.0;
        }
        
        // Generate new business ID
        String businessId = String.valueOf(nextBusinessId++);
        
        // Create new business
        Business newBusiness = new Business(businessId, name, category, address, phone, email, description, rating);
        businesses.add(newBusiness);
        
        return newBusiness;
    }

    @Tool(name = "update_business", description = "Update an existing business")
    public Business updateBusiness(String id, String name, String category, String address,
                                 String phone, String email, String description, Double rating) {
        Optional<Business> businessOpt = businesses.stream()
            .filter(business -> business.getId().equals(id))
            .findFirst();
        
        if (businessOpt.isEmpty()) {
            return null;
        }
        
        Business existingBusiness = businessOpt.get();
        
        // Update fields if provided, otherwise keep existing values
        String updatedName = name != null ? name : existingBusiness.getName();
        String updatedCategory = category != null ? category : existingBusiness.getCategory();
        String updatedAddress = address != null ? address : existingBusiness.getAddress();
        String updatedPhone = phone != null ? phone : existingBusiness.getPhone();
        String updatedEmail = email != null ? email : existingBusiness.getEmail();
        String updatedDescription = description != null ? description : existingBusiness.getDescription();
        Double updatedRating = rating;
        
        // Validate rating
        if (updatedRating != null && (updatedRating < 0.0 || updatedRating > 5.0)) {
            updatedRating = existingBusiness.getRating();
        }
        if (updatedRating == null) {
            updatedRating = existingBusiness.getRating();
        }
        
        Business updatedBusiness = new Business(id, updatedName, updatedCategory, updatedAddress, 
                                              updatedPhone, updatedEmail, updatedDescription, updatedRating);
        
        int index = businesses.indexOf(existingBusiness);
        businesses.set(index, updatedBusiness);
        
        return updatedBusiness;
    }

    @Tool(name = "remove_business", description = "Remove a business by ID")
    public Business removeBusiness(String id) {
        Optional<Business> businessToRemove = businesses.stream()
            .filter(business -> business.getId().equals(id))
            .findFirst();
        
        if (businessToRemove.isPresent()) {
            Business business = businessToRemove.get();
            businesses.remove(business);
            return business;
        }
        
        return null;
    }

    @PostConstruct
    public void init() {
        businesses.addAll(List.of(
            new Business("1", "Joe's Coffee Shop", "Restaurant", 
                "123 Main St, Springfield", "555-0101", "joe@joescoffee.com",
                "Local coffee shop serving fresh roasted coffee and pastries", 4.5),
                
            new Business("2", "Tech Repair Solutions", "Technology", 
                "456 Oak Ave, Springfield", "555-0102", "info@techrepair.com",
                "Computer and smartphone repair services", 4.2),
                
            new Business("3", "Green Thumb Landscaping", "Services", 
                "789 Pine Rd, Springfield", "555-0103", "contact@greenthumb.com",
                "Professional landscaping and garden maintenance", 4.8),
                
            new Business("4", "Bella's Boutique", "Retail", 
                "321 Elm St, Springfield", "555-0104", "bella@bellasboutique.com",
                "Fashion boutique specializing in women's clothing and accessories", 4.3),
                
            new Business("5", "Mike's Auto Repair", "Automotive", 
                "654 Maple Dr, Springfield", "555-0105", "mike@mikesauto.com",
                "Full-service automotive repair and maintenance", 4.6)
        ));
    }
}