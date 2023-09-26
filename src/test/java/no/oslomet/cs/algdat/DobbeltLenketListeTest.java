package no.oslomet.cs.algdat;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class Oppgave0Test {
    @Test
    void gruppeMedlemmerOppdatert() {
        assertNotEquals(0, DobbeltLenketListe.gruppeMedlemmer(), "Oppdater funksjonen gruppeMedlemmer() så den returnerer antall personer i gruppa!");
    }
}

class Oppgave1Test {
    @Test
    void ingenListe() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        assertEquals(0, liste.antall(), "Ikkenull antall ved ingen liste.");
        assertTrue(liste.tom(), "Ingen liste gir feil svar på liste.tom()");
    }

    @Test
    void nullListe() {
        assertThrows(NullPointerException.class, () -> new DobbeltLenketListe<Integer>(null), "Gir ingen eller gal feilmelding om sender inn nullpeker.");
    }

    @Test
    void tomListe() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[]{});
        assertEquals(0, liste.antall(), "Ikkenull antall ved tom liste.");
        assertTrue(liste.tom(), "Tom liste gir feil svar på liste.tom()");
    }

    @Test
    void ettElementListe() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[]{1});
        assertEquals(1, liste.antall(), "Feil antall på liste med ett element.");
        assertFalse(liste.tom(), "Påstår liste med ett element er tom.");
    }

    @Test
    void flereElementListe() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[]{1, 2});
        assertEquals(2, liste.antall());
        liste = new DobbeltLenketListe<>(new Integer[]{1, 2, 3});
        assertEquals(3, liste.antall());
    }

    @Test
    void endrerParametre() {
        Integer[] a = {1, 2, 3, 4, 5};
        Integer[] b = {1, 2, 3, 4, 5};
        new DobbeltLenketListe<>(a);
        assertArrayEquals(b, a, "Konstruktør endrer input-parameter");
    }

    @Test
    void nullElementer() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[]{null});
        assertEquals(0, liste.antall(), "Konstruktøren gjør noe feil når det er nullelementer i tabellen.");
        liste = new DobbeltLenketListe<>(new Integer[]{null, null});
        assertEquals(0, liste.antall(), "Konstruktøren gjør noe feil når det er nullelementer i tabellen.");
        liste = new DobbeltLenketListe<>(new Integer[]{null, 1, null});
        assertEquals(1, liste.antall(), "Konstruktøren gjør noe feil når det er nullelementer i tabellen.");
        liste = new DobbeltLenketListe<>(new Integer[]{null, 1, null, 2, null, 3, null});
        assertEquals(3, liste.antall(), "Konstruktøren gjør noe feil når det er nullelementer i tabellen.");
    }

    @Test
    void ikkeBrukLeggInn() {
        class TestLeggInn<T> extends DobbeltLenketListe<T> {
            public boolean leggInn(T verdi) {
                super.leggInn(verdi);
                super.leggInn(verdi);
                return true;
            }
            public TestLeggInn(T[] a) {super(a);}
        }
        Integer[] tall = {1, 2, 3, 4, 5};
        TestLeggInn<Integer> testListe = new TestLeggInn<>(tall);
        assertEquals(5, testListe.antall(), "Bruker leggInn-metoden i konstruktøren.");
    }
}

class Oppgave2Test {
    @Test
    void leggInnNull() {
        assertThrows(NullPointerException.class, () -> new DobbeltLenketListe<>().leggInn(null), "Kaster ingen eller gal feilmelding når legger inn nullverdi.");
    }

