
public class Warehouse {

    private int id;
    private String city;
    private String address;
    private float latitude;
    private float longitude;
    private int quantity;
    private boolean visited;

    public Warehouse(int id, String city, String address, float latitude, float longitude, int quantity, boolean visited) {
        
        this.id = id;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.quantity = quantity;
        this.visited = visited;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    double distance(Warehouse get) {
        double theta = this.longitude - get.longitude;
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(get.latitude)) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(get.latitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344; // to kiilometers;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
