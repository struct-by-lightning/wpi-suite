package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
/**
 * A Table model that holds the user's estimate
 * @author hlong290494
 */

public class TeamEstimateTableModel extends AbstractTableModel
{
    private List<String> columnNames = new ArrayList();
    private List<List> data = new ArrayList();
    {	// Add the columns
        columnNames.add("Username");
        columnNames.add("Vote");
    }

    /**
     * Add a serialized object into a new row of table
     * @param rowData: A list of data to be added
     */
    public void addRow(List rowData)
    {
        data.add(rowData);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    /*
     * Get the number of columns
     */
    public int getColumnCount()
    {
        return columnNames.size();
    }

    /*
     * Get number of rows (number of votes)
     */
    public int getRowCount()
    {
        return data.size();
    }

    public String getColumnName(int col)
    {
        try
        {
            return columnNames.get(col);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Setting value at specific cell
     */
    public Object getValueAt(int row, int col)
    {
        return data.get(row).get(col);
    }

    /**
     * Make the cell non-editable!
     */
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /**
     * Setting object type of the column 
     */
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
};