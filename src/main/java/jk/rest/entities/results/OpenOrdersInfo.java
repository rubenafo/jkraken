package jk.rest.entities.results;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OpenOrdersInfo {

    private List<String> error;
    private Map<String, Object> result;

}
