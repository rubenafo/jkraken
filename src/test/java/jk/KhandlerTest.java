package jk;

import jk.wsocket.service.KrakenWsHandler;
import jk.wsocket.service.LocalPropLoaderService;
import lombok.val;
import lombok.var;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class KhandlerTest {

    private KrakenWsHandler khandler;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        val localPropsService = mock(LocalPropLoaderService.class);
        this.khandler = new KrakenWsHandler(localPropsService, "id", "url");
        this.session = mock(WebSocketSession.class);
    }

    @Test
    public void session_receives_subscribed_msg() {
        var input = "{\"channelID\":274,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"XBT/USD\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        assertEquals(1, khandler.getChannels().size());
        val channels = khandler.getChannels();
        val channel = channels.get(0);
        assertEquals(274, channel.getChannelID());
        assertEquals("subscribed", channel.getStatus());
    }

    @Test
    public void session_receives_ticker_data_msg() {
        var input = "{\"channelID\":274,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"XBT/USD\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        var input2 = "[274,{\"a\":[\"6804.10000\",3,\"3.98560000\"],\"b\":[\"6801.00000\",1,\"1.34500000\"],\"c\":[\"6804.10000\",\"0.01440000\"],\"v\":[\"3081.29016909\",\"4852.51773585\"],\"p\":[\"6884.69236\",\"6890.16503\"],\"t\":[9843,16235],\"l\":[\"6789.60000\",\"6789.60000\"],\"h\":[\"6960.00000\",\"6960.00000\"],\"o\":[\"6873.70000\",\"6869.10000\"]},\"ticker\",\"XBT/USD\"]";
        khandler.handleTextMessage(this.session, new TextMessage(input2));
        val tickerData = khandler.getTickerData();
        assertEquals(1, tickerData.rowCount());
    }

    @Test
    public void session_subscribes_channel() {
        var input = "{\"channelID\":246,\"event\":\"subscriptionStatus\",\"pair\":\"XBT/EUR\",\"status\":\"subscribed\",\"subscription\":{\"interval\":1,\"name\":\"ohlc\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        val channels = khandler.getChannels();
        Assert.assertEquals(1, channels.size());
        val channel = channels.get(0);
        assertEquals(246, channel.getChannelID());
        assertEquals("subscriptionStatus", channel.getEvent());
        assertEquals("XBT/EUR", channel.getPair());
        assertEquals("subscribed", channel.getStatus());
    }

    @Test
    public void session_unsubscribes_private_channel() {
        val subscribed = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"reqid\":306811511,\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(subscribed));
        assertTrue(khandler.getChannels().size() == 1);
        val unsubscribed = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"unsubscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(unsubscribed));
        assertTrue(khandler.getChannels().size() == 0);
    }

    @Test
    public void session_understands_ownTrades() {
        var input = "[[{\"TOUN5T-CEN26-B3EKZX\":{\"cost\":\"143.600000000\",\"fee\":\"0.373360000\",\"margin\":\"0.000000000\",\"ordertxid\":\"OJMUKF-EEOIM-ZYL4YM\",\"ordertype\":\"market\",\"pair\":\"XDG/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.002872000\",\"time\":\"1581752407.030054\",\"type\":\"buy\",\"vol\":\"50000.00000000\"}},{\"TEIWPG-DNESO-44YKNB\":{\"cost\":\"297.950000\",\"fee\":\"0.774670\",\"margin\":\"0.000000\",\"ordertxid\":\"OPCCHI-BWYBD-52HXIR\",\"ordertype\":\"market\",\"pair\":\"SC/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.002980\",\"time\":\"1581752054.283192\",\"type\":\"buy\",\"vol\":\"100000.00000000\"}},{\"TU6IBU-HOZJS-2AYVT3\":{\"cost\":\"126.945999\",\"fee\":\"0.330060\",\"margin\":\"0.000000\",\"ordertxid\":\"OC6RLD-PZROM-4CVXIQ\",\"ordertype\":\"market\",\"pair\":\"ADA/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.063473\",\"time\":\"1581536744.772174\",\"type\":\"sell\",\"vol\":\"1999.99999991\"}},{\"TKXJUK-XRHJ6-XY47UO\":{\"cost\":\"128.700000\",\"fee\":\"0.334620\",\"margin\":\"0.000000\",\"ordertxid\":\"OM2YX4-UAUGO-TQYRI7\",\"ordertype\":\"market\",\"pair\":\"SC/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.002340\",\"time\":\"1581536547.314520\",\"type\":\"sell\",\"vol\":\"54999.99999999\"}},{\"TTTC6V-F6ZCN-LOX67X\":{\"cost\":\"0.000000\",\"fee\":\"0.000000\",\"margin\":\"0.000000\",\"ordertxid\":\"OC7EIT-B5VZP-VJUJM4\",\"ordertype\":\"limit\",\"pair\":\"OMG/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"1.130000\",\"time\":\"1581471645.111398\",\"type\":\"sell\",\"vol\":\"0.00000027\"}},{\"TP3YB6-YJC5D-Y2WPCC\":{\"cost\":\"124.101438\",\"fee\":\"0.198562\",\"margin\":\"0.000000\",\"ordertxid\":\"OC7EIT-B5VZP-VJUJM4\",\"ordertype\":\"limit\",\"pair\":\"OMG/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"1.130000\",\"time\":\"1581471645.109222\",\"type\":\"sell\",\"vol\":\"109.82428115\"}},{\"TNAPZE-XONON-M265WQ\":{\"cost\":\"93.858000000\",\"fee\":\"0.244030800\",\"margin\":\"0.000000000\",\"ordertxid\":\"OYLRMC-TXMF3-BVBHYQ\",\"ordertype\":\"market\",\"pair\":\"XDG/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.003128600\",\"time\":\"1581243486.431554\",\"type\":\"buy\",\"vol\":\"30000.00000000\"}},{\"TEW4NU-7KE6P-Z5QMEE\":{\"cost\":\"108.828792\",\"fee\":\"0.282954\",\"margin\":\"0.000000\",\"ordertxid\":\"O5BLP6-O66EO-FJS3AD\",\"ordertype\":\"market\",\"pair\":\"OMG/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.989353\",\"time\":\"1580970903.331898\",\"type\":\"buy\",\"vol\":\"110.00000000\"}},{\"TEQUSQ-J62SQ-WO2J7U\":{\"cost\":\"97.350000\",\"fee\":\"0.253110\",\"margin\":\"0.000000\",\"ordertxid\":\"O4DIEX-JPZJV-5STI3B\",\"ordertype\":\"market\",\"pair\":\"SC/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.001770\",\"time\":\"1580651306.815318\",\"type\":\"buy\",\"vol\":\"55000.00000000\"}},{\"TDT2V6-QDHTL-YBMFR3\":{\"cost\":\"104.198415\",\"fee\":\"0.270916\",\"margin\":\"0.000000\",\"ordertxid\":\"OS6TZQ-CAHL6-VCVSYM\",\"ordertype\":\"market\",\"pair\":\"ADA/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.052099\",\"time\":\"1580649429.684946\",\"type\":\"buy\",\"vol\":\"2000.00000000\"}},{\"T63C5B-FXNHA-VSHNNA\":{\"cost\":\"322.84740\",\"fee\":\"0.83940\",\"margin\":\"0.00000\",\"ordertxid\":\"OB2LMA-5CADW-NX4MDS\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"6470.80000\",\"time\":\"1527329180.148097\",\"type\":\"buy\",\"vol\":\"0.04989297\"}},{\"TBHUAO-5G4FH-7PFP2I\":{\"cost\":\"303.25344999\",\"fee\":\"0.78845898\",\"margin\":\"0.00000000\",\"ordertxid\":\"OSFJ37-NE6LZ-U6PMUQ\",\"ordertype\":\"market\",\"pair\":\"XRP/EUR\",\"postxid\":\"TKH2SE-M7IF5-CFI7LT\",\"price\":\"0.60050188\",\"time\":\"1517992516.608996\",\"type\":\"sell\",\"vol\":\"504.99999999\"}},{\"TZFEE5-RGGF2-MLR2KF\":{\"cost\":\"833.73024000\",\"fee\":\"2.16769862\",\"margin\":\"0.00000000\",\"ordertxid\":\"ONOTYM-XWWIP-GAH6LM\",\"ordertype\":\"market\",\"pair\":\"XRP/EUR\",\"postxid\":null,\"price\":\"2.68800000\",\"time\":\"1515067368.065937\",\"type\":\"buy\",\"vol\":\"310.16750000\"}},{\"TIZGG6-MOWRZ-Z5VDV2\":{\"cost\":\"105.92109375\",\"fee\":\"0.27539484\",\"margin\":\"0.00000000\",\"ordertxid\":\"ONOTYM-XWWIP-GAH6LM\",\"ordertype\":\"market\",\"pair\":\"XRP/EUR\",\"postxid\":null,\"price\":\"2.68750000\",\"time\":\"1515067368.052742\",\"type\":\"buy\",\"vol\":\"39.41250000\"}},{\"TT3K6F-TYPL3-SYEINC\":{\"cost\":\"268.48800000\",\"fee\":\"0.69806880\",\"margin\":\"0.00000000\",\"ordertxid\":\"ONOTYM-XWWIP-GAH6LM\",\"ordertype\":\"market\",\"pair\":\"XRP/EUR\",\"postxid\":null,\"price\":\"2.68488000\",\"time\":\"1515067368.039510\",\"type\":\"buy\",\"vol\":\"100.00000000\"}},{\"TSHRHR-MVBOF-YNFEGN\":{\"cost\":\"148.52670840\",\"fee\":\"0.38616944\",\"margin\":\"0.00000000\",\"ordertxid\":\"ONOTYM-XWWIP-GAH6LM\",\"ordertype\":\"market\",\"pair\":\"XRP/EUR\",\"postxid\":null,\"price\":\"2.68002000\",\"time\":\"1515067368.006290\",\"type\":\"buy\",\"vol\":\"55.42000000\"}},{\"TFJOW2-EHBJF-4HYEGU\":{\"cost\":\"851.13121\",\"fee\":\"2.21294\",\"margin\":\"0.00000\",\"ordertxid\":\"O2SNAD-RA62A-RJCNUT\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"12199.10000\",\"time\":\"1515066842.334155\",\"type\":\"sell\",\"vol\":\"0.06977000\"}},{\"TGIUXZ-FLPYO-D5HER2\":{\"cost\":\"309.86476\",\"fee\":\"0.80565\",\"margin\":\"0.00000\",\"ordertxid\":\"O2SNAD-RA62A-RJCNUT\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"12199.40000\",\"time\":\"1515066842.314294\",\"type\":\"sell\",\"vol\":\"0.02540000\"}},{\"T3HOMT-IQSWC-PEII3A\":{\"cost\":\"130.53465\",\"fee\":\"0.33939\",\"margin\":\"0.00000\",\"ordertxid\":\"O2SNAD-RA62A-RJCNUT\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"12199.50000\",\"time\":\"1515066842.295241\",\"type\":\"sell\",\"vol\":\"0.01070000\"}},{\"T6JBTK-PA4TE-4LDNUT\":{\"cost\":\"86.98457\",\"fee\":\"0.22616\",\"margin\":\"0.00000\",\"ordertxid\":\"O2SNAD-RA62A-RJCNUT\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"12199.80000\",\"time\":\"1515066842.269527\",\"type\":\"sell\",\"vol\":\"0.00713000\"}},{\"TNTVMP-5YD5Y-VQZNUL\":{\"cost\":\"1257.57700\",\"fee\":\"3.26970\",\"margin\":\"0.00000\",\"ordertxid\":\"OM62YM-4PTZ5-XRVUMB\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"11129.00000\",\"time\":\"1513963424.654121\",\"type\":\"buy\",\"vol\":\"0.11300000\"}},{\"TJ4WRW-EUEK7-2XOSH4\":{\"cost\":\"1259.00800\",\"fee\":\"3.27342\",\"margin\":\"0.00000\",\"ordertxid\":\"OMAOVK-JTTJB-RLW336\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"9836.00000\",\"time\":\"1513953058.993878\",\"type\":\"sell\",\"vol\":\"0.12800000\"}},{\"T4LLBL-2XBRA-HOYJX3\":{\"cost\":\"1484.54400\",\"fee\":\"3.85981\",\"margin\":\"0.00000\",\"ordertxid\":\"O72RC4-QE6TO-BYQYX7\",\"ordertype\":\"market\",\"pair\":\"XBT/EUR\",\"postxid\":null,\"price\":\"11598.00000\",\"time\":\"1512602465.162920\",\"type\":\"buy\",\"vol\":\"0.12800000\"}}],\"ownTrades\"]\n";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        val ownTrades = khandler.getOwnTrades();
        assertTrue(ownTrades.keySet().size() == 23);
    }

    @Test
    public void session_updates_order() throws IOException {
        val receiveOrders = Files.readAllBytes(Paths.get("src", "test", "java", "resources", "openOrders.json"));
        khandler.handleTextMessage(this.session, new TextMessage(receiveOrders));
        val orderUpdate = Files.readAllBytes(Paths.get("src", "test", "java", "resources", "orderUpdate.json"));
        khandler.handleTextMessage(this.session, new TextMessage(orderUpdate));
        assertEquals("closed", khandler.getOpenOrders().get("OGTT3Y-C6I3P-XRI6HX").getStatus());
    }

    @Test
    public void session_stores_reqId() {
        val subscriptionResponse = "{\"channelID\":1204,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"ADA/ETH\",\"reqid\":1278173371,\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(subscriptionResponse));
        assertTrue(khandler.getErrorMessages().containsKey(1278173371));
        assertTrue(khandler.getErrorMessages().get(1278173371).equals(""));
    }

    @Test
    public void session_handles_already_subscribed() {
        val alreadySubscribedResponse = "{\"errorMessage\":\"Already subscribed\",\"event\":\"subscriptionStatus\",\"reqid\":1998689890,\"status\":\"error\",\"subscription\":{\"name\":\"ownTrades\"}}";
        // TODO
    }
}
