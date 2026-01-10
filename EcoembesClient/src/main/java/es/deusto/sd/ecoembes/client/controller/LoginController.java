package es.deusto.sd.ecoembes.client.controller;

import es.deusto.sd.ecoembes.client.model.Credentials;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;

public class LoginController {
    private IEcoembesServiceProxy serviceProxy = new HttpServiceProxy();
    @SuppressWarnings("unused")
    private String token;

    // 1. Login
    public boolean login(String email, String password) {
        try {
            this.token = serviceProxy.login(new Credentials(email, password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
