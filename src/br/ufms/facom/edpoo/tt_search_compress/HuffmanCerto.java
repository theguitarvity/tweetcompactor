package br.ufms.facom.edpoo.tt_search_compress;

import java.util.PriorityQueue;

import edu.princeton.cs.algs4.BinaryStdOut;

class HuffmanCerto {

	static class NoTrie implements Comparable<NoTrie> {
		char simbolo;
		int frequencia;
		NoTrie esq;
		NoTrie dir;

		NoTrie(char simbolo) {
			/* ... */
			this.simbolo = simbolo;
		}

		public NoTrie(char simbolo, int frequencia) {
			/* ... */
			this.simbolo = simbolo;
			this.frequencia = frequencia;
		}

		NoTrie(int frequencia, NoTrie esq, NoTrie dir) {
			/* ... */
			this.frequencia = frequencia;
			this.esq = esq;
			this.dir = dir;
		}

		NoTrie(NoTrie esq, NoTrie dir) {
			/* ... */
			this.esq = esq;
			this.dir = dir;
		}

		boolean ehFolha() {
			return esq == null && dir == null;
		}

		@Override
		public int compareTo(NoTrie o) {
			return this.frequencia - o.frequencia;
		}
	}

	public static void compressor(String in) {
		char[] mensagem = in.toCharArray();
		NoTrie trie = criaTrie(mensagem);
		escreveTrie(trie);
		BinaryStdOut.write(mensagem.length);
		String[] tc = criaTabelaDeCodigos(trie);
		for (char c : mensagem)
			escreveCodigo(tc[c]);
		BinaryStdOut.close();
	}

	private static void escreveCodigo(String string) {
		// TODO Auto-generated method stub

	}

	private static String[] criaTabelaDeCodigos(NoTrie trie) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void escreveTrie(NoTrie n) {
		// TODO Auto-generated method stub
		if (n.ehFolha()) {
			BinaryStdOut.write(true);
			BinaryStdOut.write(n.simbolo);
		} else {
			BinaryStdOut.write(false);
			escreveTrie(n.esq);
			escreveTrie(n.dir);
		}

	}

	private static NoTrie criaTrie(char[] mensagem) {
		// TODO Auto-generated method stub
		int[] freq = new int[256];
		for (char c : mensagem)
			++freq[c];
		PriorityQueue<NoTrie> tries = new PriorityQueue<>();
		for (char c = 0; c < 256; ++c)
			if (freq[c] > 0) {
				NoTrie n = new NoTrie(c, freq[c]);
				tries.add(n);
			}

		while (tries.size() > 1) {
			NoTrie t1 = tries.remove();
			NoTrie t2 = tries.remove();
			tries.add(new NoTrie(t1.frequencia + t2.frequencia, t1, t2));
		}
		return tries.remove();
	}
}
