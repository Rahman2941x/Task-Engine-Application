package com.taskengine.taskengine_task_service.configuration;


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class OAuth2FeignConfig {

//    @Bean
//        public OAuth2AuthorizedClientManager authorizedClientManager(
//                ClientRegistrationRepository clientRegistrationRepository,
//                OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//            OAuth2AuthorizedClientProvider provider =
//                    OAuth2AuthorizedClientProviderBuilder.builder()
//                            .clientCredentials()
//                            .build();
//
//            DefaultOAuth2AuthorizedClientManager manager =
//                    new DefaultOAuth2AuthorizedClientManager(
//                            clientRegistrationRepository,
//                            authorizedClientRepository);
//
//            manager.setAuthorizedClientProvider(provider);
//            return manager;
//        }
//
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor(
//            OAuth2AuthorizedClientManager manager) {
//
//        return requestTemplate -> {
//
//            OAuth2AuthorizeRequest request =
//                    OAuth2AuthorizeRequest
//                            .withClientRegistrationId("internal-client")
//                            .principal("task-service")
//                            .build();
//
//            OAuth2AuthorizedClient client = manager.authorize(request);
//
//            String accessToken = client.getAccessToken().getTokenValue();
//
//            requestTemplate.header("Authorization", "Bearer " + accessToken);
//        };
//    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository repo,
            OAuth2AuthorizedClientService service) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(repo, service);

        manager.setAuthorizedClientProvider(provider);

        return manager;
    }


    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            OAuth2AuthorizedClientManager manager) {

        return requestTemplate -> {
            OAuth2AuthorizeRequest request =
                    OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                            .principal("task-service")
                            .build();

            OAuth2AuthorizedClient client = manager.authorize(request);

            assert client != null;
            String token = client.getAccessToken().getTokenValue();

            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}