package com.sre;

public class DBMetricsLoggerContext {
    private static ThreadLocal<Object> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(Object tenant) {
    	//System.out.println("setCurrentTenant Object "+tenant.toString());
        currentTenant.set(tenant);
    }

    public static Object getCurrentTenant() {
        return currentTenant.get();
    }
}
