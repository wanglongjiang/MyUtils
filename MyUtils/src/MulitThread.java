
public class MulitThread {

	public static void main(String[] args) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写需要开另外一个线程运行的逻辑
			}
		});
		t.start();
	}

}
