# MCP.SO Submission Content

## Title/Name:
Small Business Customer Care MCP Server

## Description:
A Spring AI-powered MCP Server that enables natural language management of small business data through Claude Desktop. Transform business operations with simple commands like "Show me all businesses", "Create a new restaurant", or "Find auto repair shops". Features 6 complete CRUD operations, pre-loaded sample data, and a rich business model with Spring AI framework integration.

Perfect for small business owners, developers learning MCP, and AI enthusiasts experimenting with natural language business operations.

## Tags:
business, data-management, spring-ai, java, crud, natural-language, productivity, tutorial

## Repository URL:
https://github.com/ravikalla/mcp-small-business-customer-care

## Server Configuration:
```json
{
  "mcpServers": {
    "small-business-customer-care": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/small-business-customer-care-1.0.0.jar"
      ],
      "env": {}
    }
  }
}
```

## Setup Instructions:
Prerequisites:
- Java 17 or higher
- Maven 3.6+
- Claude Desktop

Installation:
1. Clone: git clone https://github.com/ravikalla/mcp-small-business-customer-care.git
2. Build: mvn clean package
3. Update config path to your actual JAR location
4. Restart Claude Desktop

Replace "/path/to/" with your actual project path:
- macOS: /Users/username/path/to/project/target/small-business-customer-care-1.0.0.jar
- Windows: C:/Users/username/path/to/project/target/small-business-customer-care-1.0.0.jar
- Linux: /home/username/path/to/project/target/small-business-customer-care-1.0.0.jar

## Category:
Business & Productivity

## Features/Tools:
- list_businesses: Get all businesses
- search_businesses: Find by criteria  
- get_business: Get specific business details
- create_business: Add new business
- update_business: Modify existing business
- remove_business: Delete business

## Use Cases:
- Small business directory management
- Learning MCP server development
- Natural language data operations
- Spring AI framework demonstration
- Customer care system prototyping

## Overview:
Manage small business data through natural conversation with Claude Desktop. This MCP server transforms business operations into simple commands - no complex interfaces or APIs to learn.

**What you can do:**
• "Show me all businesses" → Get instant business listings
• "Create a coffee shop called Java Junction" → Add new businesses
• "Find all restaurants" → Smart search through your data
• "Update Mike's Auto Repair hours to 9AM-6PM" → Modify records
• "Delete the landscaping business" → Remove entries

**Why use it:**
- Natural language interface - just talk to Claude normally
- Complete business management (create, read, update, delete, search)
- Ready to use with 5 sample businesses included
- Built with production-ready Spring AI framework
- Perfect for learning MCP development or managing small business directories

Turn Claude Desktop into your personal business data assistant. Whether you're managing a business directory, learning about MCP servers, or prototyping customer care systems, this server makes data operations as simple as having a conversation.