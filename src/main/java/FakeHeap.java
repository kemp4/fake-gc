
public class FakeHeap {

    private long size = 1024;
    private long divisionAddress = 512;
    private long losPointer = 0;
    private long nonLosPointer = divisionAddress;


    public long getNonLosPointer() {
        return nonLosPointer;
    }

    public long getLosPointer() {
        return losPointer;
    }

    public long getLosSize() {
        return 0;
    }

    public long getNonLosSize() {
        return 0;
    }

    public long getLosFreeSize() {
        return divisionAddress -losPointer;
    }

    public long getNonLosFreeSize() {
        return size-nonLosPointer;
    }

    public void moveLosPointer(long size) {
        losPointer+=size;
    }
    public void moveNonLosPointer(long size) {
        nonLosPointer+=size;
    }
}
