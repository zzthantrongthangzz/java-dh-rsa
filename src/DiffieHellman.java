public class DiffieHellman {
    private long p; // Số nguyên tố
    private long g; // Căn nguyên thủy
    private long a; // Private Key của A
    private long b; // Private Key của B

    public DiffieHellman(long mssv, String major) {
        this.p = MathUtils.nextPrime(mssv);
        this.g = (mssv % 10) + 2;
        this.a = calculateDigitSum(mssv);
        this.b = major.length() * 3L;

        System.out.println("THÔNG SỐ DIFFIE-HELLMAN");
        System.out.println("p (Prime > " + mssv + ") = " + p);
        System.out.println("g (Generator) = " + g);
        System.out.println("a (Private Key A) = " + a);
        System.out.println("b (Private Key B) = " + b);
    }

    private long calculateDigitSum(long number) {
        long sum = 0;
        long temp = number;
        while (temp > 0) {
            sum += temp % 10;
            temp /= 10;
        }
        return sum;
    }

    public void executeKeyExchange() {
        System.out.println("\nQUÁ TRÌNH TRAO ĐỔI KHÓA");

        // Tính Public Key
        long publicKeyA = MathUtils.powerMod(g, a, p);
        long publicKeyB = MathUtils.powerMod(g, b, p);

        System.out.println("Public Key A (A = g^a mod p) = " + publicKeyA);
        System.out.println("Public Key B (B = g^b mod p) = " + publicKeyB);

        // Tính Shared Secret
        long secretKeyA = MathUtils.powerMod(publicKeyB, a, p);
        long secretKeyB = MathUtils.powerMod(publicKeyA, b, p);

        System.out.println("\nShared Secret tính bởi A = " + secretKeyA);
        System.out.println("Shared Secret tính bởi B = " + secretKeyB);

        if (secretKeyA == secretKeyB) {
            System.out.println("=> Khớp mã bí mật chung thành công: " + secretKeyA);
        } else {
            System.out.println("=> Lỗi: Khóa bí mật không khớp!");
        }
    }
}