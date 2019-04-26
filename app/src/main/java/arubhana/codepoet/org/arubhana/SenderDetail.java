package arubhana.codepoet.org.arubhana;

public class SenderDetail {
    String name;
    public SenderDetail(String name){
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                '}';
    }
}
