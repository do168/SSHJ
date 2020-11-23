package sshj.sshj.component;

import sshj.sshj.model.RequestThreadData;

public class ThreadRepository {
	private static ThreadLocal<RequestThreadData> threadLocal = new ThreadLocal<RequestThreadData>() {
		@Override
		protected RequestThreadData initialValue() {
			return new RequestThreadData();
		};
	};

	public static RequestThreadData getThreadData() {
		return threadLocal.get();
	}

	public static void remove() {
		threadLocal.remove();
	}
}