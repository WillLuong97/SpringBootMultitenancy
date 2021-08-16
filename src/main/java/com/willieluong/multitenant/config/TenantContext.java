package com.willieluong.multitenant.config;

/*
TenantContext class is used to store the tenant Identifier for each request.
We have used the InheritableThreadLocal variable.
This enables the child threads created from the main thread in our application to use the tenantId of the Parent Thread.
 */
public class TenantContext {
    private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant(){
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant){
        currentTenant.set(tenant);
    }

    public static void clear(){
        currentTenant.set(null);
    }
}
