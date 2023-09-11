/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int VIRTUAL_TOP = 0;
    private final boolean grid[][];
    private final int gridSize;
    private final int virtualBottom;
    private final WeightedQuickUnionUF openConnections;
    private final WeightedQuickUnionUF fullConnections;

    private int totalOpenSites;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N should be greater than 0");
        grid = new boolean[n][n];
        gridSize = n;
        int gridSizeSquare = gridSize * gridSize;
        virtualBottom = gridSizeSquare + 1;
        int openConnectionsSize = gridSizeSquare + 2;
        int fullConnectionsSize = gridSizeSquare + 1;
        openConnections = new WeightedQuickUnionUF(openConnectionsSize);
        fullConnections = new WeightedQuickUnionUF(fullConnectionsSize);
    }

    private boolean isOnGrid(int row, int col) {
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftRow >= 0 && shiftCol >= 0 && shiftRow < gridSize && shiftCol < gridSize);
    }

    private void validateSite(int row, int col) {
        if (!isOnGrid(row, col)) throw new IllegalArgumentException("Index is out of grid size");
    }

    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    public void open(int row, int col) {
        validateSite(row, col);
        if (isOpen(row, col)) return;
        grid[row - 1][col - 1] = true;

        totalOpenSites++;

        int connectionIndex = connectionsIndex(row, col);

        // Top Row. Connects open site with virtual top
        if (row == 1) {
            openConnections.union(VIRTUAL_TOP, connectionIndex);
            fullConnections.union(VIRTUAL_TOP, connectionIndex);
        }

        // Bottom Row. Connects open site with virtual bottom
        if (row == gridSize) {
            openConnections.union(virtualBottom, connectionIndex);
        }

        // Connects open site with the site on the left if it is open
        connect(connectionIndex, row, col - 1);

        // Connects open site with the site on the right if it is open
        connect(connectionIndex, row, col + 1);

        // Connects open site with above site if it is open
        connect(connectionIndex, row - 1, col);

        // Connects open site with below site if it is open
        connect(connectionIndex, row + 1, col);


    }

    private int connectionsIndex(int row, int col) {
        return gridSize * (row - 1) + col;
    }

    public boolean percolates() {
        return openConnections.find(VIRTUAL_TOP) == openConnections.find(virtualBottom);
    }

    public int numberOfOpenSites() {
        return totalOpenSites;
    }

    public boolean isFull(int row, int col) {
        validateSite(row, col);
        boolean site = grid[row - 1][col - 1];
        if (!site) return false;
        return fullConnections.find(VIRTUAL_TOP) == fullConnections.find(
                connectionsIndex(row, col));
    }

    private void connect(int openSiteConnectionIndex, int siteRow, int siteCol) {
        if (isOnGrid(siteRow, siteCol) && isOpen(siteRow, siteCol)) {
            openConnections.union(openSiteConnectionIndex, connectionsIndex(siteRow, siteCol));
            fullConnections.union(openSiteConnectionIndex, connectionsIndex(siteRow, siteCol));
        }
    }


}
