public class FakeReference {



    private Long refValue;

    public FakeReference(long refValue){
        this.refValue = refValue;
    }

    @Override
    public String toString() {
        return "FakeReference{" +
                "refValue=" + refValue +
                '}';
    }

    public Long getRefValue() {
        return refValue;
    }

    public void setRefValue(Long refValue) {
        this.refValue = refValue;
    }
    public boolean isRefNotNull(){
        return refValue!=null;
    }

}
