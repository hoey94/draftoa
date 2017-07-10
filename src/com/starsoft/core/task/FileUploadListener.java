package com.starsoft.core.task;

import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.ProgressListener;

public class FileUploadListener implements ProgressListener {

	private long num100Ks = 0;
	private long theBytesRead = 0;
	private long theContentLength = -1;
	private int whichItem = 0;
	private int percentDone = 0;
	private boolean contentLengthKnown = false;
	private HttpSession session;

	public void update(long bytesRead, long contentLength, int items) {

		if (contentLength > -1) {
			contentLengthKnown = true;
		}
		theBytesRead = bytesRead;
		theContentLength = contentLength;
		whichItem = items;

		long nowNum100Ks = bytesRead / 100000;
		if (nowNum100Ks > num100Ks) {
			num100Ks = nowNum100Ks;
			if (contentLengthKnown) {
				percentDone = (int) Math.round(100.00 * bytesRead
						/ contentLength);
				session.setAttribute("fileuploadgo", percentDone);
			}
		}
	}

	public int getPercentDone() {
		return percentDone;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

}