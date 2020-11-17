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
		System.out.println("a: "+a);
		a=new BigInteger("36486017791378609133135453651544745583908701932604171757325000294236012445885989056153579344494292093547059530500035961596075243066248870696174266635872254508959089639391896718353325360111926477994831918057029006526489833308083070914665860626720334849893400536797332583070678236185580789355277838560845430490");
		BigInteger A = g.modPow(a, p);
		// Enviar para o Professor
		String A_hexadecimal = A.toString(16);
		System.out.println("A: "+A_hexadecimal);
		
		// Passo 2
		//B recebido do professor
	//	String B_hexadecimal = "00ABBD2BCCB8CFF59B9F10D7F785519ED3AA0E68D00CEBCE587C8ED93A605913B9572418BFDE3D26E1365C75E6040880D880B2960F92597ECC57DED31932AA2099132998A461DE381AAD88A825D1167F89254C828198516F10923CF45986015C6EC9CD43D476D2556BAB70A5204E349EB3ACA232D83397C1EFCD0D452978060D81";
		//Eduardo
		String B_hexadecimal ="3786ab2e45fee25bf5aeeab471f0f4577da9e82bdd93c9288c012a72b247f0827e7176abbe5becb5995452503f847435d4dc41d37cece0d9458946309a349ebc443e0c915662fe1d78e8c2227baa7e76cfe1170664baa1b721fb393d57cb5a61e4afa013e45cdeb813bfe24995720192bf70a14842ddfd78cf1d87a2f49ce474";
		BigInteger B = new BigInteger(B_hexadecimal, 16);
		BigInteger V = B.modPow(a, p);

		// Passo 3

		byte[] dataBytes = V.toByteArray();

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.update(dataBytes, 0, dataBytes.length);
		byte[] mdbytes = md.digest();
		
		byte[] senha = Arrays.copyOfRange(mdbytes, 0, 16);
		
				
		// Etapa 2
		// Recebimento da mensagem
		//Professor
	//	String mensagemprofessor_hex = "582FF060EA980234D69F725492BCFBF7BD9F763637704FCB100BD8E441ACF42E5907440BCAE4EB342C8CB267CF9E0BC920DE29B93F8E72540FE4DE7401389D9007E5DEF13F251F77D7A72B8C58B0F768A6B00B1F360EB767F0F7C07794CC2DABEB3B8F338BA1412A9C46DB622CAA8683";
		//Eduardo
		String mensagemprofessor_hex ="31CCE0E356E19458F94A5049490DCD9B1D66981F71ED43F624E4A2D000248DDD";
		
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
		
		String mensagem_invertida= new StringBuilder(mensagem_textoClaro).reverse().toString();
		System.out.println(mensagem_invertida);
		
		 SecretKeySpec skeySpec2 = new SecretKeySpec(senha, "AES");

         Cipher cipher2 =Cipher.getInstance("AES/CBC/PKCS5Padding");

         cipher2.init(Cipher.ENCRYPT_MODE, skeySpec2);

         byte[] encrypted = cipher2.doFinal(mensagem_invertida.getBytes());
         
         System.out.println("encrypted string: " + byteArrayToHexString(encrypted));
		

	}
	 public static String byteArrayToHexString(byte[] b)
	    {
	        StringBuffer sb = new StringBuffer(b.length * 2);
	        for (int i = 0; i < b.length; i++) {
	            int v = b[i] & 0xff;
	            if (v < 16) {
	                sb.append('0');
	            }
	            sb.append(Integer.toHexString(v));
	        }
	        return sb.toString().toUpperCase();
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
