package com.ravikalla.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ravikalla.model.Business;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class McpServer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Business> businesses;
    private int nextBusinessId = 6;
    
    public McpServer() {
        this.businesses = initializeBusinesses();
    }
    
    private List<Business> initializeBusinesses() {
        List<Business> businessList = new ArrayList<>();
        
        businessList.add(new Business("1", "Joe's Coffee Shop", "Restaurant", 
            "123 Main St, Springfield", "555-0101", "joe@joescoffee.com",
            "Local coffee shop serving fresh roasted coffee and pastries", 4.5));
            
        businessList.add(new Business("2", "Tech Repair Solutions", "Technology", 
            "456 Oak Ave, Springfield", "555-0102", "info@techrepair.com",
            "Computer and smartphone repair services", 4.2));
            
        businessList.add(new Business("3", "Green Thumb Landscaping", "Services", 
            "789 Pine Rd, Springfield", "555-0103", "contact@greenthumb.com",
            "Professional landscaping and garden maintenance", 4.8));
            
        businessList.add(new Business("4", "Bella's Boutique", "Retail", 
            "321 Elm St, Springfield", "555-0104", "bella@bellasboutique.com",
            "Fashion boutique specializing in women's clothing and accessories", 4.3));
            
        businessList.add(new Business("5", "Mike's Auto Repair", "Automotive", 
            "654 Maple Dr, Springfield", "555-0105", "mike@mikesauto.com",
            "Full-service automotive repair and maintenance", 4.6));
        
        return businessList;
    }
    
    public void start() {
        try {
            // Create a persistent loop that doesn't depend on stdin staying open
            PrintWriter writer = new PrintWriter(System.out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            // Keep the server alive by running indefinitely
            while (true) {
                try {
                    // Check if input is available without blocking indefinitely
                    if (reader.ready()) {
                        String line = reader.readLine();
                        
                        if (line != null && !line.trim().isEmpty()) {
                            JsonNode request = objectMapper.readTree(line);
                            JsonNode response = handleRequest(request);
                            writer.println(objectMapper.writeValueAsString(response));
                            writer.flush();
                        }
                    } else {
                        // No input ready, sleep briefly and continue
                        Thread.sleep(50);
                    }
                    
                } catch (IOException e) {
                    // Input stream issue, but keep server alive
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    ObjectNode errorResponse = objectMapper.createObjectNode();
                    errorResponse.put("jsonrpc", "2.0");
                    ObjectNode error = objectMapper.createObjectNode();
                    error.put("code", -32700);
                    error.put("message", "Parse error: " + e.getMessage());
                    errorResponse.set("error", error);
                    writer.println(objectMapper.writeValueAsString(errorResponse));
                    writer.flush();
                }
            }
            
        } catch (Exception e) {
            // Silent error handling for clean MCP communication
        }
    }
    
    private JsonNode handleRequest(JsonNode request) {
        String method = request.has("method") ? request.get("method").asText() : "";
        JsonNode id = request.has("id") ? request.get("id") : null;
        
        ObjectNode response = objectMapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) {
            response.set("id", id);
        }
        
        try {
            JsonNode result;
            switch (method) {
                case "initialize":
                    result = handleInitialize();
                    break;
                case "tools/list":
                    result = handleToolsList();
                    break;
                case "tools/call":
                    result = handleToolsCall(request);
                    break;
                default:
                    ObjectNode error = objectMapper.createObjectNode();
                    error.put("code", -32601);
                    error.put("message", "Method not found: " + method);
                    response.set("error", error);
                    return response;
            }
            response.set("result", result);
        } catch (Exception e) {
            ObjectNode error = objectMapper.createObjectNode();
            error.put("code", -32603);
            error.put("message", "Internal error: " + e.getMessage());
            response.set("error", error);
        }
        
        return response;
    }
    
    private JsonNode handleInitialize() {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("protocolVersion", "2024-11-05");
        
        ObjectNode capabilities = objectMapper.createObjectNode();
        capabilities.put("tools", true);
        response.set("capabilities", capabilities);
        
        ObjectNode serverInfo = objectMapper.createObjectNode();
        serverInfo.put("name", "small-business-customer-care");
        serverInfo.put("version", "1.0.0");
        response.set("serverInfo", serverInfo);
        
        return response;
    }
    
    private JsonNode handleToolsList() {
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode tools = objectMapper.createArrayNode();
        
        ObjectNode listBusinessesTool = objectMapper.createObjectNode();
        listBusinessesTool.put("name", "list_businesses");
        listBusinessesTool.put("description", "Get a list of all businesses");
        
        ObjectNode searchBusinessesTool = objectMapper.createObjectNode();
        searchBusinessesTool.put("name", "search_businesses");
        searchBusinessesTool.put("description", "Search businesses by name, category, or description");
        ObjectNode searchInputSchema = objectMapper.createObjectNode();
        searchInputSchema.put("type", "object");
        ObjectNode searchProperties = objectMapper.createObjectNode();
        ObjectNode queryProp = objectMapper.createObjectNode();
        queryProp.put("type", "string");
        queryProp.put("description", "Search query");
        searchProperties.set("query", queryProp);
        searchInputSchema.set("properties", searchProperties);
        ArrayNode searchRequired = objectMapper.createArrayNode();
        searchRequired.add("query");
        searchInputSchema.set("required", searchRequired);
        searchBusinessesTool.set("inputSchema", searchInputSchema);
        
        ObjectNode getBusinessTool = objectMapper.createObjectNode();
        getBusinessTool.put("name", "get_business");
        getBusinessTool.put("description", "Get detailed information about a specific business");
        ObjectNode getInputSchema = objectMapper.createObjectNode();
        getInputSchema.put("type", "object");
        ObjectNode getProperties = objectMapper.createObjectNode();
        ObjectNode idProp = objectMapper.createObjectNode();
        idProp.put("type", "string");
        idProp.put("description", "Business ID");
        getProperties.set("id", idProp);
        getInputSchema.set("properties", getProperties);
        ArrayNode getRequired = objectMapper.createArrayNode();
        getRequired.add("id");
        getInputSchema.set("required", getRequired);
        getBusinessTool.set("inputSchema", getInputSchema);
        
        ObjectNode createBusinessTool = objectMapper.createObjectNode();
        createBusinessTool.put("name", "create_business");
        createBusinessTool.put("description", "Create a new business");
        ObjectNode createInputSchema = objectMapper.createObjectNode();
        createInputSchema.put("type", "object");
        ObjectNode createProperties = objectMapper.createObjectNode();
        
        ObjectNode nameProp = objectMapper.createObjectNode();
        nameProp.put("type", "string");
        nameProp.put("description", "Business name");
        createProperties.set("name", nameProp);
        
        ObjectNode categoryProp = objectMapper.createObjectNode();
        categoryProp.put("type", "string");
        categoryProp.put("description", "Business category");
        createProperties.set("category", categoryProp);
        
        ObjectNode addressProp = objectMapper.createObjectNode();
        addressProp.put("type", "string");
        addressProp.put("description", "Business address");
        createProperties.set("address", addressProp);
        
        ObjectNode phoneProp = objectMapper.createObjectNode();
        phoneProp.put("type", "string");
        phoneProp.put("description", "Business phone number");
        createProperties.set("phone", phoneProp);
        
        ObjectNode emailProp = objectMapper.createObjectNode();
        emailProp.put("type", "string");
        emailProp.put("description", "Business email address");
        createProperties.set("email", emailProp);
        
        ObjectNode descriptionProp = objectMapper.createObjectNode();
        descriptionProp.put("type", "string");
        descriptionProp.put("description", "Business description");
        createProperties.set("description", descriptionProp);
        
        ObjectNode ratingProp = objectMapper.createObjectNode();
        ratingProp.put("type", "number");
        ratingProp.put("description", "Business rating (0.0 to 5.0)");
        ratingProp.put("minimum", 0.0);
        ratingProp.put("maximum", 5.0);
        createProperties.set("rating", ratingProp);
        
        createInputSchema.set("properties", createProperties);
        ArrayNode createRequired = objectMapper.createArrayNode();
        createRequired.add("name");
        createRequired.add("category");
        createRequired.add("address");
        createRequired.add("phone");
        createRequired.add("email");
        createRequired.add("description");
        createInputSchema.set("required", createRequired);
        createBusinessTool.set("inputSchema", createInputSchema);
        
        ObjectNode removeBusinessTool = objectMapper.createObjectNode();
        removeBusinessTool.put("name", "remove_business");
        removeBusinessTool.put("description", "Remove a business by ID");
        ObjectNode removeInputSchema = objectMapper.createObjectNode();
        removeInputSchema.put("type", "object");
        ObjectNode removeProperties = objectMapper.createObjectNode();
        ObjectNode removeIdProp = objectMapper.createObjectNode();
        removeIdProp.put("type", "string");
        removeIdProp.put("description", "Business ID to remove");
        removeProperties.set("id", removeIdProp);
        removeInputSchema.set("properties", removeProperties);
        ArrayNode removeRequired = objectMapper.createArrayNode();
        removeRequired.add("id");
        removeInputSchema.set("required", removeRequired);
        removeBusinessTool.set("inputSchema", removeInputSchema);
        
        ObjectNode updateBusinessTool = objectMapper.createObjectNode();
        updateBusinessTool.put("name", "update_business");
        updateBusinessTool.put("description", "Update an existing business");
        ObjectNode updateInputSchema = objectMapper.createObjectNode();
        updateInputSchema.put("type", "object");
        ObjectNode updateProperties = objectMapper.createObjectNode();
        
        ObjectNode updateIdProp = objectMapper.createObjectNode();
        updateIdProp.put("type", "string");
        updateIdProp.put("description", "Business ID to update");
        updateProperties.set("id", updateIdProp);
        
        ObjectNode updateNameProp = objectMapper.createObjectNode();
        updateNameProp.put("type", "string");
        updateNameProp.put("description", "Business name (optional)");
        updateProperties.set("name", updateNameProp);
        
        ObjectNode updateCategoryProp = objectMapper.createObjectNode();
        updateCategoryProp.put("type", "string");
        updateCategoryProp.put("description", "Business category (optional)");
        updateProperties.set("category", updateCategoryProp);
        
        ObjectNode updateAddressProp = objectMapper.createObjectNode();
        updateAddressProp.put("type", "string");
        updateAddressProp.put("description", "Business address (optional)");
        updateProperties.set("address", updateAddressProp);
        
        ObjectNode updatePhoneProp = objectMapper.createObjectNode();
        updatePhoneProp.put("type", "string");
        updatePhoneProp.put("description", "Business phone number (optional)");
        updateProperties.set("phone", updatePhoneProp);
        
        ObjectNode updateEmailProp = objectMapper.createObjectNode();
        updateEmailProp.put("type", "string");
        updateEmailProp.put("description", "Business email address (optional)");
        updateProperties.set("email", updateEmailProp);
        
        ObjectNode updateDescriptionProp = objectMapper.createObjectNode();
        updateDescriptionProp.put("type", "string");
        updateDescriptionProp.put("description", "Business description (optional)");
        updateProperties.set("description", updateDescriptionProp);
        
        ObjectNode updateRatingProp = objectMapper.createObjectNode();
        updateRatingProp.put("type", "number");
        updateRatingProp.put("description", "Business rating (0.0 to 5.0) (optional)");
        updateRatingProp.put("minimum", 0.0);
        updateRatingProp.put("maximum", 5.0);
        updateProperties.set("rating", updateRatingProp);
        
        updateInputSchema.set("properties", updateProperties);
        ArrayNode updateRequired = objectMapper.createArrayNode();
        updateRequired.add("id");
        updateInputSchema.set("required", updateRequired);
        updateBusinessTool.set("inputSchema", updateInputSchema);
        
        tools.add(listBusinessesTool);
        tools.add(searchBusinessesTool);
        tools.add(getBusinessTool);
        tools.add(createBusinessTool);
        tools.add(removeBusinessTool);
        tools.add(updateBusinessTool);
        
        response.set("tools", tools);
        return response;
    }
    
    private JsonNode handleToolsCall(JsonNode request) {
        JsonNode params = request.get("params");
        String toolName = params.get("name").asText();
        JsonNode arguments = params.has("arguments") ? params.get("arguments") : objectMapper.createObjectNode();
        
        switch (toolName) {
            case "list_businesses":
                return handleListBusinesses();
            case "search_businesses":
                return handleSearchBusinesses(arguments);
            case "get_business":
                return handleGetBusiness(arguments);
            case "create_business":
                return handleCreateBusiness(arguments);
            case "remove_business":
                return handleRemoveBusiness(arguments);
            case "update_business":
                return handleUpdateBusiness(arguments);
            default:
                ObjectNode error = objectMapper.createObjectNode();
                error.put("error", "Unknown tool: " + toolName);
                return error;
        }
    }
    
    private JsonNode handleListBusinesses() {
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        
        ObjectNode textContent = objectMapper.createObjectNode();
        textContent.put("type", "text");
        
        StringBuilder text = new StringBuilder("Available Businesses:\n\n");
        for (Business business : businesses) {
            text.append(String.format("ID: %s\nName: %s\nCategory: %s\nAddress: %s\nPhone: %s\nRating: %.1f/5.0\n\n",
                business.getId(), business.getName(), business.getCategory(), 
                business.getAddress(), business.getPhone(), business.getRating()));
        }
        
        textContent.put("text", text.toString());
        content.add(textContent);
        response.set("content", content);
        
        return response;
    }
    
    private JsonNode handleSearchBusinesses(JsonNode arguments) {
        String query = arguments.get("query").asText().toLowerCase();
        
        List<Business> matchingBusinesses = businesses.stream()
            .filter(business -> 
                business.getName().toLowerCase().contains(query) ||
                business.getCategory().toLowerCase().contains(query) ||
                business.getDescription().toLowerCase().contains(query))
            .toList();
        
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        
        ObjectNode textContent = objectMapper.createObjectNode();
        textContent.put("type", "text");
        
        if (matchingBusinesses.isEmpty()) {
            textContent.put("text", "No businesses found matching: " + query);
        } else {
            StringBuilder text = new StringBuilder("Search Results for '" + query + "':\n\n");
            for (Business business : matchingBusinesses) {
                text.append(String.format("ID: %s\nName: %s\nCategory: %s\nAddress: %s\nPhone: %s\nRating: %.1f/5.0\nDescription: %s\n\n",
                    business.getId(), business.getName(), business.getCategory(), 
                    business.getAddress(), business.getPhone(), business.getRating(), business.getDescription()));
            }
            textContent.put("text", text.toString());
        }
        
        content.add(textContent);
        response.set("content", content);
        
        return response;
    }
    
    private JsonNode handleGetBusiness(JsonNode arguments) {
        String businessId = arguments.get("id").asText();
        
        Optional<Business> businessOpt = businesses.stream()
            .filter(business -> business.getId().equals(businessId))
            .findFirst();
        
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        
        ObjectNode textContent = objectMapper.createObjectNode();
        textContent.put("type", "text");
        
        if (businessOpt.isEmpty()) {
            textContent.put("text", "Business not found with ID: " + businessId);
        } else {
            Business business = businessOpt.get();
            String text = String.format("Business Details:\n\nID: %s\nName: %s\nCategory: %s\nAddress: %s\nPhone: %s\nEmail: %s\nRating: %.1f/5.0\nDescription: %s",
                business.getId(), business.getName(), business.getCategory(), 
                business.getAddress(), business.getPhone(), business.getEmail(), 
                business.getRating(), business.getDescription());
            textContent.put("text", text);
        }
        
        content.add(textContent);
        response.set("content", content);
        
        return response;
    }
    
    private JsonNode handleCreateBusiness(JsonNode arguments) {
        try {
            // Extract business details from arguments
            String name = arguments.get("name").asText();
            String category = arguments.get("category").asText();
            String address = arguments.get("address").asText();
            String phone = arguments.get("phone").asText();
            String email = arguments.get("email").asText();
            String description = arguments.get("description").asText();
            double rating = arguments.has("rating") ? arguments.get("rating").asDouble() : 0.0;
            
            // Validate rating
            if (rating < 0.0 || rating > 5.0) {
                rating = 0.0;
            }
            
            // Generate new business ID
            String businessId = String.valueOf(nextBusinessId++);
            
            // Create new business
            Business newBusiness = new Business(businessId, name, category, address, phone, email, description, rating);
            businesses.add(newBusiness);
            
            ObjectNode response = objectMapper.createObjectNode();
            ArrayNode content = objectMapper.createArrayNode();
            
            ObjectNode textContent = objectMapper.createObjectNode();
            textContent.put("type", "text");
            
            String text = String.format("Business created successfully!\n\nID: %s\nName: %s\nCategory: %s\nAddress: %s\nPhone: %s\nEmail: %s\nRating: %.1f/5.0\nDescription: %s",
                businessId, name, category, address, phone, email, rating, description);
            textContent.put("text", text);
            
            content.add(textContent);
            response.set("content", content);
            
            return response;
            
        } catch (Exception e) {
            ObjectNode response = objectMapper.createObjectNode();
            ArrayNode content = objectMapper.createArrayNode();
            
            ObjectNode textContent = objectMapper.createObjectNode();
            textContent.put("type", "text");
            textContent.put("text", "Error creating business: " + e.getMessage());
            
            content.add(textContent);
            response.set("content", content);
            
            return response;
        }
    }
    
    private JsonNode handleRemoveBusiness(JsonNode arguments) {
        String businessId = arguments.get("id").asText();
        
        Optional<Business> businessToRemove = businesses.stream()
            .filter(business -> business.getId().equals(businessId))
            .findFirst();
        
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        
        ObjectNode textContent = objectMapper.createObjectNode();
        textContent.put("type", "text");
        
        if (businessToRemove.isEmpty()) {
            textContent.put("text", "Business not found with ID: " + businessId);
        } else {
            Business business = businessToRemove.get();
            businesses.remove(business);
            String text = String.format("Business removed successfully!\n\nRemoved Business:\nID: %s\nName: %s\nCategory: %s",
                business.getId(), business.getName(), business.getCategory());
            textContent.put("text", text);
        }
        
        content.add(textContent);
        response.set("content", content);
        
        return response;
    }
    
    private JsonNode handleUpdateBusiness(JsonNode arguments) {
        String businessId = arguments.get("id").asText();
        
        Optional<Business> businessToUpdate = businesses.stream()
            .filter(business -> business.getId().equals(businessId))
            .findFirst();
        
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        
        ObjectNode textContent = objectMapper.createObjectNode();
        textContent.put("type", "text");
        
        if (businessToUpdate.isEmpty()) {
            textContent.put("text", "Business not found with ID: " + businessId);
        } else {
            Business business = businessToUpdate.get();
            
            String name = arguments.has("name") ? arguments.get("name").asText() : business.getName();
            String category = arguments.has("category") ? arguments.get("category").asText() : business.getCategory();
            String address = arguments.has("address") ? arguments.get("address").asText() : business.getAddress();
            String phone = arguments.has("phone") ? arguments.get("phone").asText() : business.getPhone();
            String email = arguments.has("email") ? arguments.get("email").asText() : business.getEmail();
            String description = arguments.has("description") ? arguments.get("description").asText() : business.getDescription();
            double rating = arguments.has("rating") ? arguments.get("rating").asDouble() : business.getRating();
            
            if (rating < 0.0 || rating > 5.0) {
                rating = business.getRating();
            }
            
            Business updatedBusiness = new Business(businessId, name, category, address, phone, email, description, rating);
            int index = businesses.indexOf(business);
            businesses.set(index, updatedBusiness);
            
            String text = String.format("Business updated successfully!\n\nUpdated Business:\nID: %s\nName: %s\nCategory: %s\nAddress: %s\nPhone: %s\nEmail: %s\nRating: %.1f/5.0\nDescription: %s",
                businessId, name, category, address, phone, email, rating, description);
            textContent.put("text", text);
        }
        
        content.add(textContent);
        response.set("content", content);
        
        return response;
    }
}