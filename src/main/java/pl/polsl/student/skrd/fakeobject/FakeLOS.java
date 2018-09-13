package pl.polsl.student.skrd.fakeobject;

import static pl.polsl.student.skrd.settings.GlobalSettings.LOS_SIZE;

public class FakeLOS extends FakeObject{


    public FakeLOS(String some_content) {
        content = some_content;
        size = LOS_SIZE;
    }
}
