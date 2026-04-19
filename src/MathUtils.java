public class MathUtils {

    // Kiểm tra số nguyên tố
    public static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    // Tìm số nguyên tố nhỏ nhất > n
    public static long nextPrime(long n) {
        long p = n + 1;
        while (!isPrime(p)) {
            p++;
        }
        return p;
    }

    // Tìm số nguyên tố lớn nhất < n
    public static long prevPrime(long n) {
        long p = n - 1;
        while (!isPrime(p)) {
            p--;
        }
        return p;
    }

    // Thuật toán nhân modulo an toàn (tránh tràn số long khi tính a * b % mod)
    public static long mulMod(long a, long b, long mod) {
        long res = 0;
        a = a % mod;
        while (b > 0) {
            if (b % 2 == 1) {
                res = (res + a) % mod;
            }
            a = (a * 2) % mod;
            b /= 2;
        }
        return res;
    }

    // Lũy thừa modulo - base^exp % mod
    public static long powerMod(long base, long exp, long mod) {
        long res = 1;
        base = base % mod;
        while (exp > 0) {
            if (exp % 2 == 1) {
                res = mulMod(res, base, mod);
            }
            base = mulMod(base, base, mod);
            exp /= 2;
        }
        return res;
    }

    // Tìm ước chung lớn nhất
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Thuật toán Euclid mở rộng
    // Trả về mảng [gcd, x, y] thỏa mãn: a*x + b*y = gcd
    public static long[] extendedGCD(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] vals = extendedGCD(b, a % b);
        long gcd = vals[0];
        long x1 = vals[1];
        long y1 = vals[2];
        return new long[]{gcd, y1, x1 - (a / b) * y1};
    }

    // Nghịch đảo modulo - Tìm d cho (a * d) % m = 1
    public static long modInverse(long a, long m) throws IllegalArgumentException {
        long[] res = extendedGCD(a, m);
        if (res[0] != 1) {
            throw new IllegalArgumentException("Không tồn tại nghịch đảo modulo (Hai số không nguyên tố cùng nhau)");
        }
        long inv = res[1] % m;
        if (inv < 0) {
            inv += m; // Đảm bảo kết quả luôn dương
        }
        return inv;
    }
}