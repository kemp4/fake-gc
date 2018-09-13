package pl.polsl.student.skrd.fakeobject.factory;

import pl.polsl.student.skrd.fakeobject.FakeNonLOS;
import pl.polsl.student.skrd.fakeobject.FakeObject;

public class FakeNonLOSFactory implements FakeObjectFactory {
    int creationsNumber = 0;
    @Override
    public FakeObject createObject() {
        return new FakeNonLOS("nonLOS: "+creationsNumber++);
    }
}
