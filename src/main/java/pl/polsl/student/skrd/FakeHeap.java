package pl.polsl.student.skrd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pl.polsl.student.skrd.settings.GlobalSettings.HEAP_SIZE;

public class FakeHeap {

    private long size = HEAP_SIZE;
    private long divisionAddress = HEAP_SIZE/2;
    private long losPointer = 0;
    private long nonLosPointer = divisionAddress;
    Logger logger = LoggerFactory.getLogger(FakeHeap.class);


    public void moveLosPointer(long size) {
        losPointer += size;
    }
    public void moveNonLosPointer(long size) {
        nonLosPointer += size;
    }

    public void resetPointers() {
        losPointer = 0;
        nonLosPointer = divisionAddress;
    }
    public void logStage(){
        logger.info("Heap state - LOS_POINTER: "+losPointer+" NON_LOS_POINTER: "+nonLosPointer+" HEAP_SIZE: "+size+" DIVISION_ADDRESS: "+divisionAddress );
    }

    public void setDivisionAddress(long divisionAddress) {
        this.divisionAddress = divisionAddress;
    }

    public long getNonLosPointer() {
        return nonLosPointer;
    }

    public long getLosPointer() {
        return losPointer;
    }

    public long getLosFreeSize() {
        return divisionAddress - losPointer;
    }

    public long getNonLosFreeSize() {
        return size-nonLosPointer;
    }


    public long getDivisionAddress() {
        return divisionAddress;
    }
}
