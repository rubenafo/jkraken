package jk.wsocket;

import lombok.Data;
import lombok.Value;
import org.springframework.web.socket.WebSocketSession;

@Value
public class KrakenSession {

    private WebSocketSession session;

    public KrakenSession(WebSocketSession session) {
        this.session = session;
    }
}
