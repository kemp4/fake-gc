import exceptions.FakeNullReferenceException;
import fakeobject.FakeObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FakeJVM {


    private final FakeHeap heap;
    private final FakeGarbageCollector gc;
    private final List<FakeReference> references;
    private final Map<Long,FakeObject> refMap;

    public FakeJVM(){
        heap = new FakeHeap();
        refMap = new TreeMap<>();
        references = new LinkedList<>();
        gc = new FakeGarbageCollector(heap, refMap, references);
    }

    public FakeReference fakeNew(FakeObject object){
        long fakeRefVal = gc.allocateObject(object);
        FakeReference newFakeRef = new FakeReference(fakeRefVal);
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
        if(fakeReference==null){
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
