import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MiniGPT {
	String seed = "";
	HashMap<String, Integer> hashy;
	ArrayList<Integer[]> matrix;

	public MiniGPT(String fileName, int chainOrder) throws IOException {

		File input = new File(fileName);
		BufferedReader buff = new BufferedReader(new FileReader(input));
		hashy = new HashMap<String, Integer>();
		matrix = new ArrayList<Integer[]>();

		String previousN = "";
		for (int i = 0; i < chainOrder; i++) {
			previousN = previousN + (char) buff.read();
		}

		int numOn = 0;

		int max = 0;
		String storePrev = "";

		while (buff.ready()) {
			int place = buff.read();
			char c = (char) place;

			if (hashy.containsKey(previousN)) {
				Integer[] arr = matrix.get(hashy.get(previousN));
				arr[place]++;
				if (arr[place] >= max) {
					storePrev = previousN;
					max = arr[place];
				}

			} else {
				hashy.put(previousN, numOn);

				Integer[] arr = new Integer[256];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = 0;
				}

				matrix.add(arr);
				arr[place]++;

				if (arr[place] >= max) {
					storePrev = previousN;
					max = arr[place];
				}
				numOn++;
			}

			previousN = previousN.substring(1);
			previousN = previousN + c;
		}
		buff.close();

		int maxxy = 0;
		int row = hashy.get(storePrev);
		for (int i = 0; i < matrix.get(row).length; i++) {
			if (matrix.get(row)[i] >= max) {
				maxxy = i;
			}
		}
		seed = storePrev + (char) maxxy;
		seed = seed.substring(0, seed.length() - 1);
	}

	public void generateText(String outputFileName, int numChars) throws FileNotFoundException {
		StringBuilder str = new StringBuilder(seed);
		char nextAdd;
		String markov = seed;
		for (int i = 0; i < numChars; i++) {
			int row = hashy.get(markov);
			nextAdd = (char) getRandom(matrix.get(row));
			str.append(nextAdd);

			markov = markov.substring(1);
			markov = markov + nextAdd;
		}

		PrintWriter pw = new PrintWriter("Output.txt");
		pw.print(str.toString());
		pw.close();

	}

	public String printSeed() {
		return seed;
	}

	public int getRandom(Integer[] arr) // does the randomizing given an array of integers, returns an int that is
	{
		ArrayList<Integer> full = new ArrayList<Integer>();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i]; j++) {
				full.add(i);
			}
		}
		int loc = (int) (Math.random() * full.size());
		return full.get(loc);
	}
}