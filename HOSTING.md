# MCP.so Hosting Guide

This document describes how to deploy the Small Business Customer Care MCP Server for hosting on MCP.so platform.

## 🏗️ Architecture

The server supports two modes:
- **STDIO Mode**: For local Claude Desktop integration
- **Web Mode**: For MCP.so hosting and remote access

## 🚀 Quick Start for Hosting

### 1. Build for Web Mode
```bash
# Build the application
mvn clean package -DskipTests

# Run in web mode (for testing)
java -jar -Dspring.profiles.active=web target/small-business-customer-care-1.0.0.jar
```

### 2. Docker Deployment
```bash
# Build Docker image
docker build -t small-business-customer-care:latest .

# Run container
docker run -p 8080:8080 small-business-customer-care:latest

# Test health endpoint
curl http://localhost:8080/api/mcp/health
```

### 3. Available Endpoints

#### Health & Info
- `GET /api/mcp/health` - Health check
- `GET /api/mcp/info` - Server information

#### Business Operations
- `GET /api/mcp/businesses` - List all businesses
- `GET /api/mcp/businesses/search?query={query}` - Search businesses
- `GET /api/mcp/businesses/{id}` - Get specific business
- `POST /api/mcp/businesses` - Create new business
- `PUT /api/mcp/businesses/{id}` - Update business
- `DELETE /api/mcp/businesses/{id}` - Delete business

## 📋 Configuration Files

### chatmcp.yaml
Contains MCP server metadata and configuration for the hosting platform.

### Dockerfile
Multi-stage build optimized for production deployment.

### Profile-based Configuration
- `application-stdio.properties` - STDIO mode for Claude Desktop
- `application-web.properties` - Web mode for hosting

## 🔧 Environment Variables

For hosting deployment:
```bash
SPRING_PROFILES_ACTIVE=web
JAVA_OPTS="-Xmx512m -Xms256m"
```

## 🧪 Testing

### Local STDIO Mode (Default)
```bash
java -jar target/small-business-customer-care-1.0.0.jar
echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05"},"id":0}' | java -jar target/small-business-customer-care-1.0.0.jar
```

### Web Mode Testing
```bash
# Start in web mode
java -jar -Dspring.profiles.active=web target/small-business-customer-care-1.0.0.jar

# Test endpoints
curl http://localhost:8080/api/mcp/health
curl http://localhost:8080/api/mcp/businesses
curl "http://localhost:8080/api/mcp/businesses/search?query=coffee"
```

## 📦 MCP.so Submission

1. **Repository**: https://github.com/ravikalla/mcp-small-business-customer-care
2. **Docker Image**: Built from Dockerfile
3. **Configuration**: chatmcp.yaml
4. **Health Check**: `/api/mcp/health`
5. **Documentation**: This file and README.md

## 🔒 Security

- Non-root user in Docker container
- CORS enabled for web mode
- No local file system access
- In-memory data only (no persistence)

## 📚 Compliance

✅ **MCP.so Requirements:**
- Open source with MIT license
- No local data access
- Container-ready deployment
- Health check endpoint
- REST API transport
- Proper documentation

The server is ready for MCP.so hosting and playground integration!