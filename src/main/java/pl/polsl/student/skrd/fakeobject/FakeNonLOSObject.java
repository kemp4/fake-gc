package pl.polsl.student.skrd.fakeobject;

import static pl.polsl.student.skrd.settings.GlobalSettings.NON_LOS_SIZE;

public class FakeNonLOSObject extends FakeObject {



    public FakeNonLOSObject(String someContent) {
        content = someContent;
        size = NON_LOS_SIZE;

    }
}
