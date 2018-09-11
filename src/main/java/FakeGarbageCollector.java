import java.util.List;
import java.util.Map;

public class FakeGarbageCollector {

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
                runCollection();
                return allocateObject(object);
            }
        }else{
            if(isNonLosAllocPossible(object)){
                return allocateObjectInNonLos(object);
            }else{
                runCollection();
                return allocateObject(object);
            }
        }
    }

    private void runCollection() {
        //long heapNewDivisionAddress = calculateNewDivisionAddress();

    }

    private long calculateNewDivisionAddress() {


//        losAllocationSpeed/((losAllocationSpeed+nonLosAllocationSpeed)*
//                (heap.getLosFreeSize()+heap.getNonLosFreeSize())+(survivorsSize));
        return 0;
    }

    private long allocateObjectInLos(FakeObject object) {
        losAllocationSpeed+=object.getSize();
        heap.moveLosPointer(object.getSize());
        return heap.getLosPointer();
    }

    private long allocateObjectInNonLos(FakeObject object) {
        nonLosAllocationSpeed+=object.getSize();
        heap.moveNonLosPointer(object.getSize());
        return heap.getNonLosPointer();
    }


    private boolean isNonLosAllocPossible(FakeObject object) {
        return (heap.getNonLosFreeSize() > object.getSize());
    }

    private boolean isLosAllocPossible(FakeObject object){
        return (heap.getLosFreeSize() > object.getSize());
    }

}
