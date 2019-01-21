package ifd.sparing.POJOs;

public class Olahraga {
    private  boolean sepakbola,badminton,tenismeja,voli,futsal,bolatenis,lari,basket;
    public Olahraga() {
    }

    public Olahraga(boolean sepakbola, boolean badminton, boolean tenismeja, boolean voli, boolean futsal, boolean bolatenis, boolean lari, boolean basket) {
        this.sepakbola = sepakbola;
        this.badminton = badminton;
        this.tenismeja = tenismeja;
        this.voli = voli;
        this.futsal = futsal;
        this.bolatenis = bolatenis;
        this.lari = lari;
        this.basket = basket;
    }

    public boolean isSepakbola() {
        return sepakbola;
    }

    public boolean isBadminton() {
        return badminton;
    }

    public boolean isTenismeja() {
        return tenismeja;
    }

    public boolean isVoli() {
        return voli;
    }

    public boolean isFutsal() {
        return futsal;
    }

    public boolean isBolatenis() {
        return bolatenis;
    }

    public boolean isLari() {
        return lari;
    }

    public boolean isBasket() {
        return basket;
    }
}
