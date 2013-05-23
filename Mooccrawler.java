import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Mooccrawler {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		String path = "/home/rpickhardt/data/blogs/rp/mooc/mooc/";

		HashMap<String, LinkedList<Integer>> globalRank = new HashMap<String, LinkedList<Integer>>();
		HashMap<String, LinkedList<Integer>> globalVote = new HashMap<String, LinkedList<Integer>>();

		for (int i = 9; i < 24; i++) {
			for (int j = 0; j < 24; j++) {

				HashMap<String, Integer> rankings = new HashMap<String, Integer>();
				HashMap<String, Integer> votes = new HashMap<String, Integer>();
				int rank = 0;

				for (int k = 1; k < 27; k++) {
					String day = i < 10 ? "0" + i : "" + i;
					String hour = j < 10 ? "0" + j : "" + j;
					String fileName = path + day + "-" + hour + "-15/page" + k
							+ ".htm";
					try {
						BufferedReader br = IOHelper.openReadFile(fileName);
						String line = "";

						boolean flag = false;
						System.out.println(fileName);
						while ((line = br.readLine()) != null) {
							if (line.startsWith("<div class='container main'>")) {
								flag = true;
							}
							if (flag) {
								if (line.startsWith("<a class='article-item span10' href='")) {
									String uri = line
											.replace(
													"<a class='article-item span10' href='",
													"http://www.moocfellowship.org");
									rank++;
									boolean voteCountFound = false;
									int cnt = 0;
									rankings.put(uri, rank);
									while ((line = br.readLine()) != null) {
										cnt++;
										if (line.contains("<span class='num'>")) {
											voteCountFound = true;
											break;
										}
										if (cnt > 10) {
											break;
										}
									}
									if (voteCountFound) {
										line = br.readLine();
										Integer count = Integer.parseInt(line);
										votes.put(uri, count);
										// System.out.println("\t\t" + count
										// + "\t" + uri);
									} else {
										// System.out.println("\t\t" + uri);
									}

								}
							}
						}
						if (!flag) {
							System.out.println("not found");
						}
						br.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
				}
				for (String key : votes.keySet()) {
					LinkedList<Integer> tmp = globalVote.get(key);
					if (tmp == null) {
						tmp = new LinkedList<Integer>();
					}
					tmp.add(votes.get(key));
					globalVote.put(key, tmp);
				}

				for (String key : rankings.keySet()) {
					LinkedList<Integer> tmp = globalRank.get(key);
					if (tmp == null) {
						tmp = new LinkedList<Integer>();
					}
					tmp.add(rankings.get(key));
					globalRank.put(key, tmp);
				}
			}
		}

		try {
			BufferedWriter voteWriter = IOHelper.openWriteFile(path
					+ "vote.tsv");
			BufferedWriter rankWriter = IOHelper.openWriteFile(path
					+ "rank.tsv");

			for (String key : globalVote.keySet()) {
				LinkedList<Integer> tmp = globalVote.get(key);
				for (Integer vote : tmp) {
					key = key.concat("\t" + vote);
				}

				voteWriter.write(key + "\n");
			}

			voteWriter.flush();
			voteWriter.close();
			for (String key : globalRank.keySet()) {
				LinkedList<Integer> tmp = globalRank.get(key);
				for (Integer rank : tmp) {
					key = key.concat("\t" + rank);
				}
				rankWriter.write(key + "\n");
			}
			rankWriter.flush();
			rankWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done");
	}
}
