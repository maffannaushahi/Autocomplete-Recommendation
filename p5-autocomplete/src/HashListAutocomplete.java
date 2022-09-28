import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
        initialize(terms,weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k:" + k);
        }
        List<Term> temp = new ArrayList<>();
        if(myMap.containsKey(prefix)){
            temp= myMap.get(prefix);
        }
         List<Term> list = temp.subList(0, Math.min(k, temp.size()));
 
         return list;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<String, List<Term>>();
        myMap.put("", new ArrayList<Term>()); 
        for(int i =0; i <terms.length; i++){
            myMap.get("").add(new Term(terms[i], weights[i]));
            for(int k =1; k <terms[i].length() && k <= MAX_PREFIX; k++){
                String key = terms[i].substring(0,k); 
                // if (!myMap.containsKey(key)){
                //     myMap.put(key, new ArrayList<Term>());
                // }
                myMap.putIfAbsent(key, new ArrayList<Term>());
                myMap.get(key).add(new Term(terms[i], weights[i]));

            }
        }
        for(String x : myMap.keySet()){
            Collections.sort(myMap.get(x), Comparator.comparing(Term::getWeight).reversed()); 
        }
    }

    @Override
    public int sizeInBytes() {
        int mySize = 0;
        HashSet<Term> unique = new HashSet<>(); 
        for(String key : myMap.keySet()){
            mySize += BYTES_PER_CHAR*key.length();
            unique.addAll(myMap.get(key));
        }
            for(Term t : unique){
                mySize+= BYTES_PER_CHAR*t.getWord().length() + BYTES_PER_DOUBLE;
            }
        return mySize;
    }
    
}

