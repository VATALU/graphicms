import graphql.schema.GraphQLObjectType;
import io.vertx.core.json.Json;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.nio.channels.Selector;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MongoTest {
    public static void main(String[] args) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String password = "qqq12345";
        md.update(password.getBytes());
        System.out.println(new BigInteger(1, md.digest()).toString(16));
    }
}