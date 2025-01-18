package org.Tool;

import org.gameStart.multiDimensional.Matrix;
import org.gameStart.multiDimensional.objects.Point;
import org.gameStart.multiDimensional.objects.Vector;

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

    //点point的坐标平移到以newOrigin为原点的坐标
    public static Point translate(Point point, Point newOrigin) {
        if (point.size() != newOrigin.size()) {
            throw new IllegalArgumentException("两个点的维度不同:\n" + point + "\n" + newOrigin);
        }
        double[] coordinates = new double[point.size()];
        for (int i = 0; i < point.size(); i++) {
            coordinates[i] = point.get(i) - newOrigin.get(i);
        }
        return new Point(coordinates);
    }

    //将点point旋转到以matrix为基坐标的坐标系 即求Mx = p中的x
    public static Point revolve(Point point, Matrix matrix) {
        //LU分解
        Matrix[] matrices = LUDecomposition(matrix);
        Matrix L = matrices[0]; // 下三角矩阵
        Matrix U = matrices[1]; // 上三角矩阵
        //Ly = p
        Point y = forwardSubstitution(L, point);
        //Ux = p
        Point x = backwardSubstitution(U, y);
        return x;
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
    public static Point forwardSubstitution(Matrix L, Point b) {
        int n = L.getRow();
        Point y = new Point(n);
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L.get(i, j) * y.get(j);
            }
            y.set(i, (b.get(i) - sum) / L.get(i, i));
        }
        return y;
    }

    // 后代法
    public static Point backwardSubstitution(Matrix U, Point y) {
        int n = U.getRow();
        Point x = new Point(n);
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U.get(i, j) * x.get(j);
            }
            x.set(i, (y.get(i) - sum) / U.get(i, i));
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
    public static Vector matrixVectorMultiplication(Matrix matrix, Vector vector) {
        // 检查矩阵的列数是否等于向量的大小
        if (matrix.getColumn() != vector.size()) {
            throw new IllegalArgumentException("矩阵的列数必须等于向量的大小:" + matrix.getColumn() + "!=" + vector.size());
        }

        // 创建结果向量
        Vector resultVector = new Vector(matrix.getRow());

        // 进行矩阵和向量的乘法运算
        for (int i = 0; i < resultVector.size(); i++) {
            double sum = 0;
            for (int j = 0; j < matrix.getColumn(); j++) {
                sum += matrix.get(i, j) * vector.get(j);
            }
            resultVector.set(i, sum);
        }

        return resultVector;
    }

}
