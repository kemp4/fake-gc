import fakeobject.FakeLOS;
import fakeobject.FakeNonLOS;
import fakeobject.FakeObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GCTests {

    private FakeJVM jvm;
    private Random generator;

    @Before
    public void beforeEach()
    {
        jvm = new FakeJVM();
        generator = new Random();

    }

    @Test
    public void testGC() {
        FakeReference fr1 = jvm.fakeNew(new FakeNonLOS("some content1"));
        FakeReference fr2 = jvm.fakeNew(new FakeNonLOS("some content2"));
        FakeReference fr3 = jvm.fakeNew(new FakeNonLOS("some content3"));
        FakeReference fr4 = jvm.fakeNew(new FakeLOS("Large 1"));
        FakeReference fr5 = jvm.fakeNew(new FakeLOS("Large 2"));
        FakeReference fr6 = jvm.fakeNew(new FakeLOS("Large 3"));
        FakeReference fr7 = jvm.fakeNew(new FakeLOS("Large 4"));
        fr4.setRefValue(null);
        fr6.setRefValue(null);
        FakeReference fr8 = jvm.fakeNew(new FakeLOS("Large 5"));
        FakeReference fr9 = jvm.fakeNew(new FakeLOS("Large 6"));

        jvm.printAliveReferences();
        jvm.printAllObjects();
        jvm.printAliveObjects();
        System.out.println(jvm.getCollectionsNumber());
    }

    @Test
    public void testLosGC2() {
        List<FakeReference> referencesArray = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            referencesArray.add(jvm.fakeNew(new FakeLOS("large object " + i)));
            if (i > 2) {
                referencesArray.get(i - 3).setRefValue(null);
            }
        }
        System.out.println("references:");
        jvm.printAliveReferences();
        System.out.println("all objects on stack (dead too):");
        jvm.printAllObjects();
        System.out.println("number of collections "+jvm.getCollectionsNumber());
    }

    @Test
    public void testNonLosGC3() {
        List<FakeReference> referencesArray = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            referencesArray.add(jvm.fakeNew(new FakeNonLOS("small object " + i)));
            if (i > 100) {
                referencesArray.get(i - 100).setRefValue(null);
            }
        }
        System.out.println("references:");
        jvm.printAliveReferences();
        System.out.println("all objects on stack (dead too):");
        jvm.printAllObjects();
        System.out.println("number of collections TOTAL: "+jvm.getCollectionsNumber()+"  LOS: "+jvm.getByLosCollectionsNumber()+"  NON-LOS:"+jvm.getByNonLosCollectionsNumber());
    }

    @Test
    public void mainTest() {

    }

    @Test
    public void mixedTest() {

        List<FakeReference> referencesArray = new ArrayList<>();
        List<Integer> losIndexes = new LinkedList<>();
        List<Integer> nonLosIndexes = new LinkedList<>();

        for (int i=0; i < 200; i++) {
            FakeObject object;
            if (generator.nextFloat() > 0.02) {
                object = new FakeNonLOS("nonLOS" + i);
                referencesArray.add(jvm.fakeNew(object));
                nonLosIndexes.add(i);
            } else {
                object = new FakeLOS("LOS " + i);
                referencesArray.add(jvm.fakeNew(object));
                losIndexes.add(i);
            }
            if (losIndexes.size()>3){
                int index = Math.abs(generator.nextInt()%losIndexes.size());
                referencesArray.get(losIndexes.get(index)).setRefValue(null);
                losIndexes.remove(index);
            }
            if (nonLosIndexes.size()>100){
                int index = Math.abs(generator.nextInt()%nonLosIndexes.size());
                referencesArray.get(nonLosIndexes.get(index)).setRefValue(null);
                nonLosIndexes.remove(index);
            }
        }
        System.out.println("references:");
        jvm.printAliveReferences();
        System.out.println("all objects on stack (dead too):");
        jvm.printAllObjects();
        System.out.println("number of collections TOTAL: "+jvm.getCollectionsNumber()+"  LOS: "+jvm.getByLosCollectionsNumber()+"  NON-LOS:"+jvm.getByNonLosCollectionsNumber());
    }
}