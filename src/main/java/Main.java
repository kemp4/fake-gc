import fakeobject.FakeNonLOS;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main (String[] args){
        FakeJVM jvm = new FakeJVM();
        List<FakeReference> referencesArray = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            referencesArray.add(jvm.fakeNew(new FakeNonLOS("small object " + i)));
            if (i > 100) {
                referencesArray.get(i - 100).setRefValue(null);
            }
        }
    }
}
