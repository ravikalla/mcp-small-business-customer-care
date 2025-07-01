# 🏪 Small Business Customer Care MCP Server

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-green.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M6-blue.svg)](https://spring.io/projects/spring-ai)
[![MCP Protocol](https://img.shields.io/badge/MCP-2024--11--05-purple.svg)](https://modelcontextprotocol.io/)

A **Spring AI-powered MCP (Model Context Protocol) server** for managing small business customer care operations through AI assistants like Claude Desktop.

## 🎯 Overview

This MCP server enables AI assistants to interact directly with business data, providing intelligent customer relationship management capabilities. Built using Spring AI's MCP framework for reliable, production-ready AI integration.

## ✨ Features

### **Complete Business Management**
- 🏢 **Create businesses** with full contact details and ratings
- 📋 **List all businesses** in your database
- 🔍 **Search businesses** by name, category, or description
- 👀 **Get detailed business information** by ID
- ✏️ **Update business details** (partial updates supported)
- 🗑️ **Remove businesses** from the system

### **Smart Search & Analytics**
- Full-text search across all business fields
- Category-based filtering
- Rating-based business evaluation
- Business hours management (via descriptions)

## 🚀 Quick Start

### **Prerequisites**
- Java 17 or higher
- Maven 3.6 or higher
- Claude Desktop (for AI assistant integration)

### **Installation & Build**
```bash
# Clone the repository
git clone https://github.com/ravikalla/mcp-small-business-customer-care.git
cd mcp-small-business-customer-care

# Build the application
mvn clean package

# Verify the JAR was created
ls -la target/small-business-customer-care-1.0.0.jar
```

### **Testing the Server**
```bash
# Test initialization
echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05"},"id":0}' | java -jar target/small-business-customer-care-1.0.0.jar

# Test listing tools
echo '{"jsonrpc":"2.0","method":"tools/list","params":{},"id":1}' | java -jar target/small-business-customer-care-1.0.0.jar

# Test business listing
echo '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"list_businesses","arguments":{}},"id":2}' | java -jar target/small-business-customer-care-1.0.0.jar
```

## 🔧 Claude Desktop Integration

### **Configuration**
Add this configuration to your Claude Desktop config file at:
- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "small-business-customer-care": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/your/mcp-small-business-customer-care/target/small-business-customer-care-1.0.0.jar"
      ],
      "env": {}
    }
  }
}
```

**Important**: Replace `/path/to/your/` with the actual absolute path to your project directory.

### **Restart Claude Desktop**
After updating the configuration:
1. Completely close Claude Desktop
2. Reopen Claude Desktop
3. The MCP server will automatically connect

## 🛠️ Available Business Tools

### **1. List All Businesses**
```json
{
  "method": "tools/call",
  "params": {
    "name": "list_businesses",
    "arguments": {}
  }
}
```

### **2. Search Businesses**
```json
{
  "method": "tools/call",
  "params": {
    "name": "search_businesses",
    "arguments": {
      "query": "restaurant"
    }
  }
}
```

### **3. Get Business Details**
```json
{
  "method": "tools/call",
  "params": {
    "name": "get_business",
    "arguments": {
      "id": "1"
    }
  }
}
```

### **4. Create New Business**
```json
{
  "method": "tools/call",
  "params": {
    "name": "create_business",
    "arguments": {
      "name": "Ravi Restaurant",
      "category": "Restaurant", 
      "address": "789 Pine Rd, Springfield",
      "phone": "555-0106",
      "email": "info@ravirestaurant.com",
      "description": "Family-friendly restaurant serving fresh cuisine. Hours: 9:00 AM - 5:00 PM daily",
      "rating": 4.5
    }
  }
}
```

### **5. Update Business**
```json
{
  "method": "tools/call",
  "params": {
    "name": "update_business",
    "arguments": {
      "id": "5",
      "description": "Full-service automotive repair and maintenance. Hours: 1:00 PM - 9:00 PM daily"
    }
  }
}
```

### **6. Remove Business**
```json
{
  "method": "tools/call",
  "params": {
    "name": "remove_business",
    "arguments": {
      "id": "3"
    }
  }
}
```

## 🎭 Real-World Usage Examples

Based on actual Claude Desktop conversations, here are examples of what you can do:

### **Example 1: Business Hours Management**
**User**: "Show me what businesses have timings"

**Claude Response**: Lists all businesses and notes that timing information is stored in descriptions.

**User**: "Delete green thumb business. Create new business called Ravi Restaurant that opens at 9AM and closes at 5PM. Also, Mike's auto repair shop opens at 1PM and closes at 9PM."

**Claude Actions**:
1. ✅ Deletes Green Thumb Landscaping
2. ✅ Creates Ravi Restaurant with 9AM-5PM hours in description
3. ✅ Updates Mike's Auto Repair with 1PM-9PM hours in description

### **Example 2: Business Listing**
**User**: "Show me the list of business names"

**Claude Response**:
- Joe's Coffee Shop
- Tech Repair Solutions  
- Bella's Boutique
- Mike's Auto Repair (Hours: 1:00 PM - 9:00 PM)
- Ravi Restaurant (Hours: 9:00 AM - 5:00 PM)

*Total: 5 businesses*

### **Example 3: Smart Search**
**User**: "Find all restaurants"

**Claude**: Uses `search_businesses` with query "restaurant" to find restaurant-category businesses.

## 🏗️ Technical Architecture

### **Spring AI MCP Framework**
- Uses `spring-ai-mcp-server-spring-boot-starter` for robust MCP protocol handling
- Annotation-driven tool definitions with `@Tool`
- Automatic tool discovery and registration via `ToolCallbacks.from()`
- Clean STDIO transport with proper logging configuration

### **Business Service Layer**
```java
@Service
public class BusinessService {
    
