package pe.com.andinadistribuidora.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor) //Registra tu AuthInterceptor como un interceptor activo.
                .addPathPatterns("/**") //Indica que debe aplicarse a todas las rutas ("/**").
                .excludePathPatterns("/login", "/css/**", "/js/**", "/images/**", "/styles/**", "/api/**"); //Excluye ciertas rutas donde no debe ejecutarse (login, CSS, JS, imágenes y styles donde estan mis estilos).
    }
}