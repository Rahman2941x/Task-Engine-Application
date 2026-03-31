package com.taskengine.taskengine_user_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth_client")
public class OAuthClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "client_name")
    private String clientId;
    @Column(name = "client_secret")
    private String ClientSecret;
    @Column(name = "is_active")
    private  Boolean isActive;


    @PrePersist
    public  void prePersist(){
        this.isActive=true;
    }

    public OAuthClient() {
    }

    public OAuthClient(Long id, String clientId, String clientSecret, Boolean isActive) {
        this.id = id;
        this.clientId = clientId;
        ClientSecret = clientSecret;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return ClientSecret;
    }

    public void setClientSecret(String clientSecret) {
        ClientSecret = clientSecret;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ClientCredential{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", ClientSecret='" + ClientSecret + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
