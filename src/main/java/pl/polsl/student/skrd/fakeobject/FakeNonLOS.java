package pl.polsl.student.skrd.fakeobject;

import static pl.polsl.student.skrd.settings.GlobalSettings.NON_LOS_SIZE;

public class FakeNonLOS extends FakeObject {



    public FakeNonLOS(String someContent) {
        content = someContent;
        size = NON_LOS_SIZE;

    }
}
