import org.junit.Test;

import java.io.File;

/**
 * Created by vivek on 08/06/2017.
 */
public class TestCase {

    @Test
    public void test1(){
        EnvVarFromTWXFile.process(new File("/Users/vivek/Documents/cts_projects/twx_inspection/base_1.zip"));
    }
}
