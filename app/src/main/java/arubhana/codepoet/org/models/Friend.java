package arubhana.codepoet.org.models;

public class Friend {
    private boolean status;
    private String name;

    public Friend(String name,boolean status){
        this.name=name;
        this.status=status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
