package com.ibrahim;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws LifecycleException {
        System.out.println( "Hello World!" );
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);
        tomcat.getConnector().setProperty("address", "127.0.0.1");

        Context context = tomcat.addContext("", null);
        Tomcat.addServlet(context,"helloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello", "helloServlet");

        try {
            tomcat.start();
            System.out.println("Server started at http://localhost:8082/hello");
            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace(); // Show the real error
        }
    }
}
