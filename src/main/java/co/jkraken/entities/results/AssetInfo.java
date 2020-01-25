package co.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import co.jkraken.entities.AssetItem;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetInfo {

    private Map<String, AssetItem> result;
}
