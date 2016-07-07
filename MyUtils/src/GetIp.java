import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetIp {

	public static void main(String[] args) throws SocketException {
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			System.out.println(netInterface.getName());
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					System.out.println("本机的IP = " + ip.getHostAddress());
				}
			}
		}
	}

}
