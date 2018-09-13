package pl.polsl.student.skrd;

import org.junit.After;
import org.junit.Ignore;
import pl.polsl.student.skrd.fakeobject.FakeLOSObject;
import pl.polsl.student.skrd.fakeobject.FakeNonLOSObject;
import pl.polsl.student.skrd.fakeobject.FakeObject;
import org.junit.Before;
import org.junit.Test;
import pl.polsl.student.skrd.fakeobject.FakeReference;
import pl.polsl.student.skrd.fakeobject.factory.FakeLOSFactory;
import pl.polsl.student.skrd.fakeobject.factory.FakeNonLOSFactory;
import pl.polsl.student.skrd.fakeobject.factory.FakeObjectFactory;
import pl.polsl.student.skrd.jvm.FakeJVM;

import java.util.*;
import java.util.stream.IntStream;

import static pl.polsl.student.skrd.settings.GlobalSettings.*;

public class GCTests {

    private FakeJVM jvm;
    private Random generator;
    private long allocationsNum;
    private List<FakeReference> referencesArray;
    private PriorityQueue<FakeReference> deathQueue;
    private FakeObjectFactory losFactory;
    private FakeObjectFactory nonLosFactory;

    @Before
    public void beforeEach()
    {
        jvm = new FakeJVM();
        generator = new Random();
        referencesArray = new ArrayList<>();
        deathQueue = new PriorityQueue<>();
        allocationsNum = 0;
        losFactory = new FakeLOSFactory();
        nonLosFactory = new FakeNonLOSFactory();
    }




    @Test
    public void mainTest() {
        for (int i =0 ; i < ITERATIONS_NUMBER ; i++){
            allocateInLOS(drawLosAllocsNumber());
            allocateInNonLOS(drawNonLosAllocsNumber());
            deathQueue.stream().forEach(FakeReference::deathGettingCloser);
            deathQueue.stream()
                    .filter(FakeReference::isDeathTime)
                    .forEach(FakeReference::setRefValueToNull);
            deathQueue.removeIf(FakeReference::isRefNull);
        }
    }

    private int drawLosAllocsNumber() {
        return generator.nextInt()%(MAX_LOS_PER_ITERATION-MIN_LOS_PER_ITERATION)+MIN_LOS_PER_ITERATION;
    }

    private int drawNonLosAllocsNumber() {
        return generator.nextInt()%(MAX_NON_LOS_PER_ITERATION-MIN_NON_LOS_PER_ITERATION)+MIN_NON_LOS_PER_ITERATION;
    }

    private void allocateInLOS(int objectsNumber){
        IntStream.rangeClosed(1, objectsNumber)
                .mapToObj(i -> jvm.fakeNew(losFactory.createObject(),drawLosLifespan()))
                .forEach(deathQueue::add);

    }
    private void allocateInNonLOS(int objectsNumber){
        IntStream.range(1, objectsNumber)
                .mapToObj(i -> jvm.fakeNew(nonLosFactory.createObject(), drawNonLosLifespan()))
                .forEach(deathQueue::add);
    }

    @After
    public void printResults(){
        long allCreations = losFactory.getCreationsNumber() + nonLosFactory.getCreationsNumber();
        System.out.println("number of collections TOTAL: "+jvm.getCollectionsNumber()+"  LOS CAUSED: "+jvm.getByLosCollectionsNumber()+"  NON-LOS CAUSED:"+jvm.getByNonLosCollectionsNumber());
        System.out.println("created objects TOTAL: "+allCreations+" LOS CREATED: "+losFactory.getCreationsNumber()+" NON-LOS CREATED: "+ nonLosFactory.getCreationsNumber());
        jvm.printStats();
    }

    private int drawLosLifespan(){
        return 1 + generator.nextInt() % MAX_LOS_LIFESPAN;
    }

    private int drawNonLosLifespan(){
        return 1 + generator.nextInt() % MAX_NON_LOS_LIFESPAN;
    }
    @Ignore
    @Test
    public void testGC() {
        FakeReference fr1 = jvm.fakeNew(new FakeNonLOSObject("some content1"));
        FakeReference fr2 = jvm.fakeNew(new FakeNonLOSObject("some content2"));
        FakeReference fr3 = jvm.fakeNew(new FakeNonLOSObject("some content3"));
        FakeReference fr4 = jvm.fakeNew(new FakeLOSObject("Large 1"));
        FakeReference fr5 = jvm.fakeNew(new FakeLOSObject("Large 2"));
        FakeReference fr6 = jvm.fakeNew(new FakeLOSObject("Large 3"));
        FakeReference fr7 = jvm.fakeNew(new FakeLOSObject("Large 4"));
        fr4.setRefValue(null);
        fr6.setRefValue(null);
        FakeReference fr8 = jvm.fakeNew(new FakeLOSObject("Large 5"));
        FakeReference fr9 = jvm.fakeNew(new FakeLOSObject("Large 6"));

    }
    @Ignore
    @Test
    public void testLosGC2() {
        for (int i = 0; i < 6; i++) {
            referencesArray.add(jvm.fakeNew(new FakeLOSObject("large object " + i)));
            if (i > 2) {
                referencesArray.get(i - 3).setRefValueToNull();
            }
        }
    }
    @Ignore
    @Test
    public void testNonLosGC3() {

        for (int i = 0; i < 5000; i++) {
            referencesArray.add(jvm.fakeNew(new FakeNonLOSObject("small object " + i)));
            if (i > 100) {
                referencesArray.get(i - 100).setRefValueToNull();
            }
        }
    }
    @Ignore
    @Test
    public void mixedTest() {

        List<Integer> losIndexes = new LinkedList<>();
        List<Integer> nonLosIndexes = new LinkedList<>();

        for (int i = 0; i < 100; i++) {
            FakeObject object;
            if (generator.nextFloat() > 0.05) {
                object = new FakeNonLOSObject("nonLOS" + i);
                referencesArray.add(jvm.fakeNew(object));
                nonLosIndexes.add(i);
            } else {
                object = new FakeLOSObject("LOS " + i);
                referencesArray.add(jvm.fakeNew(object));
                losIndexes.add(i);
            }
            if (losIndexes.size() > 3){
                int index = Math.abs(generator.nextInt() % losIndexes.size());
                referencesArray.get(losIndexes.get(index)).setRefValueToNull();
                losIndexes.remove(index);
            }
            if (nonLosIndexes.size() > 100){
                int index = Math.abs(generator.nextInt() % nonLosIndexes.size());
                referencesArray.get(nonLosIndexes.get(index)).setRefValueToNull();
                nonLosIndexes.remove(index);
            }
        }
    }


}
