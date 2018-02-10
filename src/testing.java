package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class testing {
	public static void main(String[] args) {
		Map<String, String[]> pb = new HashMap<String, String[]>();
		String[] nw = { "n1,n2,n3" };
		String[] sw = { "s1", "s2", "s3", "q5", "q7", "q9" };
		pb.put("nw", nw);
		pb.put("sw", sw);
		
        Set<String> keys = pb.keySet();
        for (String key:keys) {
        	for (String val:pb.get(key))
                System.out.println(val  + "->" );
        }
	}
}
