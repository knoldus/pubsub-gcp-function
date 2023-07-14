package gcfv2pubsub;

import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.pubsub.v1.MessagePublishedData;
import com.google.events.cloud.pubsub.v1.Message;
import com.google.gson.Gson;
import gcfv2pubsub.eventmodel.PubSubModel;
import io.cloudevents.CloudEvent;
import java.util.Base64;
import java.util.logging.Logger;

public class PubSubFunction implements CloudEventsFunction {
  private static PubSubModel pubSubModel;
  private static final Logger logger = Logger.getLogger(
          PubSubFunction.class.getName());

  private static final double PRICE_CONVERSION_RATE_ = 82.11;
  private static final double MILEAGE_CONVERSION_RATE_ = 1.609344;

  @Override
  public void accept(final CloudEvent event) {
    String cloudEventData = new String(event.getData().toBytes());
    Gson gson = new Gson();
    MessagePublishedData data = gson.fromJson(cloudEventData,
            MessagePublishedData.class);
    Message message = data.getMessage();
    String encodedData = message.getData();
    String decodedData = new String(Base64.getDecoder()
            .decode(encodedData));
    logger.info("Pub/Sub message: " + decodedData);

    PubSubModel pubSubModel = gson.fromJson(decodedData,
            PubSubModel.class);
    double priceInRupees = transformPrice(pubSubModel
            .getMessage().getPrice());
    double mileageInKmpl = transformMileage(pubSubModel
            .getMessage().getMileage());

    pubSubModel.getMessage().setPriceInRupees(priceInRupees);
    pubSubModel.getMessage().setMileageInKmpl(mileageInKmpl);

    logger.info("Mileage in kmpl: " + pubSubModel
            .getMessage().getMileageInKmpl());
    logger.info("Price in rupees: " + pubSubModel
            .getMessage().getPriceInRupees());
  }
  private double transformPrice(final double priceInDollars) {
    double conversionRate = PRICE_CONVERSION_RATE_;
    return priceInDollars * conversionRate;
  }

  private double transformMileage(final double mileageInMiles) {
    double conversionFactor = MILEAGE_CONVERSION_RATE_;
    return mileageInMiles * conversionFactor;
  }
}

