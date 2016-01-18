 package com.dwl.dbtemplate.util;
 
 public class WordUtils
 {
   public static String uncapitalize(String str)
   {
     return uncapitalize(str, null);
   }
 
   public static String uncapitalize(String str, char[] delimiters)
   {
     int delimLen = delimiters == null ? -1 : delimiters.length;
     if ((str == null) || (str.length() == 0) || (delimLen == 0)) {
       return str;
     }
     char[] buffer = str.toCharArray();
     boolean uncapitalizeNext = true;
     for (int i = 0; i < buffer.length; i++) {
       char ch = buffer[i];
       if (isDelimiter(ch, delimiters)) {
         uncapitalizeNext = true;
       } else if (uncapitalizeNext) {
         buffer[i] = Character.toLowerCase(ch);
         uncapitalizeNext = false;
       }
     }
     return new String(buffer);
   }
 
   public static String capitalize(String str)
   {
     return capitalize(str, null);
   }
 
   public static String capitalize(String str, char[] delimiters)
   {
     int delimLen = delimiters == null ? -1 : delimiters.length;
     if ((str == null) || (str.length() == 0) || (delimLen == 0)) {
       return str;
     }
     char[] buffer = str.toCharArray();
     boolean capitalizeNext = true;
     for (int i = 0; i < buffer.length; i++) {
       char ch = buffer[i];
       if (isDelimiter(ch, delimiters)) {
         capitalizeNext = true;
       } else if (capitalizeNext) {
         buffer[i] = Character.toTitleCase(ch);
         capitalizeNext = false;
       }
     }
     return new String(buffer);
   }
 
   private static boolean isDelimiter(char ch, char[] delimiters)
   {
     if (delimiters == null) {
       return Character.isWhitespace(ch);
     }
     for (char delimiter : delimiters) {
       if (ch == delimiter) {
         return true;
       }
     }
     return false;
   }
 }




