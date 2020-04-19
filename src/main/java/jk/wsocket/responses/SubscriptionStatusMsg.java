package jk.wsocket.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionStatusMsg {

    private int channelID;
    private String channelName;
    private String ticker;
    private String event;
    private String pair;
    private String status;
    private Map<String, Object> subscription;
}
