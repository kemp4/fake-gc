package pl.polsl.student.skrd;

import pl.polsl.student.skrd.exceptions.FakeNullReferenceException;
import pl.polsl.student.skrd.fakeobject.FakeObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pl.polsl.student.skrd.settings.GlobalSettings.IS_SPACE_TUNER_ON;


public class FakeJVM {

    Logger logger = LoggerFactory.getLogger(FakeJVM.class);
    private final FakeHeap heap;
    private final FakeGarbageCollector gc;
    private final List<FakeReference> references;
    private final Map<Long,FakeObject> refMap;

   // final static Logger logger = LoggerFactory.getLogger(FakeJVM.class);

    public FakeJVM(){
        logger.info("Fake JVM created. GC with "+(IS_SPACE_TUNER_ON?"en":"dis")+"abled Space Tuner");

        heap = new FakeHeap();
        refMap = new TreeMap<>();
        references = new LinkedList<>();
        gc = new FakeGarbageCollector(heap, refMap, references);
    }
    public FakeReference fakeNew(FakeObject object){
        return fakeNew(object,0);
    }
    public FakeReference fakeNew(FakeObject object, int lifespan){
        long fakeRefVal = gc.allocateObject(object);
        FakeReference newFakeRef = new FakeReference(fakeRefVal);
        newFakeRef.setLifespan(lifespan);
        references.add(newFakeRef);
        refMap.put(fakeRefVal,object);
        return newFakeRef;
    }

    public void printAliveReferences(){
        references.stream()
                .filter(FakeReference::isRefNotNull)
                .forEach(System.out::println);
    }

    public FakeObject getObject(FakeReference fakeReference) throws FakeNullReferenceException {
        if(fakeReference == null){
            throw new FakeNullReferenceException();
        }
        return refMap.get(fakeReference.getRefValue());
    }
    public long getCollectionsNumber(){
        return gc.getCollectionsNumber();
    }

    public void printAllObjects() {
        refMap.entrySet().forEach(System.out::println);
    }

    public void printAliveObjects() {
        refMap.entrySet().stream();
    }


    public long getByLosCollectionsNumber() {
        return gc.getCollectionsByLosNumber();
    }

    public long getByNonLosCollectionsNumber() {
        return gc.getCollectionsNumber() - gc.getCollectionsByLosNumber();
    }
}
