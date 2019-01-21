package ifd.sparing.model;

public class GroupModel {
    private String time, title,lastChat;
    private Sparing sparing;
    public GroupModel(String time, String title, String lastChat) {
        this.time = time;
        this.title = title;
        this.lastChat = lastChat;
    }

    public GroupModel() {
    }

    public Sparing getSparing() {
        return sparing;
    }

    public void setSparing(Sparing sparing) {
        this.sparing = sparing;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }
}
