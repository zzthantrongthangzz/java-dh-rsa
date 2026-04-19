public class RSA {
    private long p, q, n, phi, e, d;

    public RSA(long baseId) {
        this(MathUtils.prevPrime(baseId), MathUtils.nextPrime(baseId), 35, true);
    }

    public RSA(long p, long q, long initialE, boolean autoFixE) {
        if (!MathUtils.isPrime(p) || !MathUtils.isPrime(q)) {
            throw new IllegalArgumentException("p và q BẮT BUỘC phải là số nguyên tố!");
        }

        this.p = p;
        this.q = q;
        this.n = this.p * this.q;
        this.phi = (this.p - 1) * (this.q - 1);

        this.e = initialE;

        if (autoFixE) {
            // Tự động tăng e cho đến khi nguyên tố cùng nhau với phi
            while (MathUtils.gcd(this.e, this.phi) != 1) {
                this.e++;
            }
        } else {
            // Cố tình bỏ qua kiểm tra, có thể ném lỗi khi tính nghịch đảo
            if (MathUtils.gcd(this.e, this.phi) != 1) {
                throw new IllegalArgumentException("e không hợp lệ (gcd(e, phi) != 1)");
            }
        }

        // Tính d: nghịch đảo modulo của e mod phi
        this.d = MathUtils.modInverse(this.e, this.phi);
    }

    public void printKeys() {
        System.out.println("=== KHÓA RSA ===");
        System.out.println("Public Key (e, n) = (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n) = (" + d + ", " + n + ")");
    }

    public long[] encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return new long[0];
        }

        long[] ciphertext = new long[plaintext.length()];
        for (int i = 0; i < plaintext.length(); i++) {
            char ch = plaintext.charAt(i);
            // Giới hạn chỉ mã hóa ký tự ASCII chuẩn (từ 32 đến 126)
            if (ch < 32 || ch > 126) {
                throw new IllegalArgumentException("Lỗi: Chứa ký tự không hợp lệ (" + ch + ")");
            }
            long asciiVal = (long) ch;
            // c = m^e mod n
            ciphertext[i] = MathUtils.powerMod(asciiVal, e, n);
        }
        return ciphertext;
    }

    public String decrypt(long[] ciphertext) {
        if (ciphertext == null || ciphertext.length == 0) {
            return "";
        }

        StringBuilder plaintext = new StringBuilder();
        for (long cipherChar : ciphertext) {
            // m = c^d mod n
            long asciiVal = MathUtils.powerMod(cipherChar, d, n);
            plaintext.append((char) asciiVal);
        }
        return plaintext.toString();
    }

    // Hỗ trợ Test Case 4: Giả mạo khóa d
    public void setFakePrivateKey() {
        this.d = this.d + 1;
    }
}