package gameEngine.Tool;

import gameEngine.gameStart.multiDimensional.Matrix;
import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.gameStart.multiDimensional.objects.Vector;

import java.util.Arrays;

public final class MathUtil {
    // 组合数最大 n 值
    private static final int MAX_N = 10;
    // 声明一个二维数组来存储组合数
    private static final long[][] dp = new long[MAX_N + 1][MAX_N + 1];

    // 使用动态规划预计算组合数
    static {
        // 初始化组合数数组
        for (int n = 0; n <= MAX_N; n++) {
            dp[n][0] = 1; // C(n, 0) = 1
            dp[n][n] = 1; // C(n, n) = 1
        }

        // 填充组合数数组
        for (int n = 1; n <= MAX_N; n++) {
            for (int k = 1; k < n; k++) {
                dp[n][k] = dp[n - 1][k - 1] + dp[n - 1][k];
            }
        }
    }

    // 获取组合数 C(n, k)
    public static long C(int n, int k) {
        if (n < 0 || n > MAX_N || k < 0 || k > n) {
            throw new IllegalArgumentException("Invalid values for n or k.");
        }
        return dp[n][k];
    }

    //正规化 化为单位向量
    public static void normalization(Vector vector) {
        double sum = 0;
        for (int i = 0; i < vector.size(); i++) {
            sum += vector.get(i) * vector.get(i);
        }
        sum = Math.sqrt(sum);
        if (sum == 1) return;
        for (int i = 0; i < vector.size(); i++) {
            vector.set(i, vector.get(i) / sum);
        }
    }

    //点point的坐标加n倍的vector p+n*v
    public static double[] move(double[] point,double n, double[] vector) {
        if (point.length != vector.length) {
            throw new IllegalArgumentException("两个点的维度不同:\n" + Arrays.toString(point) + "\n" + Arrays.toString(vector));
        }
        double[] coordinates = new double[point.length];
        for (int i = 0; i < point.length; i++) {
            coordinates[i] = point[i] + n*vector[i];
        }
        return coordinates;
    }

    //将点point旋转到以matrix为基坐标的坐标系 即求Mx = p中的x
    public static double[] revolve(double[] point, Matrix matrix) {
        //LU分解
        Matrix[] matrices = LUDecomposition(matrix);
        Matrix L = matrices[0]; // 下三角矩阵
        Matrix U = matrices[1]; // 上三角矩阵
        //Ly = p
        double[] y = forwardSubstitution(L, point);
        //Ux = p
        double[] x = backwardSubstitution(U, y);
        return x;
    }

