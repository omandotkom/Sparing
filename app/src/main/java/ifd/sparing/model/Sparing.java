package ifd.sparing.model;

public class Sparing {
    private String mOlahraga;
    private Integer pemainSekarang;
    private Integer pemainYangdibutuhkan;
    private String mTanggal;
    private String jam;
    private String threadID;
    private String chatID;
    private String channelID;
    private boolean full;
    public Sparing() {
    }

    public boolean isFull() {
        if (pemainYangdibutuhkan<=0){
            return true;
        }else{
            return false;
        }
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public Integer getPemainYangdibutuhkan() {
        return pemainYangdibutuhkan;
    }

    public void setPemainYangdibutuhkan(Integer pemainYangdibutuhkan) {
        this.pemainYangdibutuhkan = pemainYangdibutuhkan;
    }



    public void setPemainSekarang(Integer mcurrentPerson) {
        this.pemainSekarang = mcurrentPerson;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getThreadID() {
        return threadID;
    }

    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getJam() {
        return jam;
    }


    public Integer getPemainSekarang() {
        return pemainSekarang;
    }

    public String getmOlahraga() {
        return mOlahraga;
    }

    public void setmOlahraga(String mOlahraga) {
        this.mOlahraga = mOlahraga;
    }


    public String getmTanggal() {
        return mTanggal;
    }
    public String getStringDatetime(){
        return "Olahraga pada tanggal " + mTanggal + " pukul " + jam;
    }

    public void setmTanggal(String mTanggal) {
        this.mTanggal = mTanggal;
    }
}