    @Test
    void leggInnBool() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        assertTrue(liste.leggInn(1), "leggInn skal returnere true");
        assertEquals(1, liste.antall(), "leggInn øker ikke antallet");
    }

    @Test
    void strenger() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        assertEquals("[]", liste.toString(), "Tom liste skal gi en streng på [].");
        assertEquals("[]", liste.omvendtString(), "Tom liste skal gi en omvendt streng på [].");

        liste.leggInn(1);
        String s = liste.toString();
        assertEquals("[1]", s, "Fikk et svar på "+s+" med toString, men skulle hatt [1].");
        s = liste.omvendtString();
        assertEquals("[1]", s, "Fikk et svar på "+s+" med omvendtString, men skulle hatt [1].");

        liste.leggInn(2);
        s = liste.toString();
        assertEquals("[1, 2]", s, "Fikk et svar på "+s+" med toString, men skulle hatt [1, 2].");
        s = liste.omvendtString();
        assertEquals("[2, 1]", s, "Fikk et svar på "+s+" med omvendtString, men skulle hatt [2, 1].");

        liste.leggInn(3);
        liste.leggInn(4);
        s = liste.toString();
        assertEquals("[1, 2, 3, 4]", s, "Fikk et svar på "+s+" med toString, men skulle hatt [1, 2, 3, 4].");
        s = liste.omvendtString();
        assertEquals("[4, 3, 2, 1]", s, "Fikk et svar på "+s+" med omvendtString, men skulle hatt [4, 3, 2, 1].");
    }

    @Test
    void toStringNull() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[]{null});
        assertEquals("[]", liste.toString(), "Gir ikke riktig streng på liste instansiert med nullverdier.");

        liste = new DobbeltLenketListe<>(new Integer[]{null, 1, null, 2, null});
        assertEquals("[1, 2]", liste.toString(), "Gir ikke riktig streng på liste instansiert med nullverdier.");
    }

    @Test
    void toStringTid() {
        Integer[] a = new Integer[20_000];
        for (int i = 0; i < 20_000; ++i) a[i] = i;
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(a);
        assertTimeout(Duration.ofMillis(20), liste::toString, "Metoden bruker alt for lang tid på å lage en streng.");
    }
}

class Oppgave3Test {
    @Test
    void hentTid() {
        Integer[] a = new Integer[100_000];
        for (int i = 0; i < 100_000; ++i) {
            a[i] = i;
        }
        Liste<Integer> testListe = new DobbeltLenketListe<>(a);
        assertTimeout(Duration.ofMillis(20), () -> {
            for (int i = 0; i < 10_000; ++i) testListe.hent(99_999);
        }, "Metoden hent gikk for tregt på store indekser. Har du kodet finnNode() riktig?");
        assertTimeout(Duration.ofMillis(20), () -> {
            for (int i = 0; i < 10_000; ++i) testListe.hent(0);
        }, "Metoden hent gikk for tregt på små indekser. Har du kodet finnNode() riktig?");
    }

    @Test
    void hentTom() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        assertThrows(IndexOutOfBoundsException.class, () -> liste.hent(0), "Kaster ingen eller gal feilmelding om vi prøver hente ut element fra tom liste.");
    }

    @Test
    void hentIndeksering() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(1);
        assertThrows(IndexOutOfBoundsException.class, () -> liste.hent(-1), "Kaster ingen eller gal feilmelding på negativ indeks.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.hent(1), "Kaster ingen eller gal feilmelding på for stor indeks.");
    }

    @Test
    void hentVerdi() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4});
        for (int i = 3; i >= 0; --i) {
            assertEquals(i+1, liste.hent(i), "Metoden hent() gir feil svar.");
        }
    }

    @Test
    void oppdaterNull() {
        Integer[] a = {1, 2, 3, 4};
        Liste<Integer> liste = new DobbeltLenketListe<>(a);
        assertThrows(NullPointerException.class, () -> liste.oppdater(3, null), "Ingen eller gal feilmelding når vi prøver legge inn en nullpeker.");
    }

    @Test
    void oppdaterIndekser() {
        Integer[] a = {1, 2, 3, 4};
        Liste<Integer> liste = new DobbeltLenketListe<>(a);
        assertThrows(IndexOutOfBoundsException.class, () -> liste.oppdater(4, 5), "Ingen eller gal feilmelding når vi oppdaterer verdi på for høy indeks.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.oppdater(-1, 5), "Ingen eller gal feilmelding når vi oppdaterer verdi på for lav indeks.");
    }

    @Test
    void oppdaterVerdi() {
        Integer[] a = {1, 3, 2, 4};
        Liste<Integer> liste = new DobbeltLenketListe<>(a);
        assertEquals(4, liste.oppdater(3, 5), "Oppdater gir ut gal verdi.");
        assertEquals(5, liste.hent(3), "Oppdater lagrer feil verdi i lista.");
        assertEquals(4, liste.antall(), "Antall elementer i lista skal ikke økes dersom en av dem oppdateres.");

        assertEquals(1, liste.oppdater(0, -1), "Oppdater gir ut gal verdi.");
        assertEquals(-1, liste.hent(0), "Oppdater lagrer feil verdi i lista.");
    }

    @Test
    void sublisteTom() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        assertEquals("[]", liste.subliste(0,0).toString(), "Tom subliste av tom liste skal være tom.");
    }

    @Test
    void sublisteDiverse() {
        Character[] cliste = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        DobbeltLenketListe<Character> liste = new DobbeltLenketListe<>(cliste);
        assertTrue(liste.subliste(3,3).tom(), "Subliste fra 3 til 3 skal være tom.");
        assertEquals("[A]", liste.subliste(0,1).toString(), "Subliste fra 0 til 1 skal kun inneholde første verdi.");
        assertEquals("[J]", liste.subliste(9,10).toString(), "Subliste fra 9 til 10 skal kun inneholde siste verdi");
        assertEquals("[A, B]", liste.subliste(0, 2).toString(), "Subliste fra 0 til 2 skal inneholde de to første verdiene.");
        assertEquals("[D, E, F, G]", liste.subliste(3, 7).toString(), "Subliste fra 3 til 7 skal inneholde D-G.");
        assertEquals(4, liste.subliste(3,7).antall(), "Feil lengde på subliste fra 3 til 7.");
    }

    @Test
    void sublisteFeil() {
        Boolean[] bliste = {true, false, false, true};
        DobbeltLenketListe<Boolean> liste = new DobbeltLenketListe<>(bliste);
        assertThrows(IndexOutOfBoundsException.class, () -> liste.subliste(-1, 1), "Kaster ingen eller gal feilmelding med subliste utenfor liste.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.subliste(3, 5), "Kaster ingen eller gal feilmelding med subliste utenfor liste.");
        assertThrows(IllegalArgumentException.class, () -> liste.subliste(2,1), "Kaster ingen eller gal feilmelding når fra er større enn til.");
    }
}

