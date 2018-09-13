package pl.polsl.student.skrd;

public class FakeReference implements Comparable<FakeReference> {


    private int timeToDeath;
    private Long refValue;

    public FakeReference(long refValue){
        this.refValue = refValue;
    }

    @Override
    public String toString() {
        return "pl.polsl.student.skrd.FakeReference{" +
                "refValue=" + refValue +
                '}';
    }

    public void deathGettingCloser(){
        timeToDeath--;
    }
    public boolean isDeathTime(){
        return timeToDeath <= 0;
    }
    public Long getRefValue() {
        return refValue;
    }
    public void setRefValue(Long refValue) {
        this.refValue = refValue;
    }
    public void setRefValueToNull() {
        this.refValue = null;
    }
    public boolean isRefNotNull(){
        return refValue != null;
    }

    @Override
    public int compareTo(FakeReference anotherRef) {
        if (this.timeToDeath == anotherRef.timeToDeath){
            return 0;
        }
        if (!this.isRefNotNull()){
            return 1;
        }
        if (!anotherRef.isRefNotNull()){
            return -1;
        }
        return this.timeToDeath>anotherRef.timeToDeath ? -1 : 1;
    }

    public boolean isRefNull() {
        return refValue == null;
    }

    public void setLifespan(int lifespan) {
        timeToDeath = lifespan;
    }
}
