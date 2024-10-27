import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefendingAgent extends Agent {
    private Map<String, Integer> ipCounter = new HashMap<>();
    private final int ICMP_THRESHOLD = 10;
    private Set<String> proxyList = new HashSet<>();

    protected void setup() {
        System.out.println("Starting Defending Agent: " + getAID().getName());
        loadProxyList();  // Load proxy list from file
        addBehaviour(new MonitorTrafficBehaviour());
    }
    private void loadProxyList() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/proxylist.txt"))) {
            String ip;
            while ((ip = br.readLine()) != null) {
                proxyList.add(ip.trim());
            }
            System.out.println("Proxy list loaded with " + proxyList.size() + " IPs.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class MonitorTrafficBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            try {
                // Monitor all traffic and capture source IP and packet size
                String[] command = {"tshark", "-i", "ens33", "-T", "fields", "-e", "ip.src", "-e", "frame.len"};
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\t");
                    if (tokens.length >= 2) {
                        String srcIp = tokens[0].trim();
                        int packetSize = Integer.parseInt(tokens[1].trim());

                        System.out.println("Captured packet: " + srcIp + " | Size: " + packetSize);

                        // Block immediately if the IP is in the proxy list
                        if (proxyList.contains(srcIp)) {
                            System.out.println("IP from proxy list detected: " + srcIp);
                            sendBlockRequest(srcIp);
                            continue;
                        }

                        // Count pings from the IP
                        ipCounter.put(srcIp, ipCounter.getOrDefault(srcIp, 0) + 1);

                        // If the packet size exceeds 1000 or the number of pings exceeds the threshold, block the IP
                        if (packetSize > 1000 || ipCounter.get(srcIp) > ICMP_THRESHOLD) {
                            System.out.println("Malicious IP detected: " + srcIp + " | Size: " + packetSize);
                            sendBlockRequest(srcIp);
                            ipCounter.put(srcIp, 0);  // Reset counter for this IP
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Function to send a block request to the ActionAgent
        private void sendBlockRequest(String ip) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Block IP: " + ip);
            msg.addReceiver(new AID("actionAgent", AID.ISLOCALNAME));  // ActionAgent needs to be registered with this name
            send(msg);
            System.out.println("Sent block request to ActionAgent for IP: " + ip);
        }
    }
}
