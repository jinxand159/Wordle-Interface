import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Dictionary {
	private Map<String, String> dictionary;
	private int size;
	public Dictionary(String location) {
		dictionary = new Hashtable<String, String>();
		size = 0;
		loadDictionary(location);
	}
	
	public String elegirPalabra () {
		//When size is 1, there a very little change that random's value is 1, but we only have the element 0.
		int random = (int) (Math.random()*(size-1));
		int i=0;
		Iterator<String> iterator = dictionary.keySet().iterator();
		while(i<random) {
			iterator.next();
			i++;
		}
		return iterator.next();
	}

	private void loadDictionary(String location) {
		File archivo;
		FileReader fr;
		BufferedReader br;
		try {
			archivo = new File (location);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
			String auxString = br.readLine();
			while(auxString != null) {
				dictionary.put(auxString, auxString);
				size++;
				
				int pos = 0;
				for(int i=0; i<5 ; i++) {
					pos = auxString.charAt(i)-65;
					pos = (pos-22)%26;
					if(pos<0) {
						pos+=26;
					}
				}
				auxString = br.readLine();
			}

			
			if( null != fr ){   
				fr.close();     
			}       
		}catch(Exception e){
			System.out.println("Error en la inicializacion del fichero\n");
		}
	}

	public boolean inDictionary(String word) {	
		System.out.println(word);
		return dictionary.containsKey(word);
	}
}
