import io.vertx.core.json.Json;
import org.bson.types.ObjectId;

import java.nio.channels.Selector;

public class MongoTest {
    public static void main(String[] args) {
        System.out.printf(new ObjectId().toHexString());
    }
}