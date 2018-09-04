import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Genetics{
    //Podesavanje ova 2 parametra daje razlicite rezultate
    private int velicinaPopulacije = 500;
    private double stopaMutacije=0.05;

    //Pratimo broj trenutne generacije
    private int generacija = 0;

    private int brojGeneracija = 0;

    private Double minFitness = Double.MAX_VALUE;
    private Double maxFitness = 0.0;

    private Warehouse pocetniGrad;
    private Warehouse krajnjiGrad;

    private Double najkracaDistanca= Double.MAX_VALUE; //distanca najboljeg resenja

    private ArrayList<Integer> najboljiPut = new ArrayList<>(); //ovde se cuva najbolje resenje

    private ArrayList<ArrayList<Integer>> populacija = new ArrayList();
    private ArrayList<Integer> put = new ArrayList<>();
    private ArrayList<Double> fitness = new ArrayList<>();
    private ArrayList<Warehouse> warehouses = new ArrayList<>();

    public Genetics(ArrayList<Warehouse> warehouses , Warehouse pocetniGrad, Warehouse krajnjiGrad, int brojGeneracija, int velicinaPopulacije, double stopaMutacije  ) {
        this.velicinaPopulacije = velicinaPopulacije;
        this.stopaMutacije = stopaMutacije;
        this.brojGeneracija = brojGeneracija;
        this.pocetniGrad = pocetniGrad;
        this.krajnjiGrad = krajnjiGrad;
        this.warehouses = warehouses;
        for (Warehouse w : warehouses) {
            System.out.println(w.getId());
        }

    }

    public Genetics(ArrayList<Warehouse> warehouses, Warehouse pocetni, int brojGeneracija, int velicinaPopulacije, double stopaMutacije) {
        this.warehouses = warehouses;
        this.pocetniGrad = pocetni;
        this.velicinaPopulacije = velicinaPopulacije;
        this.stopaMutacije = stopaMutacije;
        this.brojGeneracija = brojGeneracija;

        for (Warehouse w : warehouses) {
            System.out.println(w.getId());
        }

    }

    public Optimal evolucija(){

        //Dodavanje pocetnih vrednosti
        for (int i = 0; i < warehouses.size(); i++) {
            put.add(i);
        }

        //Generisanje prve generacije !
        for (int i = 0; i < velicinaPopulacije; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.addAll(put);
            Collections.shuffle(temp);
            populacija.add(temp);
        }


        //Proces evolucije
        for (int i = 0; i < brojGeneracija; i++) {
            calculatFitenss();
            normalizeFitness();
            System.out.println("Generacija " + generacija + " najkraciput: " + najkracaDistanca);
            sledecaGeneracija();
        }

        System.out.println(najkracaDistanca);
        System.out.println(pocetniGrad.getCity() + " "+pocetniGrad.getLatitude()+" "+ pocetniGrad.getLongitude());


        ArrayList<Warehouse> optimalniPut = new ArrayList<>();
        optimalniPut.add(pocetniGrad);
        for (Integer i : najboljiPut) {
            optimalniPut.add(warehouses.get(i));
        }
        if (krajnjiGrad != null) {
            optimalniPut.add(krajnjiGrad);
        }

        return new Optimal(optimalniPut, najkracaDistanca);
    }

    private void calculatFitenss(){


        //Drugacije racunamo fitness ako imamo i pocetni i krajnji grad
        if(krajnjiGrad!=null){
            //Imamo pocetnu i krajnju tacku
            for (int i = 0; i < velicinaPopulacije; i++) {
                double d = sumaDistanciPut(warehouses, populacija.get(i));

                double fit = (Math.pow(d, 8) + 1);

                if (d < najkracaDistanca) {
                    najkracaDistanca = d;
                    najboljiPut = populacija.get(i);
                }

                //Optimizacija normalizacije
            /*
            * Ideja:
            * Ako sad sacuvamo min fitness i max fitness
            * ne treba nam Collections.min i Collections.max
            * jer su to skuplje metode
            * */
                if (fit < minFitness) {
                    minFitness = fit;
                }
                if ( maxFitness < fit) {
                    maxFitness = fit;
                }

                fitness.add(i, fit);
            }
        }else {
            //Imamo samo pocetnu tacku
            for (int i = 0; i < velicinaPopulacije; i++) {
                double d = sumaDistanci(warehouses, populacija.get(i));

                double fit = (Math.pow(d, 8) + 1);

                if (d < najkracaDistanca) {
                    najkracaDistanca = d;
                    najboljiPut = populacija.get(i);
                }

                //Optimizacija normalizacije
            /*
            * Ideja:
            * Ako sad sacuvamo min fitness i max fitness
            * ne treba nam Collections.min i Collections.max
            * jer su to skuplje metode
            * */
                if (fit < minFitness) {
                    minFitness = fit;
                }
                if ( maxFitness < fit) {
                    maxFitness = fit;
                }

                fitness.add(i, fit);
            }
        }


    }

    private void sledecaGeneracija() {
        /*
        * U ovoj funkciji se priprema nova generaicija tj. generise se nova populacija.
        * Takodje ovde se obicno i resetuju globalne vrednosti kojima je reset potreban.
        * Kod nas je to fitness lista, minFitness, maxFitness
        *
        * Nova generacije se generise tako sto
        * Biramo 2 roditelja iz populacija (obicno su to jedinke sa skoro najvecim fitness vrednostima)
        * i iz njih pravimo novo dete koje ce bitu smesteno u novoj generaciji
        *
        * Crossover funkcija ti je bukvalno parenje :P
        * Mutacija je nesto drugo, objasnio sam u samoj metodi dole.
        *
        * */

        minFitness = Double.MAX_VALUE;
        maxFitness = 0.0;

        ArrayList<ArrayList<Integer>> newPopulation= new ArrayList();
        for (int i = 0; i < velicinaPopulacije; i++) {
            ArrayList<Integer> deteA = izaberiJednog(populacija, fitness);
            ArrayList<Integer> deteB = izaberiJednog(populacija, fitness);
            ArrayList<Integer> novoDete = crossOver(deteA, deteB);
            novoDete = mutate(novoDete,stopaMutacije);
            newPopulation.add(novoDete);
        }
        populacija.clear();
        populacija.addAll(newPopulation);
        fitness = new ArrayList<>();
        generacija++;
    }

    private ArrayList<Integer> crossOver(ArrayList<Integer> deteA, ArrayList<Integer> deteB) {
        Random r = new Random();
        int start = r.nextInt(deteA.size())+1;
        double end = deteA.size();
        double random = new Random().nextDouble();
        double result = start + (random * (end - start));

        //Kriterijum po kome se radi crossover
        /*
        * ako imamo deteA [1,2,3,4,5,6] i deteB [6,5,4,3,2,1]
        * priroda problema je takva da u novom detetu elementi ne mogu da se ponavljaju
        *
        * Korak1: bira se random vrednost za promenljivu start i onda se
        * bira vrednost u intervalu od start i deteA.size()
        * Kad dobijemo vrednosti start i end onda uzimamo podlistu detetaA od
        * pozicije start do pozicije end i dodajemo je novom detetu.
        * U prvom koraku se uzimaju geni iz detetaA!
        *
        * Korak2: prolazimo kroz listu deteB i dodajemo one elemente koji se ne
        * nalaze u novom Detetu
        *
        * Ovaj kriterijum ti garantuje da ce novoDete uvek da ima isti broj gena kao
        * njegovi roditelji.
        *
        * */

        ArrayList<Integer> novoDete = new ArrayList<>();

        //Korak1
        for (int i = start; i < result; i++) {
            novoDete.add(deteA.get(i));
        }

        //Korak2
        for (Integer i : deteB) {
            if (!novoDete.contains(i)) {
                novoDete.add(i);
            }
        }

        return novoDete;
    }

    private ArrayList<Integer> mutate(ArrayList<Integer> dete, double stopaMutacije) {
        Random r = new Random();

        /*
        * Mutacija
        *
        * Mutacija ti je najbitniji deo genetickog/evolucionarnog algoritma
        * da nemas mutaciju evolucija ne bi radila tj. u jednom trenutku (jednoj generaciji)
        * sve jedinke iz populacije bi imale iste gene
        *
        * stopaMutacije je procenat za koliko dece se pretpostavlja da ce biti mutirani-malo izmenjeni
        *
        *
        * Primer1:
        * Imas populaciju od 100 jedinki
        * i imas stopuMutacije 0.01
        * Stopa mutacije od 0.01 u ovom slucaju znaci da ce jedna u 100 jedinki biti mutirana
        *
        * Primer2:
        * populacija = 100
        * stopaMutacije = 0.5
        * Svaka druga jedinka ce biti mutirana
        *
        *
        * Metoda mutacije:
        *
        * Ideja mutacije je da se swapuju dva susedna gena u jedinki
        *
        * Korak1 - bira se random realan broj od 0 do 1 (ne ukljucujuci 1)
        * Ukoliko je broj manji od stopeMutacije desava se mutacija
        * Korak2 - biramo random gen iz jedinke i cuvamo ga u indexA
        * Korak3 - indexB racunamo tako sto dodamo jedan (uzimamo prvog suseda)
        * Ukoliko smo presli preko granice uzimamo prvog prethodnog ili ti levog suseda od indexA
        *
        * Primer:
        * geni [1,2,3,4,5,6]
        * ako je indexA = 3
        * onda je indexB = 4
        *
        * Primer2:
        * geni [1,2,3,4,5,6]
        * ako je indexA = 5
        * onda je indexB = 4
        *
        * */


        for (int i = 0; i < warehouses.size(); i++) {
            if(r.nextDouble() < stopaMutacije){
                int indexA = r.nextInt(dete.size());
                int indexB = indexA + 1;
                if(indexB>=warehouses.size()){
                    indexB = indexA - 1;
                }
                Collections.swap(dete, indexA, indexB);
            }
        }

        ArrayList<Integer> a = new ArrayList<>();
        a.addAll(dete);
        return a;
    }

    private ArrayList<Integer> izaberiJednog(ArrayList<ArrayList<Integer>> populacija, ArrayList<Double> verovatnoca) {

        /*
        * U ovoj metodi se bira jedan kandidat koji ce ucestvovati u crossoveru (parenju)
        *
        *  - > Ideja ove metode je da se izabere jedinka sa sto vecim fitnessom tj. sa sto boljim trenutnim putem,
        * a njegovom algoritmu to se radi na osnovu verovatnoce <--
        *
        *  Metoda koja se koristi je ista kao u klipu i iskreno nisam je bas lepo skontao
        *  Lik je napravio poseban klip u kome objasnjava ovu metodu tako da cu da ti ostavim link,
        *  a mozemo i zajedno da probamo da je skontamo.
        * */

        int index = 0;
        double r =Math.random();
        while (r > 0) {
            r = r - verovatnoca.get(index);
            index++;
        }
        index--;

        ArrayList<Integer> temp = new ArrayList<>();
        temp.addAll(populacija.get(index));
        return temp;
    }

    private void normalizeFitness(){
        /*
        * U ovoj metodi normalizujemo fitness vrednosti tj.
        * mapiramo ih u vrednosti izmedju 0 i 1 kako bi ih lakse kasnije obradjivali
        *
        * Ostavljam ti link obrazca koji sam koristio ovde
        *
        * Ja sam malo optimizovao
        * Pronalazim min i max fitness u calculateFitness funkciji
        * da bi ovde izbegli Collections.min i Collecitons.max
        *
        * */

//        double min = Collections.min(fitness);
//        double max = Collections.max(fitness);


        double min = minFitness;
        double max = maxFitness;

        for (int i = 0; i < fitness.size(); i++) {
            double x = fitness.get(i);
            double normalized = (x - min) / (max - min);
            fitness.set(i, normalized);
        }

        //Resetujemo vrednosti
        minFitness = Double.MAX_VALUE;
        maxFitness = 0.0;

    }


    private double sumaDistanciPut(ArrayList<Warehouse> points, ArrayList<Integer> dete){
        //Ova metoda racuna distancu kada imamo i pocetni i krajnji put

        double sum=0;
        sum += pocetniGrad.distance(points.get(dete.get(0)));
        sum += krajnjiGrad.distance(points.get(dete.get(dete.size()-1)));

        for (int i = 0; i < dete.size()-1; i++) {
            int cityAIndex = dete.get(i);
            Warehouse cityA = points.get(cityAIndex);
            int cityBIndex = dete.get(i + 1);
            Warehouse cityB = points.get(cityBIndex);
            double distanca = cityA.distance(cityB);
            sum += distanca;
        }
        return sum;

    }

    private double sumaDistanci(ArrayList<Warehouse> points, ArrayList<Integer> dete){
        double sum=0;

        /*
        * Kao sto i samo ime funkcije kaze
        * ovde se sumiraju sve distance
        *
        * Na pocetuku dodajemo distancu od pocetnog grada i prvog grada iz jedinke,
        * a zatim racunamo ostalo.
        * */

        sum += pocetniGrad.distance(points.get(dete.get(0)));
        for (int i = 0; i < dete.size()-1; i++) {
            int cityAIndex = dete.get(i);
            Warehouse cityA = points.get(cityAIndex);
            int cityBIndex = dete.get(i + 1);
            Warehouse cityB = points.get(cityBIndex);
            double distanca = cityA.distance(cityB);
            sum += distanca;
        }
        return sum;

    }
}
