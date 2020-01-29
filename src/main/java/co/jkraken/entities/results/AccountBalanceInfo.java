package co.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class AccountBalanceInfo {

    private List<String> error;

    @JsonProperty("result")
    private Map<String, BigDecimal> balances;
}
