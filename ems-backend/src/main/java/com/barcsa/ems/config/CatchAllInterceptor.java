package com.barcsa.ems.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CatchAllInterceptor implements HandlerInterceptor {

    private final Environment environment;

    public CatchAllInterceptor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getRequestURI().startsWith("/api")) {
            return true; // Continue with the normal processing for API requests
        } else {
            // Handle non-API requests (fallback mechanism)

            // Specify your custom path to the folder containing index.html
            String customPath = "ems-frontend";
            String indexPath = Paths.get(customPath, "index.html").toString();

            String content = new String(Files.readAllBytes(Paths.get(indexPath)));

            response.setContentType("text/html");
            response.getWriter().write(content);

            return false; // Stop further processing for non-API requests
        }
    }
}