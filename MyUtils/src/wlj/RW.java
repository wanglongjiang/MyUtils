package wlj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class RW {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// 线程池
		final ExecutorService exec = Executors.newFixedThreadPool(5);
		// 两个缓冲区放到待读取队列
		final BlockingDeque<Buffer> readQue = new LinkedBlockingDeque<>(2);
		readQue.put(new Buffer());
		readQue.put(new Buffer());
		final BlockingDeque<Buffer> writeQue = new LinkedBlockingDeque<>(2);
		// 读取完毕哨兵
		final Buffer finish = new Buffer();
		// 读者
		Runnable read = new Runnable() {
			@Override
			public void run() {
				try (FileInputStream in = new FileInputStream("d:/a.txt")) {
					Buffer buffer = readQue.take();
					while ((buffer.size = in.read(buffer.buff)) != -1) {
						writeQue.put(buffer);
						buffer = readQue.take();
					}
					writeQue.put(finish);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		exec.submit(read);
		// 写者
		Runnable writer = new Runnable() {
			@Override
			public void run() {
				try (FileOutputStream out = new FileOutputStream("d:/b.txt")) {
					Buffer buffer = writeQue.take();
					do {
						out.write(buffer.buff, 0, buffer.size);
						readQue.put(buffer);
					} while ((buffer = writeQue.take()) != finish);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		exec.submit(writer);
		exec.shutdown();
	}

	static class Buffer {
		byte[] buff = new byte[4096];
		int size = 0;
	}
}
