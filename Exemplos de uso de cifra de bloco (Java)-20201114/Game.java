import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Game {

	public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Etapa 1
		// Passo 1
		String p_hexadecimal = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23"
				+ "B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD709848"
				+ "8E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371";

		String g_hexadecimal = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
				+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
				+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
				+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
				+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5";

		BigInteger p = new BigInteger(p_hexadecimal, 16);
		BigInteger g = new BigInteger(g_hexadecimal, 16);
		Random r = new Random();
		BigInteger a = new BigInteger(p.bitLength() - 1, r);
		BigInteger A = g.modPow(a, p);
		// Enviar para o Professor
		String A_hexadecimal = A.toString(16);
	//	System.out.println(A_hexadecimal);
		
		// Passo 2
		//B recebido do professor
		String B_hexadecimal = "3878171495B549C5F40162B5514106546A346EC9552AE3F0F28B8F46B409E9B94EDFCD432EF8A60602CA126C7E6874EB4333EFBA26B9786020492B4D7A4F8FEE74E91504939BA2895C97EAB1698CB42BD506025975E851F5118B8E486293FB395E534116F51603C853BFB475C1F339D47C5FDCC94261147A1FE6D743B626CC32";
		BigInteger B = new BigInteger(B_hexadecimal, 16);
		BigInteger V = B.modPow(a, p);

		// Passo 3

		byte[] dataBytes = V.toString().getBytes();

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(dataBytes, 0, V.toString().length());
		byte[] mdbytes = md.digest();
		
		byte[] senha = Arrays.copyOfRange(mdbytes, 0, 16);
		
				
		// Etapa 2
		// Recebimento da mensagem
		String mensagemprofessor_hex = "E62968DB1C564B045C9628CDDD2445B70C3F38C9AC1954D6D66C38D71566C61DB1D5B0394E1822C251F08E928C0F855A6B3D6D955B835BFD0986D52B5103855646F2CF54F8FA9E16723F581520B68AA4F01CBDEBBCA1C8DBE96657734E6190FFDF981064D3DA58DAECBDC5734CD07E10";
		byte[] hash = hexStringToByteArray(mensagemprofessor_hex);
		IvParameterSpec iv =new IvParameterSpec(Arrays.copyOfRange(hash, 0, 16));
		byte[] mens = Arrays.copyOfRange(hash, 16, hash.length);

		SecretKeySpec skeySpec = new SecretKeySpec(senha, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] deciphered = cipher.doFinal(mens);

		String mensagem_textoClaro = new String(deciphered);
		System.out.println("Mensagem cifrada: " + mensagem_textoClaro);
		
		//Enviar Nova mensagem
		
		

	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
