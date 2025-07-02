package com.ravikalla.controller;

import com.ravikalla.service.BusinessService;
import com.ravikalla.model.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for MCP server web mode (hosting compatibility)
 * Only active when 'web' profile is enabled
 */
@RestController
@RequestMapping("/mcp")
@Profile("web")
@CrossOrigin(origins = "*")
public class McpRestController {

    @Autowired
    private BusinessService businessService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "small-business-customer-care",
            "version", "1.0.0"
        ));
    }

    /**
     * Get server information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "name", "small-business-customer-care",
            "description", "MCP Server for Small Business Customer Care Management",
            "version", "1.0.0",
            "tools", List.of(
                "list_businesses",
                "search_businesses", 
                "get_business",
                "create_business",
                "update_business",
                "remove_business"
            )
        ));
    }

    /**
     * List all businesses
     */
    @GetMapping("/businesses")
    public ResponseEntity<List<Business>> listBusinesses() {
        return ResponseEntity.ok(businessService.listBusinesses());
    }

    /**
     * Search businesses
     */
    @GetMapping("/businesses/search")
    public ResponseEntity<List<Business>> searchBusinesses(@RequestParam String query) {
        return ResponseEntity.ok(businessService.searchBusinesses(query));
    }

    /**
     * Get specific business
     */
    @GetMapping("/businesses/{id}")
    public ResponseEntity<Business> getBusiness(@PathVariable String id) {
        Business business = businessService.getBusiness(id);
        if (business != null) {
            return ResponseEntity.ok(business);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create new business
     */
    @PostMapping("/businesses")
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) {
        Business created = businessService.createBusiness(
            business.getName(),
            business.getCategory(),
            business.getAddress(),
            business.getPhone(),
            business.getEmail(),
            business.getDescription(),
            business.getRating()
        );
        return ResponseEntity.ok(created);
    }

    /**
     * Update existing business
     */
    @PutMapping("/businesses/{id}")
    public ResponseEntity<Business> updateBusiness(@PathVariable String id, @RequestBody Business business) {
        Business updated = businessService.updateBusiness(
            id,
            business.getName(),
            business.getCategory(),
            business.getAddress(),
            business.getPhone(),
            business.getEmail(),
            business.getDescription(),
            business.getRating()
        );
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete business
     */
    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<Map<String, String>> removeBusiness(@PathVariable String id) {
        Business removedBusiness = businessService.removeBusiness(id);
        if (removedBusiness != null) {
            return ResponseEntity.ok(Map.of(
                "message", "Business '" + removedBusiness.getName() + "' deleted successfully",
                "id", removedBusiness.getId()
            ));
        }
        return ResponseEntity.notFound().build();
    }
}