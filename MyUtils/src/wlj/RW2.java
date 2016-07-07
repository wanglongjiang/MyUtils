package wlj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class RW2 {
	public static void main(String[] args) {
		// 两个缓冲区队列
		final BuffQue readQue = new BuffQue();
		readQue.put(new Buffer());
		readQue.put(new Buffer());
		final BuffQue writeQue = new BuffQue();
		// 读取完毕哨兵
		final Buffer finish = new Buffer();
		// 读者
		Thread read = new Thread(new Runnable() {
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
		});
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

		Thread readThread = new Thread(read);
		readThread.start();
		Thread writeThread = new Thread(writer);
		writeThread.start();
	}

	static class Buffer {
		byte[] buff = new byte[4096];
		int size = 0;
	}

	static class BuffQue {
		LinkedList<Buffer> que = new LinkedList<>();

		public synchronized void put(Buffer buff) {
			que.addLast(buff);
			notify();
		}

		public synchronized Buffer take() throws InterruptedException {
			while (que.isEmpty()) {
				wait();
			}
			return que.removeFirst();
		}
	}
}
