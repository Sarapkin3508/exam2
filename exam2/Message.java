package master;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Message implements Serializable {

    private String time;
    private String sender;
    private String text;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        Date timeDate = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
        String time = dt1.format(timeDate);
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        Date timeDate = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
        String time = dt1.format(timeDate);
        this.time = time;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