    @Tool(name = "list_businesses", description = "Get a list of all businesses")
    public List<Business> listBusinesses() { ... }
    
    @Tool(name = "search_businesses", description = "Search businesses by name, category, or description")
    public List<Business> searchBusinesses(String query) { ... }
    
    // ... other tool methods
}
```

### **Configuration**
```properties
spring.application.name=small-business-customer-care
spring.main.web-application-type=none
spring.ai.mcp.server.name=small-business-customer-care
spring.ai.mcp.server.version=1.0.0

# Required for STDIO transport
spring.main.banner-mode=off
logging.pattern.console=
```

## 🧪 Development & Testing

### **Running Tests**
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=SmallBusinessCustomerCareApplicationTest
```

### **Local Development**
```bash
# Start in development mode
mvn spring-boot:run

# Or run the JAR directly
java -jar target/small-business-customer-care-1.0.0.jar
```

### **Debugging**
The server logs to stderr (visible in Claude Desktop logs), while MCP communication happens via stdout/stdin.

## 📋 Default Business Data

The server comes pre-loaded with sample businesses:

1. **Joe's Coffee Shop** (Restaurant) - 4.5⭐
2. **Tech Repair Solutions** (Technology) - 4.2⭐  
3. **Green Thumb Landscaping** (Services) - 4.8⭐
4. **Bella's Boutique** (Retail) - 4.3⭐
5. **Mike's Auto Repair** (Automotive) - 4.6⭐

## 🚀 Production Deployment

### **Building for Production**
```bash
mvn clean package -DskipTests
```

### **Running in Production**
```bash
java -jar target/small-business-customer-care-1.0.0.jar
```

### **System Requirements**
- Java 17+ Runtime
- Minimum 512MB RAM
- Disk space for JAR file (~20MB)

## 🔧 Troubleshooting

### **Server Not Connecting**
1. Verify Java 17+ is installed: `java -version`
2. Check JAR file exists: `ls -la target/small-business-customer-care-1.0.0.jar`
3. Test server manually with echo commands above
4. Restart Claude Desktop completely
5. Check Claude Desktop logs for errors

### **Build Issues**
```bash
# Clean rebuild
mvn clean compile

# Skip tests if failing
mvn clean package -DskipTests

# Verbose output
mvn clean package -X
```

### **Path Issues**
- Use absolute paths in Claude Desktop configuration
- Escape spaces in paths with quotes: `"/path with spaces/"`
- Verify file permissions (JAR should be readable)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run `mvn test` to verify
6. Submit a pull request

## 📚 Additional Resources

- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [Spring AI Documentation](https://spring.io/projects/spring-ai)
- [Claude Desktop MCP Setup](https://claude.ai/docs)

## 📄 License

This project is open source and available under the MIT License.

---

**Ready to use?** Follow the Claude Desktop integration steps above and start managing your business data with AI! 🚀