/*
   Programa para decifrar uma mensagem passada como argumento.
   Usa o AES sem o modo de operação.
 
   Não existe consistência no programa.
 
   Autor: Avelino F. Zorzo
   Data: 10.09.2018
 
 */

import java.security.*;
   import javax.crypto.*;
   import javax.crypto.spec.*;
   import java.util.Arrays;

 //  import javax.xml.bind.DatatypeConverter;

   public class AESDecipher {

      // Funcão para converter um array de bytes para uma
      // String em hexadecimal
  
      public static String toHexString(byte[] array) {
        //return DatatypeConverter.printHexBinary(array);
        return HexToString.byteArrayToHexString(array);
      }
 
      // Função para converter uma String em hexadecimal para
      // um array de bytes
      public static byte[] toByteArray(String s) {
        //return DatatypeConverter.parseHexBinary(s);
        return HexToString.hexStringToByteArray(s);
      }
       
       // Gera uma chave a partir de uma String.
       // Retorna a chave secreta a partir dos 16 bytes da função hash
       // aplicada sobre a string.
       public static SecretKeySpec getSecretKey (String passwd) throws Exception {
           
           passwd = passwd + "sal";
        
           byte[] dataBytes = passwd.getBytes();
           
           MessageDigest md = MessageDigest.getInstance("SHA-256");
           
           md.update(dataBytes, 0, passwd.length());
           byte[] mdbytes = md.digest();
           
           return new SecretKeySpec(Arrays.copyOfRange(mdbytes,0,16), "AES");
       }
       
     // Recebe dois parâmetros: senha e uma string para ser decifrada com a senha
     public static void main(String[] args) throws Exception {

         SecretKeySpec skeySpec = getSecretKey(args[0]);

         Cipher cipher = Cipher.getInstance("AES");

         cipher.init(Cipher.DECRYPT_MODE, skeySpec);

         byte[] deciphered = cipher.doFinal(toByteArray(args[1]));
         
         System.out.println("Mensagem cifrada: " + (new String(deciphered)));
     }
}
