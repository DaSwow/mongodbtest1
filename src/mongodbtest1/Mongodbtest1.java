package mongodbtest1;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import static com.mongodb.client.model.Aggregates.match;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.expr;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Filters.type;
import static com.mongodb.client.model.Projections.fields;
import com.mongodb.client.result.DeleteResult;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
                    .append("estrellas", 5);
            //System.out.println(document1.toJson());

            Document document2 = new Document()
                    .append("nombre", "El Padrino")
                    .append("categorias", new Document().append("Especialidad", "Pizza").append("Nacionalidad", "Italiana"))
                    .append("estrellas", 4);
            //System.out.println(document2.toJson());

            Document document3 = new Document()
                    .append("nombre", "El Mexiantojo")
                    .append("categorias", new Document().append("Especialidad", "Enchiladas").append("Nacionalidad", "Mexicana"))
                    .append("estrellas", 3);
            //System.out.println(document3.toJson());

            restaurantes.insertMany(Arrays.asList(document1, document2, document3));
//
//
//
            System.out.println("Restaurantes con mas de 4 estrellas.");
            AggregateIterable<Document> filtrado1 = restaurantes.aggregate(Arrays.asList(
                    // Java equivalent of the $match stage
                    Aggregates.match(Filters.gt("estrellas", 4))
            ));

            for (Document document : filtrado1) {
                System.out.println(document.toJson());
            }
//
//
//
            System.out.println("Restaurantes con la cagerogia Pizza");

            FindIterable<Document> filtrado2 = restaurantes.find(eq("categorias.Especialidad", "Pizza"));

            for (Document document : filtrado2) {
                System.out.println(document.toJson());
            }
//
//
//
            System.out.println("Restaurantes cuyo nombre incluya Sushi");
            Pattern pattern = Pattern.compile(".*" + Pattern.quote("Sushi") + ".*", Pattern.CASE_INSENSITIVE);
            FindIterable<Document> filtrado3 = restaurantes.find(regex("nombre", pattern));

            for (Document document : filtrado3) {
                System.out.println(document.toJson());
            }
//
//
//
            System.out.println("Agregando una categoria a Sushilito");
            DBObject listItem = new BasicDBObject("categorias.Chefs", "Sanmoto Gorozaemon");
            restaurantes.updateOne(eq("nombre", "Sushilito"), new Document().append("$set", listItem));

            restaurantes = db.getCollection("restaurantes", Document.class);
            FindIterable<Document> filtrado4 = restaurantes.find(regex("nombre", pattern));

            for (Document document : filtrado4) {
                System.out.println(document.toJson());
            }
//
//
//
            System.out.println("Eliminando un restaurante por su identificador");
            BasicDBObject documentoABorrar = new BasicDBObject();
            documentoABorrar.put("nombre", "Sushilito");
            restaurantes.deleteOne(documentoABorrar);

            try (MongoCursor<Document> cursor = restaurantes.find().iterator()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            }
//
//
//     
            System.out.println("Eliminando restaurantes con 3 estrellas o menos.");

            restaurantes.deleteMany(lt("estrellas", 3));
            restaurantes.deleteMany(eq("estrellas", 3));
            
            try (MongoCursor<Document> cursor = restaurantes.find().iterator()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            }

        }
    }

}
