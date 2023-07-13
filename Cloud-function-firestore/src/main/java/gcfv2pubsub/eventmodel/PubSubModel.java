package gcfv2pubsub.eventmodel;

public class PubSubModel {
    private PubsubMessage message;

    public PubsubMessage getMessage() {
        return message;
    }

    public class PubsubMessage {
        private int carId;
        private String carModel;
        private String brand;
        private int year;
        private String color;
        private double mileage;
        private double price;
        private String location;

        private  double priceInRupees;
        private double mileageInKmpl;

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public double getMileage() {
            return mileage;
        }

        public void setMileage(double mileage) {
            this.mileage = mileage;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public double getPriceInRupees() {
            return priceInRupees;
        }

        public void setPriceInRupees(double priceInRupees) {
            this.priceInRupees = priceInRupees;
        }

        public double getMileageInKmpl() {
            return mileageInKmpl;
        }

        public void setMileageInKmpl(double mileageInKmpl) {
            this.mileageInKmpl = mileageInKmpl;
        }
    }
}