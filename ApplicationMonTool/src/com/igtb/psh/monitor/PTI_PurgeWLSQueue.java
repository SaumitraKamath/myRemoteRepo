package com.igtb.psh.monitor;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.ObjectName;
import javax.naming.Context;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;

public class PTI_PurgeWLSQueue {

    private static final String WLS_USERNAME = "weblogic";
    private static final String WLS_PASSWORD = "weblogic";
    private static final String WLS_HOST = "localhost";
    private static final int WLS_PORT = 7001;
    private static final String JMS_SERVER = "wlsbJMSServer";
    private static final String JMS_DESTINATION = "test.q";

    private static JMXConnector getMBeanServerConnector(String jndiName) throws Exception {
        Hashtable<String,String> h = new Hashtable<String,String>();
        JMXServiceURL serviceURL = new JMXServiceURL("t3", WLS_HOST, WLS_PORT, jndiName);
        h.put(Context.SECURITY_PRINCIPAL, WLS_USERNAME);
        h.put(Context.SECURITY_CREDENTIALS, WLS_PASSWORD);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        JMXConnector connector = JMXConnectorFactory.connect(serviceURL, h);
        return connector;
    }

    public static void main(String[] args) {
        try {
            JMXConnector connector = 
              getMBeanServerConnector("/jndi/"+RuntimeServiceMBean.MBEANSERVER_JNDI_NAME);
            MBeanServerConnection mbeanServerConnection = 
              connector.getMBeanServerConnection();

            ObjectName service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
            ObjectName serverRuntime = (ObjectName) mbeanServerConnection.getAttribute(service, "ServerRuntime");
            ObjectName jmsRuntime = (ObjectName) mbeanServerConnection.getAttribute(serverRuntime, "JMSRuntime");
            ObjectName[] jmsServers = (ObjectName[]) mbeanServerConnection.getAttribute(jmsRuntime, "JMSServers");
            for (ObjectName jmsServer: jmsServers) {
                if (JMS_SERVER.equals(jmsServer.getKeyProperty("Name"))) {
                    ObjectName[] destinations = (ObjectName[]) mbeanServerConnection.getAttribute(jmsServer, "Destinations");
                    for (ObjectName destination: destinations) {
                        if (destination.getKeyProperty("Name").endsWith("!"+JMS_DESTINATION)) {
                            Object o = mbeanServerConnection.invoke(
                                destination,
                                "deleteMessages",
                                new Object[] {""},        // selector expression
                                new String[] {"java.lang.String"});
                            System.out.println("Result: "+o);
                            break;
                        }
                    }
                    break;
                }
            }
            connector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}