class Oppgave4Test {
    @Test
    void indeksTilTom() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        assertEquals(-1, liste.indeksTil(2), "En tom liste skal ikke inneholde noen verdier.");
    }

    @Test
    void indeksTilNull() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        assertDoesNotThrow(() -> liste.indeksTil(null), "Skal ikke kaste feilmelding med null som verdi.");
        assertEquals(-1, liste.indeksTil(null), "Nullpeker skal ikke finnes i lista.");
    }

    @Test
    void indeksTilDiverse() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 3, 5, 7});
        for (int i = 0; i < 4; ++i) {
            assertEquals(i, liste.indeksTil(2*i+1), "Verdier finnes på feil sted.");
        }
        assertEquals(-1, liste.indeksTil(0), "Finner ikke-eksisterende verdi.");
    }

    @Test
    void indeksTilEquals() {
        class Bubble {
            final int i;
            public Bubble(int i) {this.i = i;}

            @Override
            public boolean equals(Object o) {
                if (o instanceof Bubble) return this.i == ((Bubble) o).i;
                return super.equals(o);
            }
        }
        Liste<Bubble> liste = new DobbeltLenketListe<>();
        liste.leggInn(new Bubble(3));
        assertEquals(0, liste.indeksTil(new Bubble(3)), "Må bruke equals, og ikke ==, i sammenlikningen.");
    }

    @Test
    void indeksTilLikeVerdier() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(3);
        liste.leggInn(3);
        assertEquals(0, liste.indeksTil(3), "Metoden indeksTil skal gi ut første posisjon om det er flere like verdier.");
    }

    @Test
    void inneholderDiverse() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {0, 2, 4, 6});
        for (int i : new int[] {0, 2, 4, 6}) {
            assertTrue(liste.inneholder(i), "Metoden inneholder() gir feil svar.");
        }
        for (int i : new int[] {1, 3, 5, 7}) {
            assertFalse(liste.inneholder(i), "Metoden inneholder() gir feil svar.");
        }
    }
}

class Oppgave5Test {
    @Test
    void leggInnTom() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        assertThrows(IndexOutOfBoundsException.class, () -> liste.leggInn(-1, 1), "Gir ingen eller gal feilmelding om vi legger inn på negativ indeks.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.leggInn(1, 1), "Gir ingen eller gal feilmelding om vi legger inn på for høy indeks.");
        assertDoesNotThrow(() -> liste.leggInn(0, 1), "Skal være lov med indeks lik 0 også i tom liste.");
    }

