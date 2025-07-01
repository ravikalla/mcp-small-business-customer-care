# Claude Desktop MCP Server Setup Guide

## 🚀 Installation Complete

Your MCP Small Business Customer Care server is now installed and configured in Claude Desktop!

## 📋 Setup Summary

✅ **JAR Built**: `target/small-business-customer-care-1.0.0.jar`  
✅ **Configuration Updated**: `~/Library/Application Support/Claude/claude_desktop_config.json`  
✅ **MCP Server Registered**: `small-business-customer-care`

## 🔄 Next Steps

### 1. Restart Claude Desktop
**IMPORTANT**: Close and reopen Claude Desktop to load the new MCP server.

### 2. Verify Installation
After restart, ask Claude Desktop: *"What business management tools do you have available?"*

You should see 6 business management tools available.

## 🛠️ Available Tools

| Tool | Description | Example Usage |
|------|-------------|---------------|
| **list_businesses** | Show all businesses | "List all businesses" |
| **search_businesses** | Search by name/category | "Find coffee shops" |
| **get_business** | Get specific business | "Show details for business ID 1" |
| **create_business** | Add new business | "Create a restaurant called Pizza Palace" |
| **update_business** | Update existing business | "Update business ID 2 phone to 555-9999" |
| **remove_business** | Delete business | "Remove business ID 3" |

## 💬 Example Conversations

### Basic Operations
```
You: "List all the businesses"
Claude: [Shows all 5 pre-loaded businesses]

You: "Search for technology companies"
Claude: [Shows Tech Repair Solutions]

You: "Get details for business ID 1"
Claude: [Shows Joe's Coffee Shop details]
```

### Creating Businesses
```
You: "Create a new business called 'Digital Marketing Pro' in the Services category"
Claude: [Creates business with ID 6]

You: "Add a bakery called 'Sweet Dreams Bakery' at 789 Baker St with phone 555-0199"
Claude: [Creates the bakery business]
```

### Updating Businesses
```
You: "Update business ID 1 to change the rating to 4.9"
Claude: [Updates Joe's Coffee Shop rating]

You: "Change the phone number for business ID 2 to 555-8888"
Claude: [Updates Tech Repair Solutions phone]
```

### Advanced Searches
```
You: "Find all businesses with 'repair' in their name or description"
Claude: [Shows Tech Repair Solutions and Mike's Auto Repair]

You: "Search for retail businesses"
Claude: [Shows Bella's Boutique]
```

## 🔧 Configuration Details

Your Claude Desktop config at `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "small-business-customer-care": {
      "command": "mvn",
      "args": [
        "-q",
        "exec:java",
        "-Dexec.mainClass=com.ravikalla.McpServerLauncher",
        "-Dexec.args="
      ],
      "cwd": "/Users/ravikalla/Desktop/projects/mcp-small-business-customer-care",
      "env": {}
    }
  }
}
```

## 🐛 Troubleshooting

### Server Not Loading
1. **Check Java**: Ensure Java 17+ is installed (`java -version`)
2. **Check Maven**: Ensure Maven is installed (`mvn -version`)
3. **Verify Project**: Confirm project exists at configured path
4. **Restart Claude**: Close and reopen Claude Desktop completely
5. **Check Logs**: Look for errors in Claude Desktop console

### Clean STDIO Communication
The server is configured with minimal logging to ensure clean MCP protocol communication:
- Spring Boot logs are silenced via `logback-spring.xml`
- Only essential MCP server logs are shown
- JSON responses are clean without log interference

### Rebuilding the JAR
If you make changes to the code:
```bash
cd /Users/ravikalla/Desktop/projects/mcp-small-business-customer-care
mvn clean package -DskipTests
```

### Testing the Server Standalone
Test the MCP server directly:
```bash
cd /Users/ravikalla/Desktop/projects/mcp-small-business-customer-care
./run-mcp-test.sh
```

## 📊 Pre-loaded Sample Data

Your server comes with 5 sample businesses:

1. **Joe's Coffee Shop** (Restaurant) - Rating: 4.5/5.0
2. **Tech Repair Solutions** (Technology) - Rating: 4.2/5.0  
3. **Green Thumb Landscaping** (Services) - Rating: 4.8/5.0
4. **Bella's Boutique** (Retail) - Rating: 4.3/5.0
5. **Mike's Auto Repair** (Automotive) - Rating: 4.6/5.0

## 🎯 Tips for Best Results

1. **Be Specific**: "Update business ID 2" rather than "update the tech company"
2. **Use Natural Language**: "Find coffee shops" works better than complex queries
3. **Verify Actions**: Ask to list businesses after creating/updating to confirm changes
4. **Explore Features**: Try different search terms to see the intelligent matching

## 📞 Support

- **Repository**: [mcp-small-business-customer-care](https://github.com/ravikalla/mcp-small-business-customer-care)
- **Main Project**: [ai-small-business-customercare](https://github.com/ravikalla/ai-small-business-customercare)

---

🎉 **Enjoy your new AI-powered business management capabilities in Claude Desktop!**