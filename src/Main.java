import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Họ tên: Thân Trọng Thắng | Lớp: DHKTPM19B");

        long mssv = 23729371;
        String major = "KTPM";
        String originalString = mssv + "-" + major;

        // DIFFIE-HELLMAN
        DiffieHellman dh = new DiffieHellman(mssv, major);
        dh.executeKeyExchange();

        System.out.println("CHẠY 6 TEST CASES CHO RSA");

        runTestCases(mssv, originalString);
    }

    private static void runTestCases(long mssv, String plainText) {
        // Test 1: Hợp lệ
        try {
            System.out.println("\n[TEST 1] Trường hợp hợp lệ -> Mã hóa và giải mã chuẩn");
            RSA rsa1 = new RSA(mssv);
            rsa1.printKeys();

            long[] encrypted = rsa1.encrypt(plainText);
            System.out.println("Ciphertext: " + Arrays.toString(encrypted));

            String decrypted = rsa1.decrypt(encrypted);
            System.out.println("Plaintext sau giải mã: " + decrypted);

            if (plainText.equals(decrypted)) {
                System.out.println("-> [PASSED] Dữ liệu khớp 100%");
            } else {
                System.out.println("-> [FAILED] Dữ liệu sai lệch");
            }
        } catch (Exception e) {
            System.out.println("-> [FAILED] Exception không mong muốn: " + e.getMessage());
        }

        // Test 2: Truyền p không phải số nguyên tố
        try {
            System.out.println("\n[TEST 2] Cố tình truyền p không phải là số nguyên tố");
            long fakeP = 23729372;
            long q = MathUtils.nextPrime(mssv);
            RSA rsa2 = new RSA(fakeP, q, 35, true);
            System.out.println("-> [FAILED] Lẽ ra phải có Exception ném ra.");
        } catch (IllegalArgumentException e) {
            System.out.println("Bắt lỗi thành công: " + e.getMessage());
            System.out.println("-> [PASSED]");
        }

        // Test 3: Truyền e không hợp lệ (gcd != 1)
        try {
            System.out.println("\n[TEST 3] Cố tình truyền e không hợp lệ, không cho tự sửa");
            long p = MathUtils.prevPrime(mssv);
            long q = MathUtils.nextPrime(mssv);
            RSA rsa3 = new RSA(p, q, 2, false);
            System.out.println("-> [FAILED] Lẽ ra phải có Exception ném ra.");
        } catch (IllegalArgumentException e) {
            System.out.println("Bắt lỗi thành công: " + e.getMessage());
            System.out.println("-> [PASSED]");
        }

        // Test 4: Giải mã bằng khóa d giả mạo
        try {
            System.out.println("\n[TEST 4] Giải mã bằng một khóa d giả mạo");
            RSA rsa4 = new RSA(mssv);
            long[] encrypted4 = rsa4.encrypt(plainText);

            // Phá hoại khóa Private
            rsa4.setFakePrivateKey();
            String badDecrypted = rsa4.decrypt(encrypted4);

            System.out.println("Dữ liệu giải mã ra: " + badDecrypted);
            if (!plainText.equals(badDecrypted)) {
                System.out.println("-> [PASSED] Kết quả giải mã sai lệch đúng như dự đoán");
            } else {
                System.out.println("-> [FAILED] Bị hack thành công, bảo mật kém!");
            }
        } catch (Exception e) {
            System.out.println("-> [FAILED] Lỗi runtime: " + e.getMessage());
        }

        // Test 5: Ký tự không hợp lệ
        try {
            System.out.println("\n[TEST 5] Mã hóa chứa ký tự không hợp lệ");
            RSA rsa5 = new RSA(mssv);
            String invalidText = plainText + "♫"; // Ký tự nằm ngoài bảng ASCII cơ bản
            rsa5.encrypt(invalidText);
            System.out.println("-> [FAILED] Không bắt được lỗi ký tự.");
        } catch (IllegalArgumentException e) {
            System.out.println("Bắt lỗi thành công: " + e.getMessage());
            System.out.println("-> [PASSED]");
        }

        // Test 6: Truyền chuỗi rỗng
        try {
            System.out.println("\n[TEST 6] Truyền vào chuỗi rỗng \"\"");
            RSA rsa6 = new RSA(mssv);
            long[] emptyEncrypted = rsa6.encrypt("");
            String emptyDecrypted = rsa6.decrypt(emptyEncrypted);

            if (emptyEncrypted.length == 0 && "".equals(emptyDecrypted)) {
                System.out.println("Hệ thống xử lý bình thường, không crash.");
                System.out.println("-> [PASSED]");
            } else {
                System.out.println("-> [FAILED] Xử lý mảng rỗng sai.");
            }
        } catch (Exception e) {
            System.out.println("-> [FAILED] Crash hệ thống: " + e.getMessage());
        }
    }
}