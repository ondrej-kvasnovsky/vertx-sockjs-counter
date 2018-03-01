package counter;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

import java.util.Optional;

public class CounterHandler implements Handler<BridgeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CounterHandler.class);
    private final EventBus eventBus;
    private final CounterRepository repository;

    CounterHandler(EventBus eventBus, CounterRepository repository) {
        this.eventBus = eventBus;
        this.repository = repository;
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
        Optional<Integer> counter = repository.get();
        if (counter.isPresent()) {
            Integer value = counter.get() + 1;
            repository.update(value);
            eventBus.publish("out", value);
        } else {
            Integer value = 1;
            repository.update(value);
            eventBus.publish("out", value);
        }
    }
}
