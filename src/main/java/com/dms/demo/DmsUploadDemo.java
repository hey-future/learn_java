package com.dms.demo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

/**
 * 爱知汇开放接口 Demo
 * <p>
 *     仅限于参考
 * </p>
 * 包含：统一认证、小文件上传、大文件上传
 */
public class DmsUploadDemo {
    
    // 配置信息
    private static final String BASE_URL = "https://dms.multimediapress.cn/dms/api/v1"; //接口地址
    private static final String USERNAME = "xxx"; //账号 联系管理员提供
    private static final String PASSWORD = "xxx";//密码，认证的时候要base64编码 联系管理员提供
    private static final int NODE_ID = 6361; // 课程节点编号，联系管理员提供
    
    // 大文件分片大小：5MB
    private static final int CHUNK_SIZE = 5 * 1024 * 1024;
    
    private String token; // 认证token
    
    public static void main(String[] args) {
        DmsUploadDemo demo = new DmsUploadDemo();
        
        try {
            // 1. 统一认证获取token
            System.out.println("=== 开始认证 ===");
            demo.authenticate();
            System.out.println("认证成功，Token: " + demo.token);
            
            // 2. 创建课程获取课程编号
            System.out.println("\n=== 创建课程 ===");
            int resourceId = demo.createResource("测试课程_" + System.currentTimeMillis());
            System.out.println("课程创建成功，课程编号: " + resourceId);
            
            // 3. 小文件上传示例
            System.out.println("\n=== 小文件上传 ===");
            File smallFile = getResourceFile("static/测试小文件.png");
            System.out.println("小文件路径: " + smallFile.getAbsolutePath());
            System.out.println("小文件大小: " + smallFile.length() + " 字节");
            int smallFileId = demo.uploadSmallFile(smallFile, resourceId, 0, 0);
            System.out.println("小文件上传成功，文件ID: " + smallFileId);
            
            // 4. 大文件上传示例
            System.out.println("\n=== 大文件上传 ===");
            File largeFile = getResourceFile("static/测试视频.mp4");
            System.out.println("大文件路径: " + largeFile.getAbsolutePath());
            System.out.println("大文件大小: " + largeFile.length() + " 字节");
            int largeFileId = demo.uploadLargeFile(largeFile, resourceId, 0, 1);
            System.out.println("大文件上传成功，文件ID: " + largeFileId);
            
            System.out.println("\n=== 所有操作完成 ===");
            
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取resources目录下的文件
     */
    private static File getResourceFile(String relativePath) throws Exception {
        // 先尝试从classpath获取
        java.net.URL url = DmsUploadDemo.class.getClassLoader().getResource(relativePath);
        if (url != null) {
            return new File(url.toURI());
        }
        
        // 如果classpath找不到，尝试从项目目录获取
        File file = new File("src/main/resources/" + relativePath);
        if (file.exists()) {
            return file;
        }
        
        throw new FileNotFoundException("找不到资源文件: " + relativePath);
    }
    
    /**
     * 创建课程
     * 
     * @param title 课程名称
     * @return 创建的课程ID（resourceId）
     */
    public int createResource(String title) throws Exception {
        String url = BASE_URL + "/create/resource";
        
        // 构建JSON请求体
        StringBuilder jsonBody = new StringBuilder();
        jsonBody.append("{");
        jsonBody.append("\"nodeId\":").append(NODE_ID).append(",");
        jsonBody.append("\"title\":\"").append(title).append("\",");
        jsonBody.append("\"mediaType\":1");  // 1：视频课程
        jsonBody.append("}");
        
        // 发送POST请求
        String response = sendPostRequest(url, jsonBody.toString(), this.token);
        
        // 解析响应获取资源ID
        int resourceId = extractFileId(response);
        
        if (resourceId <= 0) {
            throw new Exception("创建课程失败: " + response);
        }
        
        return resourceId;
    }
    
    /**
     * 统一认证
     */
    public void authenticate() throws Exception {
        String url = BASE_URL + "/token";
        
        // Base64编码密码
        String encodedPassword = Base64.getEncoder().encodeToString(PASSWORD.getBytes(StandardCharsets.UTF_8));
        
        // 构建JSON请求体
        String jsonBody = String.format("{\"userName\":\"%s\",\"password\":\"%s\"}", 
                USERNAME, encodedPassword);
        
        // 发送POST请求
        String response = sendPostRequest(url, jsonBody, null);
        
        // 解析响应获取token（简单的字符串解析）
        this.token = extractToken(response);
        
        if (this.token == null || this.token.isEmpty()) {
            throw new Exception("认证失败，无法获取token");
        }
    }
    
    /**
     * 小文件上传（文件大小 < 5M）
     * 
     * @param file 要上传的文件
     * @param parentId 资源id
     * @param pid 资源分类id（默认0）
     * @param ai 是否开启自动智能审核 0不开启 1开启
     * @return 上传成功后的文件ID
     */
    public int uploadSmallFile(File file, int parentId, int pid, int ai) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + file.getAbsolutePath());
        }
        
