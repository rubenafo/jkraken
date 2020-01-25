package co.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class TradeBalanceInfo  {

    private List<String> error;

    private BigDecimal eb;  // equivalent balance (combined balance of all currencies)
    private BigDecimal tb; // trade balance (combined balance of all equity currencies)
    private BigDecimal m; // margin amount of open positions
    private BigDecimal n; // unrealized net profit/loss of open positions
    private BigDecimal c; // cost basis of open positions
    private BigDecimal v; // current floating valuation of open positions
    private BigDecimal e; // equity = trade balance + unrealized net profit/loss
    private BigDecimal mf; // free margin = equity - initial margin (maximum margin available to open new positions)
    private BigDecimal ml; // margin level = (equity / initial margin) * 100

    @JsonCreator
    public TradeBalanceInfo (@JsonProperty("error") List<String> error, @JsonProperty("result") Map<String, BigDecimal> result) {
        this.error = error;
        this.eb = result.get("eb");
        this.tb = result.get("tb");
        this.m = result.get("m");
        this.n = result.get("n");
        this.c = result.get("c");
        this.v = result.get("v");
        this.e = result.get("e");
        this.mf = result.get("mf");
        this.ml = result.get("ml");
    }
}
