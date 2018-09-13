package pl.polsl.student.skrd.fakeobject.factory;

import pl.polsl.student.skrd.fakeobject.FakeNonLOSObject;
import pl.polsl.student.skrd.fakeobject.FakeObject;

public class FakeNonLOSFactory implements FakeObjectFactory {

    int creationsNumber = 0;

    @Override
    public FakeObject createObject() {
        return new FakeNonLOSObject("nonLOS: "+creationsNumber++);
    }

    @Override
    public int getCreationsNumber() {
        return creationsNumber;
    }
}