    @Test
    void leggInnNull() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        assertThrows(NullPointerException.class, () -> liste.leggInn(0, null), "Gir ingen eller gal feilmelding om vi prøver legge inn en nullpeker.");
    }

    @Test
    void leggInnDiverse() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        int[] indekser = {0, 0, 2, 1, 3, 0, 6};
        int[] verdier = {4, 2, 6, 3, 5, 1, 7};

        for (int i = 0; i < 7; ++i) {
            int indeks = indekser[i];
            int verdi = verdier[i];
            assertDoesNotThrow(() -> liste.leggInn(indeks, verdi), "Gir feilmelding på lovlig innsetting.");
        }
        assertEquals(7, liste.antall(), "Ender opp med feil antall etter innsettinger.");
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", liste.toString(), "Verdier ender opp i feil rekkefølge etter innsetting.");
        assertEquals("[7, 6, 5, 4, 3, 2, 1]", liste.omvendtString(), "Verdier ender opp i feil rekkefølge etter innsetting.");
    }
}

class Oppgave6Test {
    @Test
    void fjernTomIndeks() {
        Liste<Character> liste = new DobbeltLenketListe<>();
        assertThrows(IndexOutOfBoundsException.class, () -> liste.fjern(0), "Fjerne element fra tom liste ga ingen eller gal feilmelding.");
    }

    @Test
    void fjernTomVerdi() {
        Liste<Character> liste = new DobbeltLenketListe<>();
        assertDoesNotThrow(() -> liste.fjern(null), "Å prøve fjerne via verdi fra tom liste burde ikke kaste feilmelding.");
        assertFalse(liste.fjern(null), "Å prøve fjerne via verdi fra tom liste burde gi false.");
    }

    @Test
    void fjernEttElementIndeks() {
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>();
        liste.leggInn("C");
        assertDoesNotThrow(() -> liste.fjern(0), "Noe går galt når man fjerner siste element fra lista.");
        assertEquals("[]", liste.toString(), "Ender opp med feil svar etter å ha fjernet siste element fra lista.");
        assertEquals("[]", liste.omvendtString(), "Ender opp med feil svar etter å ha fjernet siste element fra lista.");
    }

    @Test
    void fjernEttElementVerdi() {
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>();
        liste.leggInn("B");
        assertDoesNotThrow(() -> liste.fjern("B"), "Noe går galt når man fjerner siste element fra lista.");
        assertEquals("[]", liste.toString(), "Ender opp med feil svar etter å ha fjernet siste element fra lista.");
        assertEquals("[]", liste.omvendtString(), "Ender opp med feil svar etter å ha fjernet siste element fra lista.");
    }

    @Test
    void fjernIndekser() {
        String[] s = {"A", "B", "C", "D", "E", "F", "G"};
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>(s);
        assertThrows(IndexOutOfBoundsException.class, () -> liste.fjern(7), "Fjerne element fra ikke-eksisterende indeks ga ingen eller gal feilmelding.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.fjern(-1), "Fjerne element fra ikke-eksisterende indeks ga ingen eller gal feilmelding.");
        assertEquals("D", liste.fjern(3), "Feil returverdi når vi fjernet et element.");
        assertEquals(6, liste.antall(), "Antall elementer ikke oppdatert riktig når elementer fjernes.");
        liste.fjern(0); liste.fjern(4);
        assertEquals(4, liste.antall(), "Antall elementer ikke oppdatert riktig når elementer fjernes.");
        assertEquals("[B, C, E, F]", liste.toString(), "Feil liste etter fjerning.");
        assertEquals("[F, E, C, B]", liste.omvendtString(), "Feil liste etter fjerning.");
    }

    @Test
    void fjernVerdier() {
        String[] s = {"A", "B", "C", "D", "E", "F", "G"};
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>(s);
        assertFalse(liste.fjern(" "), "Liste påstår den klarer fjerne ikke-eksisterende verdi.");
        assertFalse(liste.fjern("J"), "Liste påstår den klarer fjerne ikke-eksisterende verdi.");
        assertTrue(liste.fjern("C"), "Liste klarer ikke fjerne eksisterende verdi.");
        assertTrue(liste.fjern("E"), "Liste klarer ikke fjerne eksisterende verdi.");
        assertFalse(liste.fjern("C"), "Liste påstår den klarer fjerne verdi som allerede er fjernet.");
        assertEquals(5, liste.antall(), "Feil antall etter fjerninger.");
    }

    @Test
    void fjernLike() {
        String[] s = {"A", "B", "C", "D", "B", "D", "C", "E", "F"};
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>(s);
        liste.fjern("B"); liste.fjern("D");
        assertEquals("[A, C, B, D, C, E, F]", liste.toString(), "Ikke fjernet riktige verdier når det er flere like.");
        assertEquals("[F, E, C, D, B, C, A]", liste.omvendtString(), "Ikke fjernet riktige verdier når det er flere like.");
    }

    @Test
    void fjernTid() {
        Integer[] intliste = new Integer[100_000];
        for (int i = 0; i < 100_000; ++i) intliste[i] = i;
        Liste <Integer> liste = new DobbeltLenketListe<>(intliste);

        long tidFjernVerdi = System.currentTimeMillis();
        for (int i = 40_000; i <= 50_000; ++i) liste.fjern((Integer) i);
        tidFjernVerdi = System.currentTimeMillis() - tidFjernVerdi;

        liste = new DobbeltLenketListe<>(intliste);
        long tidFjernIndeks = System.currentTimeMillis();
        for (int i = 40_000; i <= 50_000; ++i) liste.fjern(i);
        tidFjernIndeks = System.currentTimeMillis() - tidFjernIndeks;
        long maks = Math.max(tidFjernIndeks, tidFjernVerdi);
        long min = Math.min(tidFjernIndeks, tidFjernVerdi);

        assertTrue(maks <= 1.5*min, "En av fjern-metodene bruker sammenliknbart lenger tid enn den andre. ("+maks+"ms og "+min+"ms) Har du kodet dem ved hjelp av hverandre?");
    }
}

class Oppgave7Test {
    @Test
    void nullstillTest() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 2) return;

        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        for (int i = 1; i <= 7; i++) liste.leggInn(i);
        liste.nullstill();

