package com.chinasofti.ark.bdadp.security;

import java.security.Principal;
import java.util.List;

public interface SecurityProvider {

    public SecurityConfig getConf();

    public Principal getPrincipal();

    public Credential<?> getCredential();

    public Boolean authenticate();

    public List<Permission> authorize();

}
