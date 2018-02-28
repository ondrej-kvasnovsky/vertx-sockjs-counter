import io.vertx.core.Launcher;

public class App {

    public static void main(final String[] args) {
        Launcher.executeCommand("run", CounterVerticle.class.getName());
    }
}
