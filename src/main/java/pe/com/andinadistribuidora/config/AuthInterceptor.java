package pe.com.andinadistribuidora.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String uri = request.getRequestURI(); //URL a la que usuario quiere acceder
        
        // Permitir acceso a recursos estáticos y página de login
        if (uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/") ||
            uri.equals("/login") || uri.equals("/") || uri.startsWith("/login")) {
            return true;
        }
        
        // Verificar si existe sesión activa
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}