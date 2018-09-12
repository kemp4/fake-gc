import exceptions.FakeOutOfMemoryException;
import fakeobject.FakeObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FakeGarbageCollector {

    private long collectionsNumber = 0;
    private long collectionsByLosNumber = 0;

    private final boolean IS_SPACE_TUNER_ON = false;
    private final Map<Long,FakeObject> refMap;
    private final FakeHeap heap;
    private final List<FakeReference> references;
    private long threshold = 20l;
    private long nonLosAllocationSpeed;
    private long losAllocationSpeed;

    FakeGarbageCollector(FakeHeap heap, Map<Long, FakeObject> refMap, List<FakeReference> references){
        this.refMap = refMap;
        this.heap = heap;
        this.references = references;
    }

    public long allocateObject(FakeObject object){
        if( object.getSize() >= threshold ){
            if (isLosAllocPossible(object)){
                return allocateObjectInLos(object);
            }else{
                collectionsByLosNumber++;
                runCollection();
                return retryAllocationAfterCollection(object);
            }
        }else{
            if(isNonLosAllocPossible(object)){
                return allocateObjectInNonLos(object);
            }else{
                runCollection();
                return retryAllocationAfterCollection(object);
            }
        }
    }


    private long retryAllocationAfterCollection(FakeObject object) {
        if((object.getSize() >= threshold )&&(!isLosAllocPossible(object))){
            throw new FakeOutOfMemoryException("out of memory in LOS space, rerun with bigger heap setting");
        }
        if((object.getSize() < threshold )&&(!isNonLosAllocPossible(object))) {
            throw new FakeOutOfMemoryException("out of memory in non-LOS space, rerun with bigger heap setting");
        }
        return allocateObject(object);
    }

    private void runCollection()  {
        collectionsNumber++;
        if(IS_SPACE_TUNER_ON) {
            long heapNewDivisionAddress = calculateNewDivisionAddress();
            heap.setDivisionAddress(heapNewDivisionAddress);
        }
        heap.resetPointers();
        Map<Long,FakeObject> refMapBuffer = new TreeMap<>();
        refMap.entrySet().forEach(e->reallocate(e,refMapBuffer));
        refMap.clear();
        refMap.putAll(refMapBuffer);
    }

    private void reallocate(Map.Entry<Long, FakeObject> entry, Map<Long, FakeObject> refMapBuffer) {
        if (references.stream().map(FakeReference::getRefValue).anyMatch(val -> entry.getKey().equals(val))) {
            long newRefValue = allocateObject(entry.getValue());
            refMapBuffer.put(newRefValue, entry.getValue());
            references.stream()
                    .filter(fr -> entry.getKey().equals(fr.getRefValue()))
                    .forEach(fr -> fr.setRefValue(newRefValue));
        }
    }


    private long calculateNewDivisionAddress() {
        long survivorsSize = sumAliveObjects();
        return losAllocationSpeed / ( (losAllocationSpeed+nonLosAllocationSpeed) *
                (heap.getLosFreeSize()+heap.getNonLosFreeSize())+(survivorsSize) );

    }

    private long sumAliveObjects() {
        return references.stream()
                .filter(FakeReference::isRefNotNull)
                .map(FakeReference::getRefValue)
                .distinct()
                .map(refMap::get)
                .mapToLong(FakeObject::getSize)
                .sum();
    }

    private long allocateObjectInLos(FakeObject object) {
        long actualAddress = heap.getLosPointer();
        losAllocationSpeed+=object.getSize();
        heap.moveLosPointer(object.getSize());
        return actualAddress;
    }

    private long allocateObjectInNonLos(FakeObject object) {
        long actualAddress = heap.getNonLosPointer();
        nonLosAllocationSpeed+=object.getSize();
        heap.moveNonLosPointer(object.getSize());
        return actualAddress;
    }


    private boolean isNonLosAllocPossible(FakeObject object) {
        return (heap.getNonLosFreeSize() > object.getSize());
    }

    private boolean isLosAllocPossible(FakeObject object){
        return (heap.getLosFreeSize() > object.getSize());
    }

    public long getCollectionsNumber() {
        return collectionsNumber;
    }
    public long getCollectionsByLosNumber() {
        return collectionsByLosNumber;
    }

//    private List<FakeObject> getAliveObjects() {
//        return references.stream()
//                .filter(FakeReference::isRefNotNull)
//                .map(FakeReference::getRefValue)
//                .distinct()
//                .map(refMap::get)
//                .collect(Collectors.toList());
//    }
}
