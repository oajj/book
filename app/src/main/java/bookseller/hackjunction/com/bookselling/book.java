package bookseller.hackjunction.com.bookselling;

/**
 * Created by ollip on 11/25/2017.
 */

public class book {
    private String name;
    private String state;
    private String condition;
    private String subject;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    book (String name1, String state1, String condition1, String subject1, String price1){
        price = price1;
        name = name1;
        state = state1;
        condition = condition1;
        subject = subject1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}