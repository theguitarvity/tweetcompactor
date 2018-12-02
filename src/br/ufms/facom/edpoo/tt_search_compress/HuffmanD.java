package br.ufms.facom.edpoo.tt_search_compress;

import java.util.PriorityQueue;
import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class HuffmanD {

	private static class NoTrie implements Comparable<NoTrie> {
		char simbolo;
		int frequencia;
		NoTrie esq;
		NoTrie dir;

		NoTrie(char simbolo) {
			this.simbolo = simbolo;
		}

		NoTrie(char simbolo, int frequencia) {
			this.simbolo = simbolo;
			this.frequencia = frequencia;
		}

		NoTrie(int frequencia, NoTrie esq, NoTrie dir) {
			this.frequencia = frequencia;
			this.esq = esq;
			this.dir = dir;
		}

		NoTrie(NoTrie esq, NoTrie dir) {
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

	public static void expansor(BinaryIn in) {
		NoTrie trie = leTrie(in);
		int n = in.readInt();
		for (int i = 0; i < n; ++i) {
			NoTrie no = trie;
			do {
				if (in.readBoolean())
					no = no.dir;
				else
					no = no.esq;
			} while (!no.ehFolha());
			BinaryStdOut.write(no.simbolo);
		}
		BinaryStdOut.close();
	}

	private static NoTrie leTrie(BinaryIn in) {
		if (in.readBoolean())
			return new NoTrie(in.readChar());
		else
			return new NoTrie(leTrie(in), leTrie(in));
	}
}
