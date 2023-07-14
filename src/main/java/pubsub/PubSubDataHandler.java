package pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.pubsub.v1.MessagePublishedData;
import com.google.events.cloud.pubsub.v1.Message;
import pubsub.eventmodel.CarModel;
import io.cloudevents.CloudEvent;
import java.util.Base64;
import java.util.logging.Logger;

public class PubSubDataHandler implements CloudEventsFunction {
  private static final Logger logger = Logger.getLogger(PubSubDataHandler.class.getName());

  /**
   * The conversion rate for prices
   * from a foreign currency to the local currency.
   */
  private static final Double PRICE_CONVERSION_RATE_ = 82.11;

  /**
   * The conversion rate for mileage
   * from miles to kilometers.
   */
  private static final Double MILEAGE_CONVERSION_RATE_ = 1.609344;

  /**
   * The Firestore instance for
   * interacting with the Firestore database.
   */
  private static Firestore firestore;

  /**
   * Constructor for the PubSubDataHandler class.
   * Initializes the Firestore instance.
   */
  public PubSubDataHandler() {
    try {
      firestore = FirestoreOptions
              .getDefaultInstance().getService();
    } catch (ApiException e) {
      logger.severe("Firestore initialization error: "
              + e.getMessage());
    }
  }

  /**
   * Processes the CloudEvent containing Pub/Sub data.
   *
   * @param event The incoming CloudEvent.
   * @throws JsonProcessingException If there is an
   * error parsing the JSON data.
   */
  @Override
  public void accept(final CloudEvent event)
          throws JsonProcessingException {
    String cloudEventData = new String(event
            .getData().toBytes());

    ObjectMapper objectMapper = new ObjectMapper();
    MessagePublishedData data = objectMapper.readValue(
            cloudEventData, MessagePublishedData.class);
    logger.info(data.toString());
    Message message = data.getMessage();
    String encodedData = message.getData();
    String decodedData = new String(Base64
            .getDecoder().decode(encodedData));

    logger.info("Pub/Sub message: " + decodedData);
    CarModel pubSubModel = objectMapper
            .readValue(decodedData, CarModel.class);

    logger.info(pubSubModel.toString());

    Double priceInRupees = transformPrice(
            pubSubModel.getMessage().getPrice());
    Double mileageInKmpl = transformMileage(
            pubSubModel.getMessage().getMileage());

    pubSubModel.getMessage().setPriceInRupees(priceInRupees);
    pubSubModel.getMessage().setMileageInKmpl(mileageInKmpl);

    logger.info("Mileage in kmpl: " +
            pubSubModel.getMessage().getMileageInKmpl());
    logger.info("Price in rupees: " +
            pubSubModel.getMessage().getPriceInRupees());

    saveDataToFirestore(pubSubModel);
  }

  /**
   * Converts the price from dollars to rupees.
   *
   * @param priceInDollars The price in dollars.
   * @return The price in rupees.
   */
  private double transformPrice(final double priceInDollars) {
    return priceInDollars * PRICE_CONVERSION_RATE_;
  }

  /**
   * Converts the mileage from miles to kilometers per liter.
   *
   * @param mileageInMiles The mileage in miles.
   * @return The mileage in kilometers per liter.
   */
  private double transformMileage(
          final double mileageInMiles) {
    return mileageInMiles * MILEAGE_CONVERSION_RATE_;
  }

  /**
   * Saves the data from the provided
   * CarModel object to Firestore.
   *
   * @param pubSubModel The CarModel object
   *                   containing the data to be saved.
   */
  void saveDataToFirestore(
          final CarModel pubSubModel) {
    String documentId = String
            .valueOf(pubSubModel.getMessage().getCarId());
    logger.info("document id : " + documentId);
    DocumentReference destinationDocRef =
            firestore.collection("vehicle")
                    .document(documentId);
    destinationDocRef.set(pubSubModel.getMessage());
  }
}

