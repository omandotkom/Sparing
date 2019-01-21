package ifd.sparing.POJOs;

public class UserPOJO {
    private String email, namaLengkap, alamatSekarang, gender;
    float rating;
    boolean sepakbola,badminton,tenismeja,voli,futsal,bolatenis,lari,basket;

    public void setOlahraga(boolean sepakbola, boolean badminton, boolean tenismeja, boolean voli, boolean futsal, boolean bolatenis, boolean lari, boolean basket) {
        this.sepakbola = sepakbola;
        this.badminton = badminton;
        this.tenismeja = tenismeja;
        this.voli = voli;
        this.futsal = futsal;
        this.bolatenis = bolatenis;
        this.lari = lari;
        this.basket = basket;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamatSekarang() {
        return alamatSekarang;
    }

    public void setAlamatSekarang(String alamatSekarang) {
        this.alamatSekarang = alamatSekarang;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isSepakbola() {
        return sepakbola;
    }

    public void setSepakbola(boolean sepakbola) {
        this.sepakbola = sepakbola;
    }

    public boolean isBadminton() {
        return badminton;
    }

    public void setBadminton(boolean badminton) {
        this.badminton = badminton;
    }

    public boolean isTenismeja() {
        return tenismeja;
    }

    public void setTenismeja(boolean tenismeja) {
        this.tenismeja = tenismeja;
    }

    public boolean isVoli() {
        return voli;
    }

    public void setVoli(boolean voli) {
        this.voli = voli;
    }

    public boolean isFutsal() {
        return futsal;
    }

    public void setFutsal(boolean futsal) {
        this.futsal = futsal;
    }

    public boolean isBolatenis() {
        return bolatenis;
    }

    public void setBolatenis(boolean bolatenis) {
        this.bolatenis = bolatenis;
    }

    public boolean isLari() {
        return lari;
    }

    public void setLari(boolean lari) {
        this.lari = lari;
    }

    public boolean isBasket() {
        return basket;
    }

    public void setBasket(boolean basket) {
        this.basket = basket;
    }
}