        if (file.length() >= 5 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小超过5MB，请使用大文件上传方法");
        }
        
        String url = BASE_URL + "/upload/ai/" + ai;
        
        // 计算thirdHash（SHA1算法）
        String thirdHash = calculateSha1(file.getName() + NODE_ID + parentId);
        
        // 发送multipart/form-data请求
        String response = sendMultipartRequest(url, file, parentId, pid, thirdHash, null);
        
        // 解析返回的文件ID
        return extractFileId(response);
    }
    
    /**
     * 大文件上传（文件大小 > 5M，分片上传）
     * 
     * @param file 要上传的文件
     * @param parentId 资源id
     * @param pid 资源分类id（默认0）
     * @param ai 是否开启自动智能审核 0不开启 1开启
     * @return 上传成功后的文件ID
     */
    public int uploadLargeFile(File file, int parentId, int pid, int ai) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + file.getAbsolutePath());
        }
        
        long fileSize = file.length();
        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);
        
        System.out.println("文件总大小: " + fileSize + " 字节");
        System.out.println("分片总数: " + totalChunks);
        
        // 生成文件标识
        long timestamp = System.currentTimeMillis();
        String baseIdentifier = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        
        // 计算thirdHash（使用原文件名）
        String thirdHash = calculateSha1(file.getName() + NODE_ID + parentId);
        
        String url = BASE_URL + "/fileup/nodeId/" + NODE_ID;
        
        // 逐片上传
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            for (int chunkNumber = 0; chunkNumber < totalChunks; chunkNumber++) {
                // 读取当前分片
                long startPos = (long) chunkNumber * CHUNK_SIZE;
                int currentChunkSize = (int) Math.min(CHUNK_SIZE, fileSize - startPos);
                
                byte[] chunkData = new byte[currentChunkSize];
                raf.seek(startPos);
                raf.readFully(chunkData);
                
                // 生成分片标识
                String identifier = baseIdentifier + "_0_" + timestamp + "_" + chunkNumber;
                
                // 检查分片是否已上传
                if (isChunkUploaded(identifier)) {
                    System.out.println("分片 " + (chunkNumber + 1) + "/" + totalChunks + " 已上传，跳过");
                    continue;
                }
                
                // 上传分片
                System.out.println("上传分片 " + (chunkNumber + 1) + "/" + totalChunks + 
                        " (大小: " + currentChunkSize + " 字节)");
                
                String response = sendChunkRequest(url, file.getName(), chunkData, 
                        chunkNumber, identifier, currentChunkSize, fileSize, totalChunks, 
                        parentId, pid, ai, thirdHash);
                
                // 检查是否上传成功
                if (!response.contains("\"code\":200")) {
                    throw new Exception("分片上传失败: " + response);
                }
                
                // 如果是最后一片，解析文件ID
                if (chunkNumber == totalChunks - 1) {
                    return extractFileId(response);
                }
            }
        }
        
        throw new Exception("大文件上传未返回文件ID");
    }
    
    /**
     * 检查分片是否已上传
     */
    private boolean isChunkUploaded(String md5) throws Exception {
        String url = BASE_URL + "/upload/isUpload?md5=" + URLEncoder.encode(md5, "UTF-8");
        
        String response = sendPostRequest(url, "", token);
        
        // 解析status字段：0未上传 1已上传 2上传中
        if (response.contains("\"status\":1") || response.contains("\"status\":2")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 发送POST请求（JSON格式）
     */
    private String sendPostRequest(String urlStr, String jsonBody, String authToken) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            if (authToken != null && !authToken.isEmpty()) {
                conn.setRequestProperty("Authorization", authToken);
            }
            
            // 发送请求体
            if (jsonBody != null && !jsonBody.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            }
            
            // 读取响应
            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) 
                    ? conn.getInputStream() : conn.getErrorStream();
            
            return readInputStream(is);
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * 发送multipart/form-data请求（小文件上传）
     */
    private String sendMultipartRequest(String urlStr, File file, int parentId, 
            int pid, String thirdHash, String authToken) throws Exception {
        
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            if (authToken == null) {
                authToken = this.token;
            }
            conn.setRequestProperty("Authorization", authToken);
            
            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {
                
                // 添加表单字段
                addFormField(writer, boundary, "parentNode", String.valueOf(NODE_ID));
                addFormField(writer, boundary, "parentId", String.valueOf(parentId));
                addFormField(writer, boundary, "pid", String.valueOf(pid));
                if (thirdHash != null) {
                    addFormField(writer, boundary, "thirdHash", thirdHash);
                }
                
                // 添加文件
                addFileField(writer, os, boundary, "file", file);
                
                // 结束boundary
                writer.append("--").append(boundary).append("--").append("\r\n");
                writer.flush();
            }
            
            // 读取响应
            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) 
                    ? conn.getInputStream() : conn.getErrorStream();
            
            return readInputStream(is);
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * 发送分片上传请求
     */
    private String sendChunkRequest(String urlStr, String fileName, byte[] chunkData,
            int chunkNumber, String identifier, int currentChunkSize, long totalSize,
            int totalChunks, int parentId, int pid, int ai, String thirdHash) throws Exception {
        
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Authorization", this.token);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {
                
                // 添加表单字段
                addFormField(writer, boundary, "parentNode", String.valueOf(NODE_ID));
                addFormField(writer, boundary, "parentId", String.valueOf(parentId));
                addFormField(writer, boundary, "pid", String.valueOf(pid));
                addFormField(writer, boundary, "chunkNumber", String.valueOf(chunkNumber));
                addFormField(writer, boundary, "identifier", identifier);
                addFormField(writer, boundary, "chunkSize", String.valueOf(CHUNK_SIZE));
                addFormField(writer, boundary, "currentChunkSize", String.valueOf(currentChunkSize));
                addFormField(writer, boundary, "totalSize", String.valueOf(totalSize));
                addFormField(writer, boundary, "totalChunks", String.valueOf(totalChunks));
                addFormField(writer, boundary, "fileName", fileName);
                addFormField(writer, boundary, "ai", String.valueOf(ai));
                if (thirdHash != null) {
                    addFormField(writer, boundary, "thirdHash", thirdHash);
                }
                
                // 添加文件分片
                addFileChunk(writer, os, boundary, "file", fileName, chunkData);
                
                // 结束boundary
                writer.append("--").append(boundary).append("--").append("\r\n");
                writer.flush();
            }
            
            // 读取响应
            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) 
                    ? conn.getInputStream() : conn.getErrorStream();
            
            return readInputStream(is);
            
        } finally {
            conn.disconnect();
        }
    }
    
    /**
     * 添加表单字段
     */
    private void addFormField(PrintWriter writer, String boundary, String name, String value) {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append("\r\n");
        writer.append("\r\n");
        writer.append(value).append("\r\n");
        writer.flush();
    }
    
    /**
     * 添加文件字段
     */
    private void addFileField(PrintWriter writer, OutputStream os, String boundary, 
            String fieldName, File file) throws IOException {
        
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
              .append("\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n");
        writer.append("\r\n");
        writer.flush();
        
        // 写入文件内容
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
        
        writer.append("\r\n");
        writer.flush();
    }
    
    /**
     * 添加文件分片
     */
    private void addFileChunk(PrintWriter writer, OutputStream os, String boundary,
            String fieldName, String fileName, byte[] chunkData) throws IOException {
        
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName)
              .append("\"; filename=\"").append(fileName).append("\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n");
        writer.append("\r\n");
        writer.flush();
        
        // 写入分片数据
        os.write(chunkData);
        os.flush();
        
        writer.append("\r\n");
        writer.flush();
    }
    
    /**
     * 读取输入流
     */
    private String readInputStream(InputStream is) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
    
    /**
     * 从响应中提取token（简单的字符串解析）
     */
    private String extractToken(String jsonResponse) {
        int tokenIndex = jsonResponse.indexOf("\"token\":\"");
        if (tokenIndex == -1) {
            return null;
        }
        
        int startIndex = tokenIndex + 9; // "token":"后的位置
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        
        if (endIndex == -1) {
            return null;
        }
        
        return jsonResponse.substring(startIndex, endIndex);
    }
    
    /**
     * 从响应中提取文件ID
     */
    private int extractFileId(String jsonResponse) {
        int dataIndex = jsonResponse.indexOf("\"data\":");
        if (dataIndex == -1) {
            throw new RuntimeException("响应中未找到data字段");
        }
        
        // 查找data后的数字
        int startIndex = dataIndex + 7;
        while (startIndex < jsonResponse.length() && 
               (jsonResponse.charAt(startIndex) == ' ' || jsonResponse.charAt(startIndex) == ':')) {
            startIndex++;
        }
        
        int endIndex = startIndex;
        while (endIndex < jsonResponse.length() && 
               Character.isDigit(jsonResponse.charAt(endIndex))) {
            endIndex++;
        }
        
        if (endIndex == startIndex) {
            throw new RuntimeException("无法解析文件ID");
        }
        
        return Integer.parseInt(jsonResponse.substring(startIndex, endIndex));
    }
    
    /**
     * 计算SHA1哈希值
     */
    private String calculateSha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