    /**
     * 以center为圆心，vector为轴，将point逆时针旋转a度(四维的选两个向量形成一个平面实现按照平面旋转，但是目前没写这个方法),vector必须是单位向量
     * 将点移动到以center为原点的坐标系
     * 将向量vector旋转到z轴，形成旋转矩阵
     * 对点进行旋转，形成旋转矩阵
     * 将点转回原来的vector的方向，形成旋转矩阵
     * 将点移回世界坐标系
     *
     * 上面中间三个步骤可以形成一个矩阵(五个矩阵右乘)，再乘那个点就行，这五个矩阵乘出来的可以直接用最普通的旋转矩阵相乘算出来
     * 参考 https://www.cnblogs.com/zhoug2020/p/7842808.html
     * [
     *  [ v0^2+(1-v0^2)*cos(a)  v0*v1*(1-cos(a))-v2*sin(a)  v0*v2*(1-cos(a))+v1*sin(a) ],
     *  [ v0*v1*(1-cos(a))+v2*sin(a)  v1^2+(1-v1^2)*cos(a)  v1*v2*(1-cos(a))-v0*sin(a) ],
     *  [ v0*v2*(1-cos(a))-v1*sin(a)  v1*v2*(1-cos(a))+v0*sin(a)  v2^2+(1-v2^2)*cos(a) ]
     * ]
     */
    public static double[] revolve3D(double[] point, Point center, Vector vector, double a) {
        if(point.length != center.size() ||point.length != vector.size()) {
            throw new IllegalArgumentException("维度不同："+point.length+" "+center.size()+" "+ vector.size());
        }
        //将点移动到以center为原点的坐标系
        double[] coordinates = move(point,-1,center.getCoordinates());

        double cosA = Math.cos(Math.toRadians(a));
        double sinA = Math.sin(Math.toRadians(a));

        Matrix matrix = new Matrix(point.length,point.length);

        matrix.set(0, 0, cosA + Math.pow(vector.get(0), 2) * (1 - cosA));
        matrix.set(0, 1, vector.get(0) * vector.get(1) * (1 - cosA) - vector.get(2) * sinA);
        matrix.set(0, 2, vector.get(0) * vector.get(2) * (1 - cosA) + vector.get(1) * sinA);

        matrix.set(1, 0, vector.get(1) * vector.get(0) * (1 - cosA) + vector.get(2) * sinA);
        matrix.set(1, 1, cosA + Math.pow(vector.get(1), 2) * (1 - cosA));
        matrix.set(1, 2, vector.get(1) * vector.get(2) * (1 - cosA) - vector.get(0) * sinA);

        matrix.set(2, 0, vector.get(2) * vector.get(0) * (1 - cosA) - vector.get(1) * sinA);
        matrix.set(2, 1, vector.get(2) * vector.get(1) * (1 - cosA) + vector.get(0) * sinA);
        matrix.set(2, 2, cosA + Math.pow(vector.get(2), 2) * (1 - cosA));

        coordinates = matrixVectorMultiplication(matrix,coordinates);
        //将点移回世界坐标系
        coordinates = move(coordinates,1,center.getCoordinates());
        return coordinates;
    }

    // 矩阵求逆
    public static Matrix inversion(Matrix matrix) {
        int n = matrix.getRow();
        //创建单位矩阵
        Matrix identity = new Matrix(matrix.getRow(), matrix.getColumn());
        //填充单位矩阵
        for (int i = 0; i < matrix.getRow(); i++) {
            identity.set(i, i, 1);
        }
        Matrix[] augmented = new Matrix[2];
        augmented[0] = matrix;
        augmented[1] = identity;
        Matrix[] result = gaussianElimination(augmented);
        return result[1];
    }

    // 高斯消元法求解线性方程组 AX = B
    private static Matrix[] gaussianElimination(Matrix[] augmented) {
        int n = augmented[0].getRow();
        Matrix a = augmented[0];
        Matrix b = augmented[1];

        for (int i = 0; i < n; i++) {
            // 寻找主元
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(a.get(k, i)) > Math.abs(a.get(maxRow, i))) {
                    maxRow = k;
                }
            }
            // 交换行
            swapRows(a, i, maxRow);
            swapRows(b, i, maxRow);

            // 归一化主元行
            double divisor = a.get(i, i);
            for (int j = i; j < n; j++) {
                a.set(i, j, a.get(i, j) / divisor);
            }
            for (int j = 0; j < b.getColumn(); j++) {
                b.set(i, j, b.get(i, j) / divisor);
            }

