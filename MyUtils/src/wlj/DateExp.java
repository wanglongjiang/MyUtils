package wlj;

import java.util.Date;

import com.baic.bcl.util.DateUtils;

public class DateExp {

	public static void main(String[] args) {
		Date end = DateUtils.toDate("2015-09-24");
		Date start = DateUtils.toDate("2014-01-02");
		for (Date d = start; d.compareTo(end) <= 0; d = DateUtils.addDays(d, 7)) {
			System.out.println(DateUtils.toStr(d));
		}
	}

}
