package jk.wsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jk.data.SessionData;
import jk.wsocket.responses.ChannelMsg;
import jk.wsocket.responses.TickerData;
import lombok.Value;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

@Value
public class KrakenSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenSession.class);

    private WebSocketSession session;
    private ObjectMapper jsonMapper;
    private SessionData sessionData;

    public KrakenSession(WebSocketSession session) {
        this.session = session;
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        this.sessionData = new SessionData();
    }

    public void handleMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object obj = mapper.readValue(message, Object.class);
            if (obj instanceof Map) {
                val channelAck = this.jsonMapper.readValue(message, ChannelMsg.class);
            } else if (obj instanceof List) {
                val jsonList = (List) obj;
                int channelId = (int) jsonList.get(0);
                val channelName = (String) jsonList.get(2);
                val pair = (String) jsonList.get(3);
                switch (channelName) {
                    case "ticker":
                        val tickerData = TickerData.fromMap(channelId, channelName, pair, (Map) jsonList.get(1));
                        this.sessionData.append(tickerData);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