            // 消去其他行的当前列元素
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = a.get(k, i);
                    for (int j = i; j < n; j++) {
                        a.set(k, j, a.get(k, j) - factor * a.get(i, j));
                    }
                    for (int j = 0; j < b.getColumn(); j++) {
                        b.set(k, j, b.get(k, j) - factor * b.get(i, j));
                    }
                }
            }
        }
        return new Matrix[]{a, b};
    }

    //交换行
    public static void swapRows(Matrix matrix, int i, int j) {
        for (int k = 0; k < matrix.getColumn(); k++) {
            double temp = matrix.get(i, k);
            matrix.set(i, k, matrix.get(j, k));
            matrix.set(j, k, temp);
        }
    }

    // LU 分解方法
    public static Matrix[] LUDecomposition(Matrix matrix) {
        int n = matrix.getRow();
        int m = matrix.getColumn();
        Matrix L = new Matrix(n, m); // 下三角矩阵
        Matrix U = new Matrix(n, m); // 上三角矩阵

        for (int i = 0; i < n; i++) {
            // 构造 U 矩阵的第 i 行
            for (int j = i; j < m; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += L.get(i, k) * U.get(k, j);
                }
                U.set(i, j, matrix.get(i, j) - sum);
            }

            // 构造 L 矩阵的第 i 列
            for (int j = i + 1; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += L.get(j, k) * U.get(k, i);
                }
                L.set(j, i, (matrix.get(j, i) - sum) / U.get(i, i));
            }
            L.set(i, i, 1); // 对角线元素设为 1
        }

        return new Matrix[]{L, U};
    }

    // 前代法
    public static double[] forwardSubstitution(Matrix L, double[] b) {
        int n = L.getRow();
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L.get(i, j) * y[j];
            }
            y[i] = (b[i] - sum) / L.get(i, i);
        }
        return y;
    }

    // 后代法
    public static double[] backwardSubstitution(Matrix U, double[] y) {
        int n = U.getRow();
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U.get(i, j) * x[j];
            }
            x[i] = (y[i] - sum) / U.get(i, i);
        }
        return x;
    }

    //计算以matrix为基坐标的投影矩阵 A*(A'*A)^(-1)*A'
    public static Matrix projection(Matrix matrix) {
        //A'
        Matrix transpose = transpose(matrix);
        //A'*A
        Matrix product = matrixMultiplication(transpose, matrix);
        //(A'*A)^(-1)
        Matrix inverseProduct = inversion(product);
        //A*(A'*A)^(-1)*A'
        return matrixMultiplication(matrixMultiplication(matrix, inverseProduct), transpose);
    }

    //矩阵转置
    public static Matrix transpose(Matrix matrix) {
        //创建转置矩阵
        Matrix transposeMatrix = new Matrix(matrix.getColumn(), matrix.getRow());
        for (int i = 0; i < matrix.getColumn(); i++) {
            for (int j = 0; j < matrix.getRow(); j++) {
                transposeMatrix.set(i, j, matrix.get(j, i));
            }
        }
        return transposeMatrix;
    }

    //矩阵乘法
    public static Matrix matrixMultiplication(Matrix matrix1, Matrix matrix2) {
        // 检查矩阵是否可以相乘
        if (matrix1.getColumn() != matrix2.getRow()) {
            throw new IllegalArgumentException("矩阵的列数必须等于另一个矩阵的行数:" + matrix1.getColumn() + "!=" + matrix2.getRow());
        }
        // 创建结果矩阵
        Matrix resultMatrix = new Matrix(matrix1.getRow(), matrix2.getColumn());
        // 进行矩阵乘法运算
        for (int i = 0; i < resultMatrix.getRow(); i++) {
            for (int j = 0; j < resultMatrix.getColumn(); j++) {
                double sum = 0;
                for (int k = 0; k < matrix1.getColumn(); k++) {
                    sum += matrix1.get(i, k) * matrix2.get(k, j);
                }
                resultMatrix.set(i, j, sum);
            }
        }
        return resultMatrix;
    }

    //矩阵和向量的乘法
    public static double[] matrixVectorMultiplication(Matrix matrix, double[] point) {
        // 检查矩阵的列数是否等于向量的大小
        if (matrix.getColumn() != point.length) {
            throw new IllegalArgumentException("矩阵的列数必须等于向量的大小:" + matrix.getColumn() + "!=" + point.length);
        }

        // 创建结果向量
        double[] result = new double[matrix.getRow()];

        // 进行矩阵和向量的乘法运算
        for (int i = 0; i < result.length; i++) {
            double sum = 0;
            for (int j = 0; j < matrix.getColumn(); j++) {
                sum += matrix.get(i, j) * point[j];
            }
            result[i] = sum;
        }

        return result;
    }

}
