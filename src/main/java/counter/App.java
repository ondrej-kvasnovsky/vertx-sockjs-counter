package counter;

import io.vertx.core.Launcher;

// for easier debugging and running the app from IDE
public class App {

    public static void main(final String[] args) {
        Launcher.executeCommand("run", CounterVerticle.class.getName());
    }
}