        assertEquals(0, liste.antall(), "Nullstilt liste får ikke null elementer.");
        assertEquals("[]", liste.toString(), "Nullstilt liste er ikke tom");
        assertEquals("[]", liste.toString(), "Nullstilt liste er ikke tom");
        assertEquals(-1, liste.indeksTil(1), "Nullstilt liste inneholder elementer.");
    }
}

class Oppgave8Test{
    @Test
    void iteratorTom() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        Iterator<Integer> it = liste.iterator();
        assertFalse(it.hasNext(), "Iterator påstår har neste element selv om den er tom.");
        assertThrows(NoSuchElementException.class, it::next, "Metoden gir ingen eller gal feilmelding for iterator på tom liste.");
    }

    @Test
    void iteratorEttElement() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(1);
        Iterator<Integer> it = liste.iterator();
        assertTrue(it.hasNext(), "Iterator påstår det ikke finnes et neste element når det burde finnes.");
        assertEquals(1, it.next(), "Iterator gir feil nesteverdi på liste med ett element.");
        assertFalse(it.hasNext(), "Iterator påstår det finnes flere element når det ikke burde gjøre det.");
        assertThrows(NoSuchElementException.class, it::next, "Iterator gir feil eller ingen feilmelding på tømt iterator.");
    }

    @Test
    void iteratorFlereElement() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4, 5, 6, 7});

        int k = 1;
        for (Integer integer : liste) {
            assertEquals(k++, integer, "Iterator gir feil verdier eller svar ut i feil rekkefølge.");
        }
    }

    @Test
    void iteratorFjerning() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1,2,3,4,5});
        Iterator<Integer> it = liste.iterator();
        liste.fjern(0);
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator kaster ingen eller gal feilmelding dersom element fjernes fra lista etter startet iterasjon.");
        it = liste.iterator();
        liste.fjern((Integer) 3);
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator kaster ingen eller gal feilmelding dersom element fjernes fra lista etter startet iterasjon.");
    }

    @Test
    void iteratorLeggInn() {
        Liste<Integer> liste = new DobbeltLenketListe<>();
        Iterator<Integer> it = liste.iterator();
        liste.leggInn(8);
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator kaster ingen eller gal feilmelding om element er lagt til lista etter iterasjon startet.");
        it = liste.iterator();
        liste.leggInn(0, 1);
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator kaster ingen eller gal feilmelding om element er lagt til lista etter iterasjon startet.");
    }

    @Test
    void iteratorOppdatering() {
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4});
        Iterator<Integer> it = liste.iterator();
        liste.oppdater(3, 9);
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator kaster ingen eller gal feilmelding dersom element oppdateres etter at iterasjon er startet.");
    }

    @Test
    void iteratorNullstilling() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 2) return;
        Liste<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(1);
        liste.leggInn(2);
        liste.leggInn(3);
        Iterator<Integer> it = liste.iterator();
        liste.nullstill();
        assertThrows(ConcurrentModificationException.class, it::next, "Iterator gir ingen eller gal feilmelding dersom lista nullstilles etter at iterasjon er startet.");
    }

    @Test
    void iteratorGalIndeks() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4, 5, 6, 7});
        assertThrows(IndexOutOfBoundsException.class, () -> liste.iterator(7), "Iterator gir ut ingen eller gal feilmelding om vi prøver starte den på for høy indeks.");
        assertThrows(IndexOutOfBoundsException.class, () -> liste.iterator(-1), "Iterator gir ut ingen eller gal feilmelding om vi prøver starte den på for lav indeks.");
    }

    @Test
    void iteratorSenIndeks() {
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4, 5, 6, 7});
        int k = 4;
        Iterator<Integer> it = liste.iterator(3);
        while (it.hasNext()) {
            assertEquals(k++, it.next(), "Iterator gir ut gale verdier dersom den startes senere i lista.");
        }
        assertThrows(NoSuchElementException.class, it::next, "Iterator gir ut gal eller ingen feilmelding på slutten når den er startet fra senere sted.");
    }
}

