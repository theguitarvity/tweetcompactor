package br.ufms.facom.edpoo.tt_search_compress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import edu.princeton.cs.algs4.*;

public class CompacHuffman {
	
	static String compac;
	
	// alphabet size of extended ASCII
	private static final int R = 256;

	public CompacHuffman(String s) {
		this.compac = s;
	}

	private static class Node implements Comparable<Node> {
		private final char ch;
		private final int freq;
		private final Node left, right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		// is the node a leaf node?
		private boolean isLeaf() {
			assert ((left == null) && (right == null)) || ((left != null) && (right != null));
			return (left == null) && (right == null);
		}

		// compare, based on frequency
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}
	}

	/**
	 * Reads a sequence of 8-bit bytes from standard input; compresses them using
	 * Huffman codes with an 8-bit alphabet; and writes the results to standard
	 * output.
	 */
	public static void compress(File file) {
		// read the input
		String s = compac;
		char[] input = s.toCharArray();

		// tabulate frequency counts
		int[] freq = new int[R];
		for (int i = 0; i < input.length; i++) {
			if(input[i]<256)
				freq[input[i]]++;
		}

		// build Huffman trie
		Node root = buildTrie(freq);

		// build code table
		String[] st = new String[R];
		buildCode(st, root, "");

		// print trie for decoder
		writeTrie(root);

		// print number of bytes in original uncompressed message
		BinaryStdOut.write(input.length);
		try {
			OutputStream out = new FileOutputStream(file);
			StringBuilder saida = new StringBuilder();
			BinaryOut out2 = new BinaryOut(file.getAbsolutePath());
			// use Huffman code to encode input
			for (int i = 0; i < input.length; i++) {
				String code = "";
				try {
					code = st[input[i]];
				}
				catch (Exception e) {
					// TODO: handle exception
					code = "0";
				}
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '0') {
						//BinaryStdOut.write(false);
						out2.write(false);
						//saida.append(0);
					} else if (code.charAt(j) == '1') {
						//BinaryStdOut.write(true);
						out2.write(true);
						
						//saida.append(1);
			
					} else
						throw new IllegalStateException("Illegal state");
				}
				//out2.close();
				/*BinaryIn in = new BinaryIn(saida.toString());
				BinaryOut out = new BinaryOut(file.getAbsolutePath());
				while(!in.isEmpty()) {
					out.write(in.readInt());
				}
				out.close();*/
				
			}
			out2.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		

		

		// close output stream
		BinaryStdOut.close();
	}

	// build the Huffman trie given frequencies
	private static Node buildTrie(int[] freq) {

		// initialze priority queue with singleton trees
		MinPQ<Node> pq = new MinPQ<Node>();
		for (char i = 0; i < R; i++)
			if (freq[i] > 0)
				pq.insert(new Node(i, freq[i], null, null));

		// special case in case there is only one character with a nonzero frequency
		if (pq.size() == 1) {
			if (freq['\0'] == 0)
				pq.insert(new Node('\0', 0, null, null));
			else
				pq.insert(new Node('\1', 0, null, null));
		}

		// merge two smallest trees
		while (pq.size() > 1) {
			Node left = pq.delMin();
			Node right = pq.delMin();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	// write bitstring-encoded trie to standard output
	private static void writeTrie(Node x) {
		if (x.isLeaf()) {
			BinaryStdOut.write(true);
			BinaryStdOut.write(x.ch, 8);
			return;
		}
		BinaryStdOut.write(false);
		writeTrie(x.left);
		writeTrie(x.right);
	}

	// make a lookup table from symbols and their encodings
	private static void buildCode(String[] st, Node x, String s) {
		if (!x.isLeaf()) {
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		} else {
			st[x.ch] = s;
		}
	}

	/**
	 * Reads a sequence of bits that represents a Huffman-compressed message from
	 * standard input; expands them; and writes the results to standard output.
	 */
	public static void expand() {

		// read in Huffman trie from input stream
		Node root = readTrie();

		// number of bytes to write
		int length = BinaryStdIn.readInt();

		// decode using the Huffman trie
		for (int i = 0; i < length; i++) {
			Node x = root;
			while (!x.isLeaf()) {
				boolean bit = BinaryStdIn.readBoolean();
				if (bit)
					x = x.right;
				else
					x = x.left;
			}
			BinaryStdOut.write(x.ch, 8);
		}
		BinaryStdOut.close();
	}
	private static String buildCodeS(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
            //StdOut.println(x.ch + " = " + s);
            return s;
        }
        return null;
    }
	public static String returnCode() {
        // read the input
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        String saida = buildCodeS(st, root, "");
        return saida;

    }
	private static Node readTrie() {
		boolean isLeaf = BinaryStdIn.readBoolean();
		if (isLeaf) {
			return new Node(BinaryStdIn.readChar(), -1, null, null);
		} else {
			return new Node('\0', -1, readTrie(), readTrie());
		}
	}

}

