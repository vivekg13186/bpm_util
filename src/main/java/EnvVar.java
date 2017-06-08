/**
 * Created by vivek on 08/06/2017.
 */
public class EnvVar {

    public String project,snapshot,track,name,default_,dev,test,stage,prod;


    public String[] toArray(){
         String[] result= {
                project,snapshot,track,name,default_,dev,test,stage,prod
        };
        return  result;
    }
}
