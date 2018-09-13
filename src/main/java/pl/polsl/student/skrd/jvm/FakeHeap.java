package pl.polsl.student.skrd.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pl.polsl.student.skrd.settings.GlobalSettings.HEAP_SIZE;

public class FakeHeap {

    private long size = HEAP_SIZE;
    private long divisionAddress = HEAP_SIZE/2;
    private long losPointer = 0;
    private long nonLosPointer = divisionAddress;
    Logger logger = LoggerFactory.getLogger(FakeHeap.class);

    private long totalLosUnusedSum=0;
    private long totalNonLosUnusedSum=0;
    private long totalDivAddress=0;
    private long collectionsNum=0;


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

    public void saveStats() {
        totalLosUnusedSum+=getLosFreeSize();
        totalNonLosUnusedSum+=getNonLosFreeSize();
        totalDivAddress+= divisionAddress;
        collectionsNum++;
    }

    public void printStats() {
        double averageWasted =(double)(totalNonLosUnusedSum+totalLosUnusedSum)/(double) collectionsNum;
        System.out.println("Average wasted heap space: "+averageWasted);
        System.out.println("Average wasted heap space in %: "+averageWasted/(double)HEAP_SIZE);
        System.out.println("Average division address: "+((double)totalDivAddress/(double)collectionsNum)+"/"+HEAP_SIZE);

    }
}
