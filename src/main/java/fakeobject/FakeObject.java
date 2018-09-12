package fakeobject;

public abstract class FakeObject {


    long size;
    String content;


    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "fakeobject.FakeObject{" +
                "size=" + size +
                ", content='" + content + '\'' +
                '}';
    }
}
