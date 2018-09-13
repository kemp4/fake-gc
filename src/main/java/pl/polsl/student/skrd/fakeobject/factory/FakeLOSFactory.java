package pl.polsl.student.skrd.fakeobject.factory;

import pl.polsl.student.skrd.fakeobject.FakeLOS;
import pl.polsl.student.skrd.fakeobject.FakeObject;

public class FakeLOSFactory implements FakeObjectFactory {

    int creationsNumber = 0;
    @Override
    public FakeObject createObject() {
        return new FakeLOS("LOS: "+creationsNumber++);
    }
}
