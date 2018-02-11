package src;

//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
//import java.util.TreeMap;

public class testing {
	public static void main(String[] args) {
		Map<String, String[]> pb = new LinkedHashMap<String, String[]>();
		String[] nw = { "n1", "n2", "n3" };
		String[] sw = { "s1", "s2", "s3", "q5", "q7", "q9" };
		String[] yom = { "b", "a", "d", " ", "a", "s", "s" };
		String[] aa = { "berf" };
		pb.put("yom", yom);
		pb.put("aa", aa);
		pb.put("nw", nw);
		pb.put("sw", sw);

		Set<String> keys = pb.keySet();
		for (String key : keys) {
			src.debug.printArray(pb.get(key), ", ");
		}

	}
}
