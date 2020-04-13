package jk.wsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jk.data.SessionData;
import jk.wsocket.responses.ChannelAck;
import lombok.Value;
import lombok.val;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

@Value
public class KrakenSession {

    private WebSocketSession session;
    private ObjectMapper jsonMapper;
    private SessionData sessionData;

    public KrakenSession(WebSocketSession session) {
        this.session = session;
        this.jsonMapper = new ObjectMapper();
        this.sessionData = new SessionData();
    }

    public void handleMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object obj = mapper.readValue(message, Object.class);
            if (obj instanceof Map) {
                val channelAck = this.jsonMapper.readValue(message, ChannelAck.class);
            } else if (obj instanceof List) {
                val jsonList = (List) obj;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
