package Math.Set;

import Math.MathObject;
import Math.Print;
import static Math.Declare.*;
import java.util.ArrayList;

/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.1
 * @since 0.7
 */
public class Group {
    protected ArrayList<Number> array;
    public Group(){
        this(new ArrayList<Number>());
    }
    public Group(ArrayList<Number> pArr){
        assert pArr != null : "Cannot have a null parameter array!";
        array = pArr;
    }
    public Group(Group pGroup){
        this(pGroup.array());
    }
    public ArrayList<Number> array(){
        return array;
    }

}