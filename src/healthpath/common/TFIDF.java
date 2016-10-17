package healthpath.common;

public class TFIDF {

	/**
	 * @param local_term_count will be count of times a term appeared in a given
	 *            _Person's Rx claimline
	 * @param local_doc_size is the size of a given Person's Rx claimline
	 * @return term frequency of local_term_count in the claimline
	 */
	public double tf(double local_term_count, double local_doc_size) {
		double result = local_term_count / local_doc_size;
		return result;
	}

	/**
	 * @param total_number_of_docs
	 *            double number of claimlines
	 * @param total_number_this_term
	 *            Total count of this term in all the claimlines
	 * @return the inverse term frequency of term in documents
	 */
	public double idf(double total_number_of_docs, double total_number_this_term) {
		double result = Math.log(total_number_of_docs / total_number_this_term);
		return result;
	}

	public double getTFIDF(double local_term_count, double local_doc_size, double total_number_of_docs,
			double total_number_this_term) {
		double localClaimLine = tf(local_term_count, local_doc_size);
		double globalClaimLineInformation = idf(total_number_of_docs, total_number_this_term);
		return localClaimLine * globalClaimLineInformation;
	}
}
