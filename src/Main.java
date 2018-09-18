public class Main {

    public static void main(String[] args) {
//        long startPro = System.currentTimeMillis();
//        printMazePathsProactive(0, 0, 10, 10, "");
//        long endPro = System.currentTimeMillis();
//
//        long startReact = System.currentTimeMillis();
//        printMazePathsReactive(0, 0, 10, 10, "");
//        long endReact = System.currentTimeMillis();
//
//        System.out.println("Time taken: " + (endPro - startPro) + "ms");
//        System.out.println("Time taken: " + (endReact - startReact) + "ms");

//        long start = System.currentTimeMillis();
//        System.out.println(countMazePath(0, 0, 15, 15));
//        long end = System.currentTimeMillis();
//        System.out.println("Time taken: " + (end - start) + "ms");

        int n = 15;
        long startMem = System.currentTimeMillis();
        System.out.println(countMazePathMemoized(0, 0, n, n, new int[n + 1][n + 1]));
        long endMem = System.currentTimeMillis();
        System.out.println("Time taken: " + (endMem - startMem) + "ms");

        long startTab = System.currentTimeMillis();
        System.out.println(countMazePathTabulated(n, n));
        long endTab = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTab - startTab) + "ms");
    }

    public static void printMazePathsProactive(int srcX, int srcY, int destX, int destY, String pathSoFar) {
        if (srcX == destX && srcY == destY) { // if on the end square, print it
            System.out.println(pathSoFar);
            return;
        }

        //horizontal call
        if (srcY + 1 <= destY) { // if not on the vertical edge of the board, make the horizontal call
            printMazePathsProactive(srcX, srcY + 1, destX, destY, pathSoFar + "H");
        }

        // vertical call
        if (srcX + 1 <= destX) { // if not on the horizontal edge of the board, make the vertical call
            printMazePathsProactive(srcX + 1, srcY, destX, destY, pathSoFar + "V");
        }
    }

    public static void printMazePathsReactive(int srcX, int srcY, int destX, int destY, String pathSoFar) {
        if (srcX == destX && srcY == destY) {
            System.out.println(pathSoFar);
            return;
        }

        // negative base case : recover from pointless calls
        if (srcX > destX || srcY > destY) {
            return;
        }
        //horizontal call
        printMazePathsReactive(srcX, srcY + 1, destX, destY, pathSoFar + "H");
        //vertical call
        printMazePathsReactive(srcX + 1, srcY, destX, destY, pathSoFar + "V");
    }

    public static int countMazePath(int srcX, int srcY, int destX, int destY) {
        if (srcX == destX && srcY == destY) { // if already on destination square, there is only 1 path (not moving)
            return 1;
        }

        // if outside the board bounds, no path
        if (srcX > destX || srcY > destY) {
            return 0;
        }

        int countSrcToDest = 0;
        // horizontal call
        int countInterHorizToDest = countMazePath(srcX, srcY + 1, destX, destY);
        // vertical call
        int countInterVertToDest = countMazePath(srcX + 1, srcY, destX, destY);
        countSrcToDest = countInterHorizToDest + countInterVertToDest;

        return countSrcToDest;
    }

    public static int countMazePathMemoized(int srcX, int srcY, int destX, int destY, int[][] memoizedArr) {
        if (srcX == destX && srcY == destY) { // if already on destination square, there is only 1 path (not moving)
            return 1;
        }

        // if outside the board bounds, no path
        if (srcX > destX || srcY > destY) {
            return 0;
        }
        // if we have already solved the problem, why calculate? Simply take it from the 2D Array
        if (memoizedArr[srcX][srcY] != 0) {
            return memoizedArr[srcX][srcY];
        }

        int countSrcToDest = 0;
        // horizontal call
        int countInterHorizToDest = countMazePathMemoized(srcX, srcY + 1, destX, destY, memoizedArr);
        // vertical call
        int countInterVertToDest = countMazePathMemoized(srcX + 1, srcY, destX, destY, memoizedArr);
        countSrcToDest = countInterHorizToDest + countInterVertToDest;

        // store in memoized array before returning
        memoizedArr[srcX][srcY] = countSrcToDest;
        return countSrcToDest;
    }

    public static int countMazePathTabulated(int destX, int destY) {
        int[][] f = new int[destX + 1][destY + 1];

        for (int x = destX; x >= 0; x--) { // we move from last cell (known value) towards the one we want (first cell)
            for (int y = destY; y >= 0; y--) {
                if (x == destX && y == destY) {  // last cell
                    f[x][y] = 1;
                } else if (x == destX) {         // last row, except last cell
                    f[x][y] += f[x][y+1];
                } else if (y == destY) {         // last column, except last cell
                    f[x][y] += f[x+1][y];
                } else {                         // any other row/column
                    f[x][y] += f[x + 1][y] + f[x][y + 1];
                }
            }
        }

        return f[0][0];
    }
}
