import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainContainer {
    public static void main(String[] args) {
        // Initialize the Jade runtime environment
        Runtime rt = Runtime.instance();

        // Set up the main container
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.MAIN_PORT, "1099");
        ContainerController mainContainer = rt.createMainContainer(profile);

        try {
            // Start the defending agent
            AgentController agent = mainContainer.createNewAgent("defender", "DefendingAgent", null);
            agent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
