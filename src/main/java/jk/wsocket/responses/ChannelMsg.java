package jk.wsocket.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelMsg {

    private int channelId;
    private String channelName;
    private String ticker;
    private String event;
    private String pair;
    private String status;

}
