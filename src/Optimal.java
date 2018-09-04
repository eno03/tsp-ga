import java.util.ArrayList;

class Optimal{
    private ArrayList<Warehouse> putanja = new ArrayList<Warehouse>();
    private double distanca;

    public Optimal(ArrayList<Warehouse> putanja, double distanca) {
        this.putanja = putanja;
        this.distanca = distanca;
    }

    public ArrayList<Warehouse> getPutanja() {
        return putanja;
    }

    public void setPutanja(ArrayList<Warehouse> putanja) {
        this.putanja = putanja;
    }

    public double getDistanca() {
        return distanca;
    }

    public void setDistanca(double distanca) {
        this.distanca = distanca;
    }
}
