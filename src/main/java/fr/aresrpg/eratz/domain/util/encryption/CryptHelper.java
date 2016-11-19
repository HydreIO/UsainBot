package fr.aresrpg.eratz.domain.util.encryption;

public class CryptHelper {
    private static final char[] hash = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

    public static String cryptIp(String ip) {
        String[] split = ip.split("\\.");
        String Encrypted = "";
        int Count = 0;
        for (int i = 0; i < 50; i++) {
            for (int o = 0; o < 50; o++) {
                if (((i & 15) << 4 | o & 15) == Integer.parseInt(split[Count])) {
                    Character A = (char) (i + 48);
                    Character B = (char) (o + 48);
                    Encrypted += A.toString() + B.toString();
                    i = 0;
                    o = 0;
                    Count++;
                    if (Count == 4)
                        return Encrypted;
                }
            }
        }
        return "DD";
    }

    public static String decryptIp(String ipCrypt) {
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int d1 = ipCrypt.charAt(i) - 48;
            i++;
            int d2 = ipCrypt.charAt(i) - 48;
            String d3 = Integer.toString((d1 & 15) << 4 | d2 & 15);
            ip.append(d3);

            if (i < 7) ip.append('.');
        }
        return ip.toString();
    }

    public static String cryptPort(int port) {
        int P = port;
        String nbr64 = "";
        for (int a = 2; a >= 0; a--) {
            nbr64 += hash[(int) (P / (java.lang.Math.pow(64, a)))];
            P = (P % (int) (java.lang.Math.pow(64, a)));
        }
        return nbr64;
    }

    public static int decryptPort(char[] chars) {
        if (chars.length != 3)
            throw new IllegalArgumentException("Port must be encrypted in 3 chars");
        int port = 0;
        for (int i = 0; i < 2; i++)
            port += (int) (Math.pow(64, 2 - i) * indexOfHash(chars[i]));
        port += indexOfHash(chars[2]);
        return port;
    }

    public static String cryptCellId(int cellId) {
        int char1 = cellId / 64;
        int char2 = cellId % 64;
        return hash[char1] + "" + hash[char2];
    }

    public static int decryptCellId(String cellCode) {
        char char1 = cellCode.charAt(0);
        char char2 = cellCode.charAt(1);
        int code1 = 0, code2 = 0, a = 0;
        while (a < hash.length) {
            if (hash[a] == char1) {
                code1 = a * 64;
            }
            if (hash[a] == char2) {
                code2 = a;
            }
            a++;
        }
        return code1 + code2;
    }

    private static int indexOfHash(char ch) {
        for (int i = 0; i < hash.length; i++)
            if (hash[i] == ch)
                return i;
        return -1;
    }
}
