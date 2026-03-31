package taskengine.taskengine_project_service.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class Oauth2feignConfig {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository repo,
                                                                 OAuth2AuthorizedClientService service){
        OAuth2AuthorizedClientProvider provider=
                OAuth2AuthorizedClientProviderBuilder
                        .builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager clientManager=new
                AuthorizedClientServiceOAuth2AuthorizedClientManager(repo,service);

        clientManager.setAuthorizedClientProvider(provider);
        return clientManager;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            OAuth2AuthorizedClientManager manager) {

        return requestTemplate -> {
            OAuth2AuthorizeRequest request =
                    OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                            .principal("project-service")
                            .build();

            OAuth2AuthorizedClient client = manager.authorize(request);

            assert client != null;
            String token = client.getAccessToken().getTokenValue();

            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}

