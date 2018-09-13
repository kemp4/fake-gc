package pl.polsl.student.skrd.fakeobject.factory;

import pl.polsl.student.skrd.fakeobject.FakeLOSObject;
import pl.polsl.student.skrd.fakeobject.FakeObject;

public class FakeLOSFactory implements FakeObjectFactory {

    int creationsNumber = 0;

    @Override
    public FakeObject createObject() {
        return new FakeLOSObject("LOS: "+creationsNumber++);
    }

    @Override
    public int getCreationsNumber() {
        return creationsNumber;
    }
}
