import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ActionAgent extends Agent {
    protected void setup() {
        System.out.println("Starting ActionAgent: " + getAID().getName());
        addBehaviour(new BlockIPBehaviour());
    }

    private class BlockIPBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                String content = msg.getContent();
                if (content.startsWith("Block IP: ")) {
                    String ip = content.substring(10);
                    System.out.println("Received request to block IP: " + ip);

                    // Instead of actually blocking the IP, output the command
                    printBlockingCommand(ip);
                }
            } else {
                block();
            }
        }

        // Function to print the command that would block the IP for demos
        private void printBlockingCommand(String ip) {
            // Simulating the blocking via SSH and pfSense command
            String command = "sshpass -p 'password' ssh admin@pfsense 'pfctl -t blacklist -T add " + ip + "'";
            System.out.println("Simulating SSH command: " + command);

            /* this is how we would use the sshpass version to Command promt block the ip above is a simple version of just showing it works.

            String [] command = {"/bin/sh", "-c",
                    " sshpass -p 'password ' ssh admin@pfsense 'pfctl -t blacklist -T add " +
                            maliciousIP + "'"};
            Process blockProcess = Runtime . getRuntime ().exec( command );
            blockProcess . waitFor ();*/
        }

    }
}
