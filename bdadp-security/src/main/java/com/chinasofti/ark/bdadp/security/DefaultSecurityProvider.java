package com.chinasofti.ark.bdadp.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class DefaultSecurityProvider implements SecurityProvider {

    @Override
    public SecurityConfig getConf() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Principal getPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Credential<String> getCredential() {
        return new Credential<String>("test");
    }

    @Override
    public Boolean authenticate() {
        // TODO: default authenticate implementation
        return false;
    }

    @Override
    public List<Permission> authorize() {
        if (authenticate()) {
            // TODO: search the permissions for the principal
        }
        return new ArrayList<Permission>();
    }

}
