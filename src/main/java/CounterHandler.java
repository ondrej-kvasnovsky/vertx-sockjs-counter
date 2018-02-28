import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

import java.util.Optional;

public class CounterHandler implements Handler<BridgeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CounterHandler.class);
    private final Vertx vertx;

    CounterHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED)
            logger.info("A socket was created");

        if (event.type() == BridgeEventType.SEND)
            clientToServer();

        event.complete(true);
    }

    private void clientToServer() {
        CounterRepository repository = new CounterRepository(vertx.sharedData());
        Optional<Integer> counter = repository.getCounter();
        if (counter.isPresent()) {
            Integer value = counter.get() + 1;
            repository.save(value);
            vertx.eventBus().publish("out", value);
        } else {
            Integer value = 1;
            repository.save(value);
            vertx.eventBus().publish("out", value);
        }
    }
}
