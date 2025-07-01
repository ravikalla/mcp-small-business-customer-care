package com.ravikalla.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class McpServerTest {
    
    private McpServer mcpServer;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        mcpServer = new McpServer();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    @DisplayName("Should handle initialize request")
    void testHandleInitialize() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("method", "initialize");
        
        JsonNode response = invokeHandleRequest(request);
        
        assertEquals("2024-11-05", response.get("protocolVersion").asText());
        assertTrue(response.get("capabilities").get("tools").asBoolean());
        assertEquals("small-business-customer-care", response.get("serverInfo").get("name").asText());
        assertEquals("1.0.0", response.get("serverInfo").get("version").asText());
    }
    
    @Test
    @DisplayName("Should handle tools/list request")
    void testHandleToolsList() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("method", "tools/list");
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("tools"));
        JsonNode tools = response.get("tools");
        assertEquals(6, tools.size());
        
        String[] expectedTools = {"list_businesses", "search_businesses", "get_business", "create_business", "remove_business", "update_business"};
        for (int i = 0; i < expectedTools.length; i++) {
            assertEquals(expectedTools[i], tools.get(i).get("name").asText());
        }
    }
    
    @Test
    @DisplayName("Should handle list_businesses tool call")
    void testListBusinesses() throws Exception {
        ObjectNode request = createToolCallRequest("list_businesses", objectMapper.createObjectNode());
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Available Businesses:"));
        assertTrue(text.contains("Joe's Coffee Shop"));
        assertTrue(text.contains("Tech Repair Solutions"));
        assertTrue(text.contains("Green Thumb Landscaping"));
        assertTrue(text.contains("Bella's Boutique"));
        assertTrue(text.contains("Mike's Auto Repair"));
    }
    
    @Test
    @DisplayName("Should handle search_businesses tool call")
    void testSearchBusinesses() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("query", "coffee");
        ObjectNode request = createToolCallRequest("search_businesses", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Search Results for 'coffee'"));
        assertTrue(text.contains("Joe's Coffee Shop"));
        assertFalse(text.contains("Tech Repair Solutions"));
    }
    
    @Test
    @DisplayName("Should handle search_businesses with no results")
    void testSearchBusinessesNoResults() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("query", "nonexistent");
        ObjectNode request = createToolCallRequest("search_businesses", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("No businesses found matching: nonexistent"));
    }
    
    @Test
    @DisplayName("Should handle get_business tool call")
    void testGetBusiness() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "1");
        ObjectNode request = createToolCallRequest("get_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business Details:"));
        assertTrue(text.contains("Joe's Coffee Shop"));
        assertTrue(text.contains("Restaurant"));
        assertTrue(text.contains("123 Main St, Springfield"));
        assertTrue(text.contains("555-0101"));
        assertTrue(text.contains("joe@joescoffee.com"));
    }
    
    @Test
    @DisplayName("Should handle get_business with invalid ID")
    void testGetBusinessInvalidId() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "999");
        ObjectNode request = createToolCallRequest("get_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business not found with ID: 999"));
    }
    
    @Test
    @DisplayName("Should handle create_business tool call")
    void testCreateBusiness() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("name", "Test Business");
        arguments.put("category", "Test Category");
        arguments.put("address", "123 Test St");
        arguments.put("phone", "555-0123");
        arguments.put("email", "test@test.com");
        arguments.put("description", "Test description");
        arguments.put("rating", 4.0);
        
        ObjectNode request = createToolCallRequest("create_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business created successfully!"));
        assertTrue(text.contains("Test Business"));
        assertTrue(text.contains("Test Category"));
        assertTrue(text.contains("ID: 6"));
    }
    
    @Test
    @DisplayName("Should handle create_business with default rating")
    void testCreateBusinessDefaultRating() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("name", "Test Business 2");
        arguments.put("category", "Test Category");
        arguments.put("address", "123 Test St");
        arguments.put("phone", "555-0123");
        arguments.put("email", "test@test.com");
        arguments.put("description", "Test description");
        
        ObjectNode request = createToolCallRequest("create_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business created successfully!"));
        assertTrue(text.contains("Rating: 0.0/5.0"));
    }
    
    @Test
    @DisplayName("Should handle create_business with invalid rating")
    void testCreateBusinessInvalidRating() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("name", "Test Business 3");
        arguments.put("category", "Test Category");
        arguments.put("address", "123 Test St");
        arguments.put("phone", "555-0123");
        arguments.put("email", "test@test.com");
        arguments.put("description", "Test description");
        arguments.put("rating", 6.0);
        
        ObjectNode request = createToolCallRequest("create_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business created successfully!"));
        assertTrue(text.contains("Rating: 0.0/5.0"));
    }
    
    @Test
    @DisplayName("Should handle remove_business tool call")
    void testRemoveBusiness() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "1");
        ObjectNode request = createToolCallRequest("remove_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business removed successfully!"));
        assertTrue(text.contains("Joe's Coffee Shop"));
        assertTrue(text.contains("Restaurant"));
        
        ObjectNode listRequest = objectMapper.createObjectNode();
        listRequest.put("method", "tools/call");
        ObjectNode listParams = objectMapper.createObjectNode();
        listParams.put("name", "list_businesses");
        listRequest.set("params", listParams);
        
        JsonNode listResponse = invokeHandleRequest(listRequest);
        String listText = listResponse.get("content").get(0).get("text").asText();
        assertFalse(listText.contains("Joe's Coffee Shop"));
    }
    
    @Test
    @DisplayName("Should handle remove_business with invalid ID")
    void testRemoveBusinessInvalidId() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "999");
        ObjectNode request = createToolCallRequest("remove_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business not found with ID: 999"));
    }
    
    @Test
    @DisplayName("Should handle unknown method")
    void testUnknownMethod() throws Exception {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("method", "unknown_method");
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("error"));
        assertEquals("Unknown method: unknown_method", response.get("error").asText());
    }
    
    @Test
    @DisplayName("Should handle unknown tool")
    void testUnknownTool() throws Exception {
        ObjectNode request = createToolCallRequest("unknown_tool", objectMapper.createObjectNode());
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("error"));
        assertEquals("Unknown tool: unknown_tool", response.get("error").asText());
    }
    
    @Test
    @DisplayName("Should handle update_business tool call")
    void testUpdateBusiness() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "1");
        arguments.put("name", "Updated Coffee Shop");
        arguments.put("rating", 4.8);
        
        ObjectNode request = createToolCallRequest("update_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business updated successfully!"));
        assertTrue(text.contains("Updated Coffee Shop"));
        assertTrue(text.contains("Rating: 4.8/5.0"));
        assertTrue(text.contains("ID: 1"));
    }
    
    @Test
    @DisplayName("Should handle update_business with partial data")
    void testUpdateBusinessPartialData() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "2");
        arguments.put("phone", "555-9999");
        
        ObjectNode request = createToolCallRequest("update_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business updated successfully!"));
        assertTrue(text.contains("Phone: 555-9999"));
        assertTrue(text.contains("Tech Repair Solutions"));
    }
    
    @Test
    @DisplayName("Should handle update_business with invalid ID")
    void testUpdateBusinessInvalidId() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "999");
        arguments.put("name", "Non-existent Business");
        
        ObjectNode request = createToolCallRequest("update_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business not found with ID: 999"));
    }
    
    @Test
    @DisplayName("Should handle update_business with invalid rating")
    void testUpdateBusinessInvalidRating() throws Exception {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("id", "3");
        arguments.put("rating", 6.0);
        
        ObjectNode request = createToolCallRequest("update_business", arguments);
        
        JsonNode response = invokeHandleRequest(request);
        
        assertTrue(response.has("content"));
        JsonNode content = response.get("content").get(0);
        assertEquals("text", content.get("type").asText());
        String text = content.get("text").asText();
        assertTrue(text.contains("Business updated successfully!"));
        assertTrue(text.contains("Rating: 4.8/5.0"));
    }
    
    @Test
    @DisplayName("Should increment nextBusinessId correctly")
    void testNextBusinessIdIncrement() throws Exception {
        ObjectNode arguments1 = createBusinessArguments("Business 1");
        ObjectNode request1 = createToolCallRequest("create_business", arguments1);
        JsonNode response1 = invokeHandleRequest(request1);
        String text1 = response1.get("content").get(0).get("text").asText();
        assertTrue(text1.contains("ID: 6"));
        
        ObjectNode arguments2 = createBusinessArguments("Business 2");
        ObjectNode request2 = createToolCallRequest("create_business", arguments2);
        JsonNode response2 = invokeHandleRequest(request2);
        String text2 = response2.get("content").get(0).get("text").asText();
        assertTrue(text2.contains("ID: 7"));
    }
    
    private ObjectNode createBusinessArguments(String name) {
        ObjectNode arguments = objectMapper.createObjectNode();
        arguments.put("name", name);
        arguments.put("category", "Test Category");
        arguments.put("address", "123 Test St");
        arguments.put("phone", "555-0123");
        arguments.put("email", "test@test.com");
        arguments.put("description", "Test description");
        return arguments;
    }
    
    private ObjectNode createToolCallRequest(String toolName, ObjectNode arguments) {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("method", "tools/call");
        
        ObjectNode params = objectMapper.createObjectNode();
        params.put("name", toolName);
        params.set("arguments", arguments);
        request.set("params", params);
        
        return request;
    }
    
    private JsonNode invokeHandleRequest(JsonNode request) throws Exception {
        return (JsonNode) ReflectionTestUtils.invokeMethod(mcpServer, "handleRequest", request);
    }
}