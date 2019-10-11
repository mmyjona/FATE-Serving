package com.webank.ai.fate.jmx.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpAddressUtil {

    public static String getInnetIp() throws SocketException {
        String localip = null;
        String netip = null;
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        boolean finded = false;
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}
