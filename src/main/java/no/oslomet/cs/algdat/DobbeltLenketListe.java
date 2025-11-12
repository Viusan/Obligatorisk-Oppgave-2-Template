package no.oslomet.cs.algdat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public class DobbeltLenketListe<T> implements Liste<T> {
    // Innebygd (Trenger ikke endres)

    /**
     * Node class
     *
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi; this.forrige = forrige; this.neste = neste;
        }
        private Node(T verdi) {this(verdi, null, null);}
    }

    private Node<T> hode;
    private Node<T> hale;
    private int antall;
    private int endringer;

    public void fraTilKontroll(int fra, int til) {
        if (fra < 0) throw new IndexOutOfBoundsException("fra("+fra+") er negativ.");
        if (til > antall) throw new IndexOutOfBoundsException("til("+til+") er større enn antall("+antall+")");
        if (fra > til) throw new IllegalArgumentException("fra("+fra+") er større enn til("+til+") - Ulovlig intervall.");
    }

    // Oppgave 0
    public static int gruppeMedlemmer() {
        return 0; // Returner hvor mange som er i gruppa deres
    }

    // Oppgave 1
    public DobbeltLenketListe() {
        //Setter alt til null og 0
        hode = null;
        hale = null;
        antall = 0;
        endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {
        Objects.requireNonNull(a, "Tabellen er null.");//Ser om det er tabell eller om det er null.
        for (int i = 0; i < a.length; i++){
            if(a[i]!= null){//Hopper over null verdier
                Node<T> nyNode = new Node<>(a[i], hale, null);
                //Hvis hode er lik null så eksisterer ikke noden så vi initiater den
                if(hode == null){
                    hode = nyNode;
                }else{
                    //Hvis hode allerede eksisterer så skal hale sin neste være lik den nye noden vår (hale er enden)
                    //Så blir den nye noden vår sin forrige lik hale som er den tidligere noden
                    hale.neste = nyNode;
                }
                hale = nyNode;//Oppdaterer halen til vår nye node siden det er siste node fra nå
                antall++;//Incremente antall
            }
        }
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        if(antall == 0){
            return true;
        }else{
            return false;
        }
    }

    // Oppgave 2
    @Override
    public String toString() {
        if(hode == null) return "[]";
        Node currentNode = hode;
        StringBuilder sb = new StringBuilder("[");

        while(currentNode != null){
            sb.append(currentNode.verdi);
            if(currentNode.neste != null){
                sb.append(", ");
            }
            currentNode = currentNode.neste;
        }
        sb.append("]");
        return sb.toString();
    }

    public String omvendtString() {
        Node currentNode = hale;
        StringBuilder sb = new StringBuilder("[");
        while(currentNode != null){
            sb.append(currentNode.verdi);
            if(currentNode.forrige != null){
                sb.append(", ");
            }
            currentNode = currentNode.forrige;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean leggInn(T verdi) {
       if(verdi == null) throw new NullPointerException("Verdi er null");
       Node nyNode = new Node<>(verdi, hale, null);

       if(hale == null){
           hode = nyNode;
       }else{
           hale.neste = nyNode;
       }

       hale = nyNode;

       endringer++;
       antall++;
       return true;
    }

    // Oppgave 3
    private Node<T> finnNode(int indeks) {
        int midten = antall/2;
        Node returnertNode = hode;
        if(indeks < midten){
            Node currentNode = hode;
            for(int i = 0; i < indeks; i++){
                currentNode = currentNode.neste;
            }
            return currentNode;
        }else{
            Node currentNode = hale;
            for(int i = antall-1; i > indeks; i--){
                currentNode = currentNode.forrige;
            }
            return currentNode;
        }
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        Node<T> returnerVerdi = finnNode(indeks);
        return returnerVerdi.verdi;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        indeksKontroll(indeks, false);
        if(nyverdi == null) throw new NullPointerException("Verdi er null");
        if(antall-1 < indeks) throw new IndexOutOfBoundsException("For høy verdi indeks, out of range");//Her må antall være -1 siden indeks starter på 0, mens antall starter på 1

        Node<T> endreNode = finnNode(indeks);
        T tidligereNodeVerdi = endreNode.verdi;
        endreNode.verdi = nyverdi;
        endringer++;

        return tidligereNodeVerdi;
    }


    public Liste<T> subliste(int fra, int til) {
        fraTilKontroll(fra, til);
        DobbeltLenketListe<T> subListe = new DobbeltLenketListe<>();
        Node<T> peker = finnNode(fra);

        for(int i = fra; fra < til; fra++){
            subListe.leggInn(peker.verdi);
            peker = peker.neste;
        }

        return subListe;
    }

    // Oppgave 4
    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    // Oppgave 5
    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    // Oppgave 6
    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    // Oppgave 7
    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    // Oppgave 8

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean kanFjerne;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;                   // Starter på første i lista
            kanFjerne = false;              // Settes true når next() kalles
            iteratorendringer = endringer;  // Teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
        }

        // Oppgave 9:
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Oppgave 10
    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }
}
