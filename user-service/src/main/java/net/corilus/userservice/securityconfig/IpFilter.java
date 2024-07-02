package net.corilus.userservice.securityconfig;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class IpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteAddr = httpRequest.getRemoteAddr();
        System.out.println("remoteAddr: " + remoteAddr);

        // Adresse IP de l'API Gateway
        String apiGatewayIp = "172.27.0.1";

        if (!remoteAddr.equals(apiGatewayIp)) {
            throw new ServletException("Accès direct non autorisé");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Destruction du filtre
    }
    

}