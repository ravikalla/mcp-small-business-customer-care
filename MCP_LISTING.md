# 🏪 Small Business Customer Care MCP Server

A **Spring AI-powered MCP Server** that enables natural language management of small business data through Claude Desktop.

## 🚀 What It Does

Transform business operations with natural language:

- **"Show me all businesses"** → Instant business listings
- **"Create Ravi Restaurant with 9AM-5PM hours"** → New business created
- **"Find all auto repair shops"** → Smart search results  
- **"Delete Green Thumb Landscaping"** → Record removed
- **"Update Mike's hours to 1PM-9PM"** → Business updated

## ⚡ Features

- **6 Complete CRUD Operations**: Create, read, update, delete, list, and search businesses
- **Natural Language Interface**: No APIs to learn - just talk naturally
- **Spring AI Framework**: Production-ready MCP implementation
- **Pre-loaded Data**: 5 sample businesses to start experimenting immediately
- **Rich Business Model**: Name, category, address, phone, email, description, rating

## 🛠️ Technical Stack

- **Runtime**: Java 17+ with Spring Boot 3.4.4
- **MCP Framework**: Spring AI MCP Server 1.0.0-M6  
- **Protocol**: JSON-RPC 2.0 with STDIO transport
- **Architecture**: Annotation-driven tools (@Tool)

## 📦 Quick Setup

```bash
# Build the server
git clone https://github.com/ravikalla/mcp-small-business-customer-care.git
cd mcp-small-business-customer-care
mvn clean package

# Configure Claude Desktop
# Add to claude_desktop_config.json:
{
  "mcpServers": {
    "small-business-customer-care": {
      "command": "java",
      "args": ["-jar", "/path/to/target/small-business-customer-care-1.0.0.jar"]
    }
  }
}
```

## 🎯 Use Cases

- **Small Business Owners**: Manage business directories
- **Developers**: Learn MCP server development with Spring AI
- **AI Enthusiasts**: Experiment with natural language business operations
- **Prototyping**: Foundation for larger customer care systems

## 📊 Available Tools

| Tool | Purpose | Example |
|------|---------|---------|
| `list_businesses` | Get all businesses | "Show me all businesses" |
| `search_businesses` | Find by criteria | "Find restaurants" |
| `get_business` | Get specific business | "Show details for business 1" |
| `create_business` | Add new business | "Create a coffee shop..." |
| `update_business` | Modify existing | "Update the hours for..." |
| `remove_business` | Delete business | "Remove the landscaping business" |

## 🔧 Requirements

- Java 17 or higher
- Maven 3.6+
- Claude Desktop

## 📄 License

MIT License - Open source and free to use

---

**Perfect for**: Learning MCP development, prototyping business applications, or simply experimenting with AI-powered business management through natural language.