class Oppgave9Test {
    @Test
    void removeTest() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Integer[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(a);
        for (Iterator<Integer> it = liste.iterator(); it.hasNext(); ) {
            int verdi = it.next();
            if (verdi % 2 != 0) {
                it.remove();
            }
        }
        assertEquals(6, liste.antall(), "Feil antall elementer igjen etter å ha fjernet elementer via iterators remove().");
        assertEquals("[2, 4, 6, 8, 10, 12]", liste.toString(), "Feil elementer i lista etter å ha fjernet elementer via iterators remove().");
        assertEquals("[12, 10, 8, 6, 4, 2]", liste.omvendtString(), "Feil elementer i lista etter å ha fjernet elementer via iterators remove().");
    }

    @Test
    void removeTom() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 3, 5, 7, 9, 11, 13});
        Iterator<Integer> it = liste.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        assertEquals("[]", liste.toString(), "Liste ikke tømt ordentlig via remove().");
        assertEquals("[]", liste.omvendtString(), "Liste ikke tømt ordentlig via remove().");
    }

    @Test
    void removeUlovlig() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Liste<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(1);
        liste.leggInn(2);
        liste.leggInn(3);
        Iterator<Integer> it = liste.iterator();
        assertThrows(IllegalStateException.class, it::remove, "Iterator gir ut ingen eller gal feilmelding dersom vi prøver fjerne noe før første element.");
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove, "Iterator gir ut ingen eller gal feilmelding dersom remove() kalles to ganger på rad.");
    }

    @Test
    void removeIteratorfeil() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Liste<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4, 5});
        Iterator<Integer> i1 = liste.iterator();
        Iterator<Integer> i2 = liste.iterator();
        i1.next(); i1.remove();
        assertThrows(ConcurrentModificationException.class, i2::next, "Iterator kaster ingen eller gal feilmelding dersom en iterator endrer lista når andre iterator prøver iterere.");
    }

    @Test
    void removeIteratorSen() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>(new Integer[] {1, 2, 3, 4});
        Iterator<Integer> it = liste.iterator(2);
        assertThrows(IllegalStateException.class, it::remove, "Iterator kaster ingen eller gal feilmelding dersom vi prøver starte iterator senere og så bruke remove() før next().");
    }
}

class Oppgave10Test {
    @Test
    void sorterTom() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Liste<String> liste = new DobbeltLenketListe<>(new String[0]);
        assertDoesNotThrow(() -> DobbeltLenketListe.sorter(liste, Comparator.naturalOrder()), "Sortering skal ikke gi feilmelding på tom liste.");
        assertEquals("[]", liste.toString(), "Endret på elementer når tom liste ble sortert.");
    }

    @Test
    void sorterEttElement() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Liste<String> liste = new DobbeltLenketListe<>(new String[] {"A"});
        DobbeltLenketListe.sorter(liste, Comparator.naturalOrder());
        assertEquals("[A]", liste.toString(), "Feil liste som svar når ett-elements-liste sorteres.");
    }

    @Test
    void sorterFlereElement() {
        if (DobbeltLenketListe.gruppeMedlemmer() < 3) return;
        Liste<String> liste = new DobbeltLenketListe<>(new String[] {"G", "B", "F", "E", "D", "A"});
        DobbeltLenketListe.sorter(liste, Comparator.naturalOrder());
        assertEquals("[A, B, C, D, E, F, G]", liste.toString(), "Feil sortering på liste med flere elementer.");
        DobbeltLenketListe.sorter(liste, Comparator.reverseOrder());
        assertEquals("[G, F, E, D, C, B, A]", liste.toString(), "Feil sortering på liste med flere elementer og reversert sortering.");
    }
}