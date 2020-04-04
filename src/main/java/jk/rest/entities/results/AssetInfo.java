package jk.rest.entities.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jk.rest.entities.AssetItem;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetInfo {

    private Map<String, AssetItem> result;
}
