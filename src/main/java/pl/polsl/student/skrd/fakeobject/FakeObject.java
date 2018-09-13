package pl.polsl.student.skrd.fakeobject;

public abstract class FakeObject {


    protected long size;
    String content;


    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "pl.polsl.student.skrd.fakeobject.FakeObject{" +
                "size=" + size +
                ", content='" + content + '\'' +
                '}';
    }
}
