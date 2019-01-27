package com.drkiettran.tika.text;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.ReadingTextManager;

public class ReadingTextManagerTest {
	public static final Logger logger = LoggerFactory.getLogger(ReadingTextManagerTest.class);
	public static final String TEXT = "Relevant guidance issued but not yet effective as of March 1, 2018 but "
			+ "becoming effective for fiscal years ending on or before June 30, 2018 is also presented directly "
			+ "in the text of the guide, but it is shaded gray and accompanied by a footnote indicating the "
			+ "effective date of the new guidance. In addition, because of the significance of the changes, "
			+ "relevant guidance for FASB Accounting Standards Update (ASU) No. 2016-14, "
			+ "Presentation of Financial Statements for Not-for-Profit Entities (Topic 958), "
			+ "is also included as shaded text within the guide, even though the amendments in "
			+ "FASB ASU No. 2016-14 are effective for annual financial statements issued for fiscal years "
			+ "beginning after December 15, 2017 (for example, years ending December 31, 2018 and years "
			+ "ending June 30, 2019), and for interim periods within fiscal years beginning after December 15, 2018. "
			+ "Limited guidance from FASB ASU No. 2014-09, Revenue from Contracts with Customers (Topic 606), "
			+ "appears as shaded text, primarily within chapter 12, “Revenues and Receivables From Exchange "
			+ "Transactions,” to help readers prepare for the effective date of those amendments, which "
			+ "for most NFPs is annual reporting periods beginning after December 15, 2018, and interim "
			+ "periods within annual periods beginning after December 15, 2019, with a year earlier application "
			+ "required for those that have issued, or are conduit bond obligors for, securities that are traded, "
			+ "listed, or quoted on an exchange or an over-the-counter market, The distinct presentation of this "
			+ "content is intended to aid the reader in differentiating content that may not be effective for the "
			+ "reader’s purposes (as part of the guide’s “dual guidance” treatment of applicable new guidance).";

	/**
	 * [Relevant, guidance, issued, but, not, yet, effective, as, of, March, 1,,
	 * 2018, but, becoming, effective, for, fiscal, years, ending, on, or, before,
	 * June, 30,, 2018, is, also, presented, directly, in, the, text, of, the,
	 * guide,, but, it, is, shaded, gray, and, accompanied, by, a, footnote,
	 * indicating, the, effective, date, of, the, new, guidance., In, addition,,
	 * because, of, the, significance, of, the, changes,, relevant, guidance, for,
	 * FASB, Accounting, Standards, Update, (ASU), No., 2016-14,, Presentation, of,
	 * Financial, Statements, for, Not-for-Profit, Entities, (Topic, 958),, is,
	 * also, included, as, shaded, text, within, the, guide,, even, though, the,
	 * amendments, in, FASB, ASU, No., 2016-14, are, effective, for, annual,
	 * financial, statements, issued, for, fiscal, years, beginning, after,
	 * December, 15,, 2017, (for, example,, years, ending, December, 31,, 2018, and,
	 * years, ending, June, 30,, 2019),, and, for, interim, periods, within, fiscal,
	 * years, beginning, after, December, 15,, 2018., Limited, guidance, from, FASB,
	 * ASU, No., 2014-09,, Revenue, from, Contracts, with, Customers, (Topic, 606),,
	 * appears, as, shaded, text,, primarily, within, chapter, 12,, “Revenues, and,
	 * Receivables, From, Exchange, Transactions,”, to, help, readers, prepare, for,
	 * the, effective, date, of, those, amendments,, which, for, most, NFPs, is,
	 * annual, reporting, periods, beginning, after, December, 15,, 2018,, and,
	 * interim, periods, within, annual, periods, beginning, after, December, 15,,
	 * 2019,, with, a, year, earlier, application, required, for, those, that, have,
	 * issued,, or, are, conduit, bond, obligors, for,, securities, that, are,
	 * traded,, listed,, or, quoted, on, an, exchange, or, an, over-the-counter,
	 * market,, The, distinct, presentation, of, this, content, is, intended, to,
	 * aid, the, reader, in, differentiating, content, that, may, not, be,
	 * effective, for, the, reader’s, purposes, (as, part, of, the, guide’s, “dual,
	 * guidance”, treatment, of, applicable, new, guidance).]
	 * 
	 * 
	 * 
	 */
	public static final int EXPECTED_WORD_COUNT = 272;

	@Test
	public void shouldGetWordsToReadCorrectly() {
		ReadingTextManager rtm = new ReadingTextManager(TEXT);
		assertThat(rtm.getNumberOfWords(), equalTo(EXPECTED_WORD_COUNT));
		logger.info("words: {}", rtm.getWords());
	}
}
