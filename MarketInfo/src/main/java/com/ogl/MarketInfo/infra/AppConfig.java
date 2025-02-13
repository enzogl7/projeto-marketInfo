package com.ogl.MarketInfo.infra;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public OpenAPI configOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .description("Documentação dos endpoints da aplicação web MarketInfo\n\n⚠️ **Atenção:** Certifique-se de estar logado antes de realizar as requisições.")
                        .version("1.0")
                        .title("MarketInfo - Gestão de mercados")
                        .contact(new Contact().email("enzolima527@gmail.com"))
        );
    }
}
