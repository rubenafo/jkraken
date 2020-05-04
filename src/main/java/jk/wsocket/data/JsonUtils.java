package jk.wsocket.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import tech.tablesaw.api.Table;

public class JsonUtils {

    public static String toJson (Table table) {
        val json = new ObjectMapper();
        val jsonList = json.createArrayNode();
        val cols = table.columns();
        for (int i = 0; i < table.rowCount(); i++) {
            val item = new ObjectMapper().createObjectNode();
            val row = table.row(i);
            cols.forEach(c -> {
                val obj = row.getObject(c.name());
                item.put(c.name(), row.getObject(c.name()).toString());
            });
            jsonList.add(item);
        }
        return jsonList.toString();
    }
}
