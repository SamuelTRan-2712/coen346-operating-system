public class Variable {
    public String name;
    public int value;
    public int lastAccessTime;

    public Variable(String name, int value) {
        this.name = name;
        this.value = value;
        this.lastAccessTime = 0;
    }
}
