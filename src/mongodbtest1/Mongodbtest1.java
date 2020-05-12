package mongodbtest1;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author carls
 */
public class Mongodbtest1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //MongoCredential credential=  MongoCredential.createCredential("user","test","password".toCharArray());
        //MongoClient mongo = MongoClients.create("mongodb://localhost:27017/bddmongo");
        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017/bddmongo");
        //ServerAddress address=new ServerAddress("");
        //MongoDatabase database = mongo.getDatabase("test");
        // ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb://localhost:27017/admin"));
        // CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        // CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        //         pojoCodecRegistry);
        // MongoClientSettings clientSettings = MongoClientSettings.builder()
        //         .applyConnectionString(connectionString)
        //         .codecRegistry(codecRegistry)
        //        .build();
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/bddmongo")) {
            MongoDatabase db = mongoClient.getDatabase("bddmongo");

            MongoCollection<Document> restaurantes = db.getCollection("restaurantes", Document.class);

            Document document1 = new Document()
                    .append("nombre", "Sushilito")
                    .append("categorias", new Document().append("Especialidad", "Sushi").append("Nacionalidad", "Japonesa"))
                    .append("estrellas", 3);
            System.out.println(document1.toJson());

            Document document2 = new Document()
                    .append("nombre", "El Padrino")
                    .append("categorias", new Document().append("Especialidad", "Pizza").append("Nacionalidad", "Italiana"))
                    .append("estrellas", 5);
            System.out.println(document2.toJson());

            Document document3 = new Document()
                    .append("nombre", "El Mexiantojo")
                    .append("categorias", new Document().append("Especialidad", "Enchiladas").append("Nacionalidad", "Mexicana"))
                    .append("estrellas", 4);
            System.out.println(document3.toJson());

            restaurantes.insertMany(Arrays.asList(document1, document2, document3));

            AggregateIterable<Document> documents = restaurantes.aggregate(Arrays.asList(
                    // Java equivalent of the $match stage
                    Aggregates.match(Filters.gt("estrellas", 4))
            ));

            for (Document document : documents) {
                System.out.println(document.toJson());
            }

        }

        //  MongoCollection<Document> collection = database.getCollection("inventory");
//        Document document1 = new Document()
//                .append("item", "canvas")
//                .append("qty", 100)
//                .append("tags", Arrays.asList("cotton"))
//                .append("size", new Document().append("h", 28.0).append("w", 35.5).append("uom", "cm"));
//        System.out.println(document1.toJson());
//
//        Document document2 = new Document()
//                .append("item", "journal")
//                .append("qty", 25)
//                .append("tags", Arrays.asList("blank", "red"))
//                .append("size", new Document().append("h", 14.0).append("w", 21.0).append("uom", "cm"));
//
//        Document document3 = new Document()
//                .append("item", "mousepad")
//                .append("qty", 25)
//                .append("tags", Arrays.asList("get", "blue"))
//                .append("size", new Document().append("h", 19.0).append("w", 22.85).append("uom", "in"));
//
////        collection.insertMany(Arrays.asList(document1, document2, document3));
//      //  System.out.println(collection.countDocuments());
//
////        for (Document doc : collection.find()) {
////            System.out.println(doc.toJson());
////        }
////        FindIterable<Document> documents=collection.find();
//        Filters.or(
//                Filters.eq("status", "A"),
//                Filters.and(
//                        Filters.gt("qty", 30),
//                        Filters.eq("canvas", "journal"))
//        );
//
    }

}
