import org.junit.Test;

public class GCTests {
    @Test
    public void testGC(){

        FakeJVM jvm = new FakeJVM();

        FakeReference fr1 = jvm.fakeNew(new FakeNonLOS("some content1"));
        FakeReference fr2 = jvm.fakeNew(new FakeNonLOS("some content2"));
        FakeReference fr3 = jvm.fakeNew(new FakeNonLOS("some content3"));
        FakeReference fr4 = jvm.fakeNew(new FakeLOS("Large 1"));
        FakeReference fr5 = jvm.fakeNew(new FakeLOS("Large 2"));



        fr1.setRefValue(null);

        jvm.printAliveReferences();
        System.out.println(jvm.getObject(fr1));
    }
}
