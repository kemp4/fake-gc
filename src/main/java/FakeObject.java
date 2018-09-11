public abstract class FakeObject {


    protected long size;
    protected String content;


    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "FakeObject{" +
                "size=" + size +
                ", content='" + content + '\'' +
                '}';
    }
}
