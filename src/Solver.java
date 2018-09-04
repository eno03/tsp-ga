import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nikol
 */
public class Solver {

    private List<Warehouse> warehouses = new ArrayList<>();
    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(ArrayList<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Solver(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }


    public Optimal nadjiPutOd(int idMagacina) {

        Warehouse pocetni = null;
        ArrayList<Warehouse> listaBezPocetnog = new ArrayList<>();

        for (Warehouse w : warehouses) {
            if (w.getId() == idMagacina) {
                pocetni = w;
            }else if(!w.isVisited()){
                listaBezPocetnog.add(w);
            }
        }




        Genetics g = new Genetics(listaBezPocetnog, pocetni, 500, 500, 0.02);
        Optimal o = g.evolucija();
        return o;
    }

    public Optimal najkraciPut(int idPocetni, int idKrajnji) {

        Warehouse pocetni = null;
        Warehouse krajnji = null;
        ArrayList<Warehouse> listaBezPocetnog = new ArrayList<>();

        for (Warehouse w : warehouses) {
            if (w.getId() == idPocetni) {
                pocetni = w;
            }else if(w.getId() == idKrajnji){
                krajnji = w;
            } else if (!w.isVisited()) {
                listaBezPocetnog.add(w);
            }
        }


        Genetics g = new Genetics(listaBezPocetnog, pocetni,krajnji,  500, 500, 0.02);
        Optimal o = g.evolucija();
        return o;

    }

}

