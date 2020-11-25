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

//Nome: Christian Schmidt
public class Game {

	public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		// Etapa 1
		// Passo 1

		// p e g do enunciado
		String p_hexadecimal = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23"
				+ "B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD709848"
				+ "8E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371";

		String g_hexadecimal = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
				+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
				+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
				+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
				+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5";

		// Converte o p e o g de hexadecimal string para BigInteger
		BigInteger p = new BigInteger(p_hexadecimal, 16);
		BigInteger g = new BigInteger(g_hexadecimal, 16);

		// Sorteia um BigInteger de tamanho inferior ao p
		Random r = new Random();
		BigInteger a = new BigInteger(p.bitLength() - 1, r);

		

		// O a sorteado foi este que esta em baixo. Para evitar que ele gere novos cada
		// vez eu fixei o valor.
		a = new BigInteger(
				"36486017791378609133135453651544745583908701932604171757325000294236012445885989056153579344494292093547059530500035961596075243066248870696174266635872254508959089639391896718353325360111926477994831918057029006526489833308083070914665860626720334849893400536797332583070678236185580789355277838560845430490");
		
		// System.out.println("a: "+a);
		
		// Calculo do A
		BigInteger A = g.modPow(a, p);

		// Enviar para o Professor
		String A_hexadecimal = A.toString(16);
		System.out.println("A: " + A_hexadecimal);
		// A que sera enviado ao
		// professor:1a94028038bea83a9757082c7781783e2714d498d24f2dddfa42bd5187f54f3c7ea725a22674a65b1f5d2ec754a13271d30670b596e5f793faa6cd4fb0f8469a9a01990bbcaf8b719f4145f8c0f8d672deb3de98d72831fcdad116b815b71de6fb1eff1938a9b42cad95464194b80ded8cec1670e8826339b63c5464ba27a191

		// Passo 2
		// B recebido do professor
		String B_hexadecimal = "00ABBD2BCCB8CFF59B9F10D7F785519ED3AA0E68D00CEBCE587C8ED93A605913B9572418BFDE3D26E1365C75E6040880D880B2960F92597ECC57DED31932AA2099132998A461DE381AAD88A825D1167F89254C828198516F10923CF45986015C6EC9CD43D476D2556BAB70A5204E349EB3ACA232D83397C1EFCD0D452978060D81";
		// Converte B para BigInteger
		BigInteger B = new BigInteger(B_hexadecimal, 16);
		// Gera V
		BigInteger V = B.modPow(a, p);

		// Passo 3
		// Calculo do SHA-256
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

		// Senha para se comunicar com os primeiros 128 bits
		byte[] senha = Arrays.copyOfRange(mdbytes, 0, 16);

		// Etapa 2
		// Recebimento da mensagem do professor

		// Mensagem 1
		String mensagemprofessor_hex = "582FF060EA980234D69F725492BCFBF7BD9F763637704FCB100BD8E441ACF42E5907440BCAE4EB342C8CB267CF9E0BC920DE29B93F8E72540FE4DE7401389D9007E5DEF13F251F77D7A72B8C58B0F768A6B00B1F360EB767F0F7C07794CC2DABEB3B8F338BA1412A9C46DB622CAA8683";
		// Mensagem Traduzida: Se não conseguir decifrar, não vai ler. Se leu, inverte,
		// cifra e manda de volta.

		// Mensagem 2
		//	String mensagemprofessor_hex = "2BC698C03F079F0998DE9B7EEAAC56AE0EB571CEF7B2426E9045070736365F595A3BBAFA0A7A698468A35DCBE26E735BF4562F43DF788B534B87B27E25DEC4BFCB7FB8284763CAC686C3CA35444AE285DEFA0236C22B824C381F4F47F9CD0A38";
		// Mensagem Traduzida: Show. Agora comenta bem o código com este exemplo
		// completo e envia no Moodle.

		// Decifra a mensagem recebida
		byte[] hash = hexStringToByteArray(mensagemprofessor_hex);
		IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(hash, 0, 16));
		byte[] mens = Arrays.copyOfRange(hash, 16, hash.length);

		SecretKeySpec skeySpec = new SecretKeySpec(senha, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] deciphered = cipher.doFinal(mens);

		String mensagem_textoClaro = new String(deciphered);
		System.out.println("Mensagem cifrada: " + mensagem_textoClaro);

		// Inverte a mensagem
		String mensagem_invertida = new StringBuilder(mensagem_textoClaro).reverse().toString();
		System.out.println("Mensagem invertida: " + mensagem_invertida);

		// Cria um novo IV e cifra a mensagem a ser enviada
		byte[] iv2 = new byte[16];
		new Random().nextBytes(iv2);

		IvParameterSpec ivfinal = new IvParameterSpec(iv2);

		SecretKeySpec skeySpec2 = new SecretKeySpec(senha, "AES");

		Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher2.init(Cipher.ENCRYPT_MODE, skeySpec2, ivfinal);

		byte[] encrypted = cipher2.doFinal(mensagem_invertida.getBytes());

		encrypted = joinIE(iv2, encrypted);
		System.out.println("Mensagem cifrada em hexadecimal para ser enviada: " + byteArrayToHexString(encrypted));

	}

	// Junta o iv com a mensagem
	public static byte[] joinIE(byte[] iv2, byte[] encrypted) {
		byte[] mensagem_criptografada = new byte[iv2.length + encrypted.length];
		for (int i = 0; i < iv2.length; i++) {
			mensagem_criptografada[i] = iv2[i];
		}
		for (int i = 0; i < encrypted.length; i++) {
			mensagem_criptografada[i + iv2.length] = encrypted[i];
		}
		return mensagem_criptografada;
	}

	// Converte byteArray para String de hexadecimal
	public static String byteArrayToHexString(byte[] b) {
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

	// Converte hexadecimal String para Array de Byte
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
