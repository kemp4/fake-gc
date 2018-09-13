package pl.polsl.student.skrd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.polsl.student.skrd.exceptions.FakeOutOfMemoryException;
import pl.polsl.student.skrd.fakeobject.FakeObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static pl.polsl.student.skrd.settings.GlobalSettings.*;


public class FakeGarbageCollector {
    Logger logger = LoggerFactory.getLogger(FakeGarbageCollector.class);
    private long collectionsNumber = 0;
    private long collectionsByLosNumber = 0;

    private final Map<Long,FakeObject> refMap;
    private final FakeHeap heap;
    private final List<FakeReference> references;
    private long nonLosAllocationSpeed;
    private long losAllocationSpeed;

    FakeGarbageCollector(FakeHeap heap, Map<Long, FakeObject> refMap, List<FakeReference> references){
        this.refMap = refMap;
        this.heap = heap;
        this.references = references;
    }

    public long allocateObject(FakeObject object){
        if( object.getSize() >= THRESHOLD ){
            if (isLosAllocPossible(object)){
                return allocateObjectInLos(object);
            }else{
                collectionsByLosNumber++;
                logger.debug("Collection "+collectionsNumber + " launched. Caused by LOS space overflow. LOS_ALLOC_SPEED = "+losAllocationSpeed+ " NON_LOS_ALLOC_SPEED = " +nonLosAllocationSpeed);
                runCollection();
                return retryAllocationAfterCollection(object);
            }
        }else{
            if(isNonLosAllocPossible(object)){
                return allocateObjectInNonLos(object);
            }else{
                logger.debug("Collection "+collectionsNumber + " launched. Caused by NON LOS space overflow. LOS_ALLOC_SPEED = "+losAllocationSpeed+ " NON_LOS_ALLOC_SPEED = " +nonLosAllocationSpeed);
                runCollection();
                return retryAllocationAfterCollection(object);
            }
        }
    }


    private long retryAllocationAfterCollection(FakeObject object) {
        if((object.getSize() >= THRESHOLD )&&(!isLosAllocPossible(object))){
            throw new FakeOutOfMemoryException("out of memory in LOS space, rerun with bigger heap setting");
        }
        if((object.getSize() < THRESHOLD )&&(!isNonLosAllocPossible(object))) {
            throw new FakeOutOfMemoryException("out of memory in non-LOS space, rerun with bigger heap setting");
        }
        return allocateObject(object);
    }

    private void runCollection()  {
        System.out.println();
        collectionsNumber++;
        heap.logStage();
        if(IS_SPACE_TUNER_ON) {
            double heapNewDivisionAddress = calculateNewDivisionAddress();
            logger.debug("Space Tuner calculated new DivisionAddress : " +(long)heapNewDivisionAddress+" (Last Was : "+
                    heap.getDivisionAddress() +") LOS finished at " + heap.getLosPointer()+" NON_LOS finished at "+heap.getNonLosPointer() + "(total "+ HEAP_SIZE+")" );
            heap.setDivisionAddress((long)heapNewDivisionAddress);
        }
        heap.resetPointers();
        Map<Long,FakeObject> refMapBuffer = new TreeMap<>();

        Iterator<Map.Entry<Long, FakeObject>> iter = refMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, FakeObject> e = iter.next();
            reallocate(e, refMapBuffer);
        }
        refMap.clear();
        refMap.putAll(refMapBuffer);
        losAllocationSpeed = 0;
        nonLosAllocationSpeed = 0;
    }

    private void reallocate(Map.Entry<Long, FakeObject> entry, Map<Long, FakeObject> refMapBuffer) {

        if (references.stream().map(FakeReference::getRefValue).anyMatch(val -> entry.getKey().equals(val))) {
            long newRefValue = retryAllocationAfterCollection(entry.getValue());
            refMapBuffer.put(newRefValue, entry.getValue());
            references.stream()
                    .filter(fr -> entry.getKey().equals(fr.getRefValue()))
                    .forEach(fr -> fr.setRefValue(newRefValue));
        }
    }

    private double calculateNewDivisionAddress() {
        double losSurvivorsSize = sumLosSurvivors();
        double foo = losAllocationSpeed+nonLosAllocationSpeed;
        double freeHeapSpaceAfterCollection = HEAP_SIZE - (losSurvivorsSize + sumNonLosSurvivors());
        if(freeHeapSpaceAfterCollection<HEAP_SIZE/10){logger.warn("there will be less than 10% heap space left after collection. OUT OF MEMORY EXCEPTION MAY OCCUR LOS  (total heap size = "+HEAP_SIZE+" )");}
        if((heap.getLosFreeSize()+ heap.getNonLosFreeSize())<HEAP_SIZE/10){logger.warn(" GIIIITTTTRT ap size = "+HEAP_SIZE+" )");} //todo write some better message
        double proportion = losAllocationSpeed / foo;
        return proportion * freeHeapSpaceAfterCollection + losSurvivorsSize;
    }

    private long sumNonLosSurvivors() {
        try {
            return references.stream()
                    .filter(FakeReference::isRefNotNull)
                    .map(FakeReference::getRefValue)
                    .distinct()
                    .map(refMap::get)
                    .mapToLong(FakeObject::getSize)
                    .filter(l -> l < THRESHOLD)
                    .sum();
        } catch (Exception e) {
            logger.warn("there are no objects in LOS space");
            return 0;
        }
    }


    private long sumLosSurvivors() {
        try {
            return references.stream()
                    .filter(FakeReference::isRefNotNull)
                    .map(FakeReference::getRefValue)
                    .distinct()
                    .map(refMap::get)
                    .mapToLong(FakeObject::getSize)
                    .filter(l -> l >= THRESHOLD)
                    .sum();
        }catch (Exception e){
            logger.warn("there are no objects in LOS space");
            return 0;
        }
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

}
