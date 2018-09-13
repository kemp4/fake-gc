package pl.polsl.student.skrd.fakeobject.factory;

import pl.polsl.student.skrd.fakeobject.FakeObject;

public interface FakeObjectFactory {
    FakeObject createObject();
    int getCreationsNumber();
}
