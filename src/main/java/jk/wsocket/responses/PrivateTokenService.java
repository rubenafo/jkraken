package jk.wsocket.responses;

import jk.rest.engine.ApiSign;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PrivateTokenService {

    private Logger LOGGER = LoggerFactory.getLogger(PrivateTokenService.class);

    private String token;

    @PostConstruct
    public void refreshToken () {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            val newToken = ApiSign.getWebSocketAuthToken();
            synchronized (this) {
                this.token = newToken;
            }
            LOGGER.debug("Setting token = {}" + this.token);
        }, 0, 890, TimeUnit.SECONDS);
    }

    public synchronized String getToken () {
        return this.token;
    }
}
