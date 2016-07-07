package wlj;

import com.baic.bcl.crypto.CryptoUtils;

public class PasswordEncrypt {

	public static void main(String[] args) {
		System.out.println(CryptoUtils.encryptOfNativeIrreversible("我Aa123456", "SHA-1"));
		System.out.println(CryptoUtils.encryptOfIrreversible("我Aa123456", "SHA-1"));
	}
}
