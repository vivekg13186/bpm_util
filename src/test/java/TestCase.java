import bpm.util.EnvVarFromTWXFile;
import bpm.util.EnvVarModel;
import bpm.util.LoadEnvVarFromTWXTask;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by vivek on 08/06/2017.
 */
public class TestCase {

    @Test
    public void test1(){
        EnvVarFromTWXFile.process(new File("/Users/vivek/Documents/cts_projects/twx_inspection/base_1.zip"));
    }

    @Test
    public void test2() {
        new LoadEnvVarFromTWXTask(new File("/Users/vivek/Documents/cts_projects/twx_inspection/base_1.zip"), new LoadEnvVarFromTWXTask.Callback() {
            @Override
            public void onComplete(List<EnvVarModel> envs) {
                System.out.print(envs);
            }
        }).call();
    }
}
