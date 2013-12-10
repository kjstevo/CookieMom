import de.greenrobot.daogenerator.*;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/27/13
 * Time: 6:00 PM
 */
public class CookieMomGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(14, "net.kmaster.cookiemom.dao");
        schema.enableKeepSectionsByDefault();

        addScoutOrder(schema);
        addPersonal(schema);
        new DaoGenerator().generateAll(schema, "C:\\Users\\Maddy\\Downloads\\CookieMomMain\\CookieMom-AndroidKickstartr\\CookieMom\\src-gen");
    }


    private static void addScoutOrder(Schema schema) {
        Entity scout = schema.addEntity("Scout");
        scout.addIdProperty();
        scout.addStringProperty("scoutName").notNull();
        scout.setHasKeepSections(true);

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();


//        Entity cookieFlavor=schema.addEntity("CookieFlavor");
//        cookieFlavor.addIdProperty();
//        cookieFlavor.addStringProperty("cookieFlavorName").notNull();
//         cookieFlavor.addIntProperty("cookieColor");
//        cookieFlavor.addIntProperty("resourceId");
        Property orderDate = order.addDateProperty("orderDate").getProperty();
        Property orderScoutId = order.addLongProperty("orderScoutId").notNull().getProperty();
        order.addStringProperty("orderCookieType");
        order.addBooleanProperty("orderedFromCupboard");
        order.addIntProperty("orderedBoxes");


        order.addBooleanProperty("pickedUpFromCupboard");
//        order.addToOne(cookieFlavor,orderCookieFlavorId);
        order.addToOne(scout, orderScoutId);


        Entity booth = schema.addEntity("Booth");
        booth.addIdProperty();
        booth.addStringProperty("boothLocation");
        booth.addStringProperty("boothAddress");
        booth.addDateProperty("boothDate");
        booth.setHasKeepSections(true);


        Entity cookieTransaction = schema.addEntity("CookieTransactions");
        cookieTransaction.addIdProperty();
        Property transScoutId = cookieTransaction.addLongProperty("transScoutId").getProperty();
        Property transBoothId = cookieTransaction.addLongProperty("transBoothId").getProperty();

        cookieTransaction.addStringProperty("cookieType");
        cookieTransaction.addIntProperty("transBoxes");
        Property transDate = cookieTransaction.addDateProperty("transDate").getProperty();
        cookieTransaction.addDoubleProperty("transCash");

        cookieTransaction.addToOne(scout, transScoutId);
        cookieTransaction.addToOne(booth, transBoothId);
//        cookieTransaction.addToOne(cookieFlavor, transCookieFlavorId);


        Entity boothAssignments = schema.addEntity("BoothAssignments");
        boothAssignments.addIdProperty();
        Property boothAssignScoutId = boothAssignments.addLongProperty("boothAssignScoutId").getProperty();
        Property boothAssignBoothId = boothAssignments.addLongProperty("boothAssignBoothId").getProperty();

        boothAssignments.addToOne(scout, boothAssignScoutId);
        boothAssignments.addToOne(booth, boothAssignBoothId);

        ToMany boothAssignmentsToScouts = scout.addToMany(boothAssignments, boothAssignScoutId);
        boothAssignmentsToScouts.setName("boothsAssigned");


        ToMany boothAssignmentsToBooths = booth.addToMany(boothAssignments, boothAssignBoothId);
        boothAssignmentsToBooths.setName("scoutsAssigned");

        ToMany scoutToTransactions = scout.addToMany(cookieTransaction, transScoutId);
        scoutToTransactions.setName("scoutsCookieTransactions");
        scoutToTransactions.orderAsc(transDate);


        ToMany customerToOrders = scout.addToMany(order, orderScoutId);
        customerToOrders.setName("scoutOrders");
        customerToOrders.orderAsc(orderDate);


    }

    private static void addPersonal(Schema schema) {

    }
